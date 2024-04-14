from ultralytics import YOLO
from PIL import Image

import easyocr
from ultralytics.engine.results import Results

class Model():

    def __init__(self, model_path) -> None:
        self.model = YOLO(model=model_path)

    @classmethod
    def from_pretrained(cls, path: str = "./apiserver/models/PricesOnly.pt"):
        return cls(path)
    

    def predict(self, image_path: str, save_image: bool = False) -> dict:
        image = Image.open(image_path)

        results: Results = self.model.predict(image, conf=0.55, iou=0.7, save=False)
        text_results = self.text_recognition_in_boxes(image, results, save_image)
        print(text_results)
        formatted_results = self.format_results(text_results)
        print(formatted_results)
        return formatted_results


    def text_recognition_in_boxes(self, image, results, save_image: bool = False) -> dict:
        # reader = easyocr.Reader(["ru", "en"])
        reader_price = easyocr.Reader(["ru"])
        text_results = {} 

        for result in results:
            names = result.names
            boxes = result.boxes
            for box in boxes: 
                x, y, x2, y2 = box.xyxy.ravel()
                x, y, x2, y2 = int(x), int(y), int(x2), int(y2)
                cls = box.cls.item()
                text = None
                image = result.orig_img.copy()
                cropped_image = image[y:y2, x:x2]
                text = reader_price.readtext(cropped_image, detail=0, link_threshold=0.05)

                if names[cls] not in text_results or len(text) > 0 and len(text_results[names[cls]]) == 0:
                    text_results[names[cls]] = text
            if save_image:
                result.save(filename=f'DetectedPriceImage.jpg')
        return text_results
    

    def format_results(self, text_results) -> dict:
        formatted_results = {}

        for category, text_list in text_results.items():
            if text_list is None or len(text_list) == 0:
                continue 
            
            if category == 'rubles':
                formatted_results['rubles'] = text_list[0] if text_list else None
            elif category == 'copeicas':
                formatted_results['copeicas'] = text_list[0] if text_list else None

        return formatted_results