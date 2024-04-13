from fastapi import FastAPI, HTTPException, Query, Request
from pydantic import BaseModel
import requests
from typing import Optional
import os
from urllib.parse import urlparse, quote
import numpy as np
from datetime import datetime, timezone
from decimal import Decimal

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
    address: str 
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
    address = request.address
    description = request.description
    print(address)
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

    weight, weight_name = get_weight(gramms)
    print('weight', weight, weight_name)
    date_now = datetime.now(timezone.utc).strftime("%Y-%m-%d %H:%M:%S")
    date = str(date_now)

    rel = 0
    # category = memoranum_control.is_in_memorandum(category)
    price_in_memorandum = memoranum_control.get_price(category)
    rubles_float = float(f'{rubles}.{copeicas}')
    print(rubles_float)
    print(price_in_memorandum)

    print(weight)
    if weight_name == 'г' or weight_name == 'Г':
        multiplier = 1000 / weight
        weight_adjusted = weight * multiplier
        rubles_float_adjusted = rubles_float * multiplier

        print(f"Adjusted rubles_float: {rubles_float_adjusted}")
    elif weight_name == 'кг' or weight_name == 'КГ' or weight_name == 'Кг' or weight_name == 'кГ' or weight_name == 'Л' or weight_name == 'л':
        weight = weight*100
        multiplier = 1000 / weight
        weight_adjusted = weight * multiplier
        rubles_float_adjusted = rubles_float * multiplier

        print(f"Adjusted rubles_float: {rubles_float_adjusted}")
    else:
        print("Weight is less than 50, no adjustment needed.")

    if price_in_memorandum is not None:
        if rubles_float_adjusted <= price_in_memorandum:
            rel = 0
        else:
            rel = 1
    else:
        rel = 0

    # Формируем запрос для записи в базу
    data = {
        "prod_name": name,
        'prod_price': rubles_float,
        'date': date,
        'address': address,
        'rel': rel
    }

    responce = requests.post('http://localhost:8080/add_record/', json=data)


    # Responce {резульат if in memorandum else ты дурак}
    return {"category": category}


def get_weight(string_weight: str) -> int:
    weight = []
    name = []
    for s in string_weight:
        if s.isdigit():
            weight.append(s)
        elif s == 'О' or s == 'о':
            weight.append('0')
        elif s == 'З' or s == 'з':
            weight.append('3')
        else:
            name.append(s)
    return (int(''.join(weight)), ''.join(name))