# Generated by Django 5.0.4 on 2024-04-13 16:14

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Tickets', '0003_alter_ticket_date'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ticket',
            name='price',
            field=models.DecimalField(decimal_places=2, max_digits=7),
        ),
    ]