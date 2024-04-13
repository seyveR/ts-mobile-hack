from django.db import models

# Create your models here.


class Ticket(models.Model):
    date = models.CharField(max_length=200)
    description = models.CharField(max_length=200, default=None)
    district = models.CharField(max_length=200, default=None)
    area = models.CharField(max_length=200)
    product_name = models.CharField(max_length=100)
    category = models.CharField(max_length=100, default=None)
    price_tag = models.DecimalField(max_digits=7, decimal_places=2, default=None)
    gramms = models.CharField(max_length=100, default=None)
    price_for_kg = models.DecimalField(max_digits=7, decimal_places=2, default=None)
    price_rost = models.CharField(max_length=200, default=None)
    social_relevance = models.BooleanField()

