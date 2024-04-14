from ultralytics import YOLO
from PIL import Image
from ultralytics.engine.results import Results

class Model():

    def __init__(self, model_path) -> None:
        self.model = YOLO(model=model_path)

    @classmethod
    def from_pretrained(cls, path: str = "./apiserver/models/Yolov8segS.pt"):
        return cls(path)
    
    def _get_padding(self, x1, y1, x2, y2, orig_shape, padding: int = 100):
        print(orig_shape)
        clamp = lambda x, minimum, maximum: max(minimum, min(maximum, x))
        x1 = clamp(x1 - padding, 0, orig_shape[1])
        x2 = clamp(x2 + padding, 0, orig_shape[1])
        y1 = clamp(y1 - padding, 0, orig_shape[0])
        y2 = clamp(y2 + padding, 0, orig_shape[0])
        return x1, y1, x2, y2


    def predict(self, image_path: str) -> str:
        """Return the saved crop image path"""
        results: Results = self.model.predict(image_path,
                                              save=False,
                                            #   conf=0.55, iou=0.7
                                              )
        # clamp = lambda x: max()
        for result in results:
            boxes = result.boxes
            for box in boxes: 
                x, y, x2, y2 = box.xyxy.ravel()
                x, y, x2, y2 = int(x), int(y), int(x2), int(y2)
                cls = box.cls.item()
                
                # fix bgr to rgb
                input_image = Image.fromarray(result.orig_img[:, :, ::-1]) 
                x, y, x2, y2 = self._get_padding(x, y, x2, y2, result.orig_shape, padding=250)
                license_plate = input_image.crop((x, y, x2, y2))

                plate_filename = f'CropedImage.jpg'
                license_plate.save(plate_filename)
        return plate_filename


if __name__ == "__main__":
    model = Model.from_pretrained()
    image_path = 'results.jpg'
    model.predict(image_path)