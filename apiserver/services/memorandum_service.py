import pandas as pd
from typing import Optional
from decimal import Decimal

class Memorandum:

    def __init__(self, path: str = './Memorandum.csv') -> None:
        self.data = pd.read_csv(path, sep=';')
        self.product_names = self.data['Наименование товара/группы товаров'].values
        self.prices = self.data['Цена региона-аналога (Ростова-на-Дону)'].values


    def is_in_memorandum(self, product_name: str) -> str:
        for product in self.product_names:
            if product_name in product:
                if product != "Прочее":
                    return product
        return "нет категории"
    
    def get_price(self, product_name: str) -> Optional[float]:
        for name, price in zip(self.product_names, self.prices):
            if product_name == name:
                # Replace comma with period and convert to float
                return float(price.replace(',', '.'))
        return None
    