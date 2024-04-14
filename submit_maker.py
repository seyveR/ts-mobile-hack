# Не понял нихуя
# а как не зависимым то его сделать он же должен модели кушать
import pandas as pd
import os

from apiserver.api_server import get_weight
from apiserver.services.seg_model_service import Model as Seg_Model
from apiserver.services.price_detect_model_service import Model as DetectPrice_Model
from apiserver.services.name_detect_model_service import Model as DetectName_Model
from apiserver.services.nlp_model_service import Model as NLP_model
from apiserver.services.memorandum_service import Memorandum


seg_model = Seg_Model.from_pretrained("./apiserver/models/Yolov8segS.pt")
detect_price_ocr_model = DetectPrice_Model.from_pretrained("./apiserver/models/PricesOnly.pt")
detect_name_ocr_model = DetectName_Model.from_pretrained("./apiserver/models/namesgramms.pt")
nlp_model = NLP_model.from_pretrained("./apiserver/models/nlp_model")
memoranum_control = Memorandum('Memorandum.csv')



# бля тут хз как итерироваться условно у вас скорее всего будет csv + folder с фотками тогда вот
data_folder = "test_data_folder"

results: list[dict] = []

for file in os.listdir(data_folder):
# for file in ['IMG_5778.jpg']:
    print(f'Работаю над {file}')
    image_path = os.path.join(data_folder, file)

     # Обрезка ценика на фотографии seg моделью
    seg_image_path = seg_model.predict(image_path)

    # Находим название и цену для подачи в OCR
    # Запускаем OCR
    predictions = detect_price_ocr_model.predict(seg_image_path, save_image=True)
    rubles, copeicas = predictions.get('rubles', None), predictions.get('copeicas', None)
    if copeicas is None:
        copeicas = 0
    print(f'{rubles=} {copeicas=}')

    predictions = detect_name_ocr_model.predict(seg_image_path, save_image=True)
    name, gramms = predictions.get('name', None), predictions.get('gramms', None)
    print(f'{name=} {gramms=}')

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
        category = 'Прочее'

    print(f'{category=}')

    weight, unit = get_weight(gramms)
    print(f'{weight=} {unit=}')

    # имя файла + категория + цена
    results.append({
        'Наименование файла': file[:-4], # берем без .jpg
        'Категория продукта': category,
        'Цена': float(f'{rubles}.{copeicas}'),
    })

df = pd.DataFrame(results)
df.to_csv('МамуЕбал.csv', sep=';', index=False)