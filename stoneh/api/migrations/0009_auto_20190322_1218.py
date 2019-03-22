# Generated by Django 2.1.7 on 2019-03-22 12:18

import django.core.validators
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0008_auto_20190322_1042'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='dish',
            options={'verbose_name_plural': 'Dishes'},
        ),
        migrations.AlterModelOptions(
            name='dishpref',
            options={'verbose_name_plural': 'Dish Preferences'},
        ),
        migrations.AlterModelOptions(
            name='restaurant',
            options={'verbose_name_plural': 'Restaurant Name'},
        ),
        migrations.AlterModelOptions(
            name='restaurantpref',
            options={'verbose_name_plural': 'Restaurant Preferences'},
        ),
        migrations.AddField(
            model_name='dishpref',
            name='rating',
            field=models.IntegerField(default=0, validators=[django.core.validators.MaxValueValidator(100), django.core.validators.MinValueValidator(1)]),
        ),
        migrations.AlterField(
            model_name='user',
            name='nutrition',
            field=models.IntegerField(default=0, validators=[django.core.validators.MaxValueValidator(100), django.core.validators.MinValueValidator(1)]),
        ),
    ]