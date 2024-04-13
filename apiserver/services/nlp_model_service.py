import transformers
from transformers import BertForSequenceClassification, AutoTokenizer
import torch

class Model():
    def __init__(self, path) -> None:
        self.device = 'cuda' if torch.cuda.is_available() else 'cpu'
        self.tokenizer = AutoTokenizer.from_pretrained("cointegrated/rubert-tiny2")
        self.model = BertForSequenceClassification.from_pretrained(path).to(self.device)


    @classmethod
    def from_pretrained(cls, path: str = './apiserver/models/last'):
        return cls(path)


    def predict(self, string: str):
        string = string.lower()
        inputs = self.tokenizer(string, return_tensors="pt").to(self.device)
        with torch.inference_mode():
            outputs = self.model(**inputs)
        predicted_class_id = outputs.logits.argmax().item()
        out = self.model.config.id2label[predicted_class_id]
        return out


if __name__ == "__main__":
    model = Model.from_pretrained()
    out = model.predict('Бабушкина крынка творог 400г Деревенский 5% пакет')
    print(out)