# Generated by Django 5.0.4 on 2024-04-14 03:44

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Tickets', '0009_history'),
    ]

    operations = [
        migrations.AddField(
            model_name='history',
            name='ids',
            field=models.IntegerField(default=0, max_length=100000),
        ),
    ]
