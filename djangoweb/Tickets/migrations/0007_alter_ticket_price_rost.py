# Generated by Django 5.0.4 on 2024-04-13 19:49

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Tickets', '0006_alter_ticket_gramms'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ticket',
            name='price_rost',
            field=models.CharField(default=None, max_length=200),
        ),
    ]
