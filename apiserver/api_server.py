from fastapi import FastAPI, HTTPException, Query, Request
from pydantic import BaseModel
import requests
from typing import Optional
import os
from urllib.parse import urlparse, quote
import numpy as np
from datetime import datetime, timezone
from decimal import Decimal
import pytz
import re

from .services.seg_model_service import Model as Seg_Model
from .services.price_detect_model_service import Model as DetectPrice_Model
from .services.name_detect_model_service import Model as DetectName_Model
from .services.nlp_model_service import Model as NLP_model
from .services.memorandum_service import Memorandum

app = FastAPI()

seg_model = Seg_Model.from_pretrained("./apiserver/models/Yolov8segS.pt")
detect_price_ocr_model = DetectPrice_Model.from_pretrained("./apiserver/models/PricesOnly.pt")
detect_name_ocr_model = DetectName_Model.from_pretrained("./apiserver/models/namesgramms.pt")
nlp_model = NLP_model.from_pretrained("./apiserver/models/nlp_model")
memoranum_control = Memorandum('Memorandum.csv')

class PhotoData(BaseModel):
    photo_url: str
    area: str 
    district: str 
    description: str 


@app.get("/")
async def hello_api():
    return {"Hello": "Api"}


@app.post("/phone_message")
async def send_sms(phone: PhotoData):

    print(f"Sending SMS to {phone}")
    return {"message": f"SMS sent to {phone}"}

def string_to_url(url: str) -> str:
    parsed_url = urlparse(url)
    path = parsed_url.path
    encoded_path = quote(path)
    desired_url = f"{parsed_url.scheme}://{parsed_url.netloc}{encoded_path}?{parsed_url.query}"
    return desired_url

@app.post("/predict") 
async def predict_photo(request: PhotoData):
    photo_url = request.photo_url
    area = request.area
    district = request.district
    description = request.description
    print(area)
    print(district)
    print(description)
    if '%' not in photo_url:
        print('Use string_to_url')
        photo_url = string_to_url(photo_url)


    if not os.path.exists('./apiserver/basedata'):
        os.makedirs('./apiserver/basedata')
    
    # Скачиваем и сохраняем изображение
    image_path = os.path.join('./apiserver/basedata', 'downloaded_image.jpg')
    response = requests.get(photo_url)
    
    if response.status_code != 200: 
        raise HTTPException(status_code=400, detail="Failed to download image from URL.")
    
    with open(image_path, 'wb') as f:
        f.write(response.content)


    # Обрезка ценика на фотографии seg моделью
    seg_image_path = seg_model.predict(image_path)

    # Находим название и цену для подачи в OCR
    # Запускаем OCR
    results = detect_price_ocr_model.predict(seg_image_path, save_image=True)
    rubles, copeicas = results.get('rubles', None), results.get('copeicas', None)
    if copeicas is None:
        copeicas = 0
    print(rubles, copeicas)

    results = detect_name_ocr_model.predict(seg_image_path, save_image=True)
    name, gramms = results.get('name', None), results.get('gramms', None)
    print(name, gramms)

    
    # Отдаем полученное имя товара на bert
    category = None
    if name:
        if gramms:
            name_gramm = ' '.join([name, gramms])
            category = nlp_model.predict(name_gramm)
        else:
            category = nlp_model.predict(name)
        category = memoranum_control.is_in_memorandum(category)
    else:
        category = 'На фотографии не удалось определить название, пожалуйста, сфотографируйте ценник еще раз'
    

    weight, unit = get_weight(gramms)
    print('weight', weight, unit)


    print(f'Категория: {category}')
    
    moscow_tz = pytz.timezone('Europe/Moscow')
    date_now = datetime.now(moscow_tz).strftime("%Y-%m-%d %H:%M:%S")
    date = str(date_now)
    print(date_now)

    rel = 0
    # category = memoranum_control.is_in_memorandum(category)
    price_in_memorandum = memoranum_control.get_price(category)
    rubles_float = float(f'{rubles}.{copeicas}')
    print(f'Цена товара на фото: {rubles_float}')
    print(f'Цена на категорию товара в меморандуме: {price_in_memorandum}')
    print(f'Единица измерения веса: {unit}')

    print(f'Вес товара на фото: {weight}')
    if unit.lower() == 'г'  or unit.lower() == 'мл':
        multiplier = 1000 / weight
        weight_adjusted = weight * multiplier
        rubles_float_adjusted = rubles_float * multiplier

        print(f"Цена за кг веса: {rubles_float_adjusted}")
    elif unit.lower() == 'кг' or unit.lower() == 'л':
        weight_gr = weight*1000
        multiplier = 1000 / weight_gr
        weight_adjusted = weight_gr * multiplier
        rubles_float_adjusted = rubles_float * multiplier

        print(f"Цена за кг веса: {rubles_float_adjusted}")
    elif unit == '':
        rubles_float_adjusted = rubles_float
    else:
        print("Неизвестная мера величины")

    if price_in_memorandum is not None:
        if rubles_float_adjusted <= price_in_memorandum:
            rel = 0
        else:
            rel = 1
    else:
        rel = 0

    # Формируем запрос для записи в базу
    data = {
        'date': date,
        'description': description,
        'district': district,
        'area': area,
        'prod_name': name,
        'category': category,
        'prod_price': rubles_float,
        'gramms': str(f'{weight}{unit}'),
        'price_kg': rubles_float_adjusted,
        'price_rost': price_in_memorandum,
        'rel': rel
    }

    if name:
        print('отправляю запрос')
        responce = requests.post('http://localhost:8080/add_record/', json=data)
    elif name and gramms == None:
        return {"category": 'На фотографии не удалось определить граммовку, пожалуйста, сфотографируйте ценник повторно'}

    return {"category": category}


def get_weight(string_weight: str) -> tuple[int, str]:
    pattern = re.compile(r'(\d+(?:[.,]\d+)?)\s*([A-zА-я]+)?')
    if string_weight is None:
        string_weight = '1000 г'

    string_weight = string_weight.lower().replace('з', '3').replace('о', '0').replace('o', '0')

    match = pattern.match(string_weight)
    if match:
        amount, unit = match.groups()
    else:
        amount, unit = 1000, 'г'
    
    amount = float(amount.replace(',', '.'))
    return amount, unit


if __name__ == "__main__":
    amount, unit = get_weight('4ООг')
    print(amount, unit)
    print(type(amount), type(unit))
