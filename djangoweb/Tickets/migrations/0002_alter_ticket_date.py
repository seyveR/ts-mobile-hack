# Generated by Django 5.0.4 on 2024-04-13 15:11

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Tickets', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ticket',
            name='date',
            field=models.DateTimeField(),
        ),
    ]
