from django.db import models

# Create your models here.


class Ticket(models.Model):
    date = models.CharField(max_length=200)
    store_address = models.CharField(max_length=200)
    product_name = models.CharField(max_length=100)
    price = models.DecimalField(max_digits=7, decimal_places=2)
    social_relevance = models.BooleanField()

