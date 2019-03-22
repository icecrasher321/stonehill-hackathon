
from django.contrib.auth.models import AbstractUser
from django.db import models
from django.core.validators import MinValueValidator, MaxValueValidator
# Create your models here.
from multiselectfield import MultiSelectField
from .list import a
from .DishAlgo import init_cuisine

    #height = models.IntegerField(default = 0)
    #gender = models.BooleanField(default=False)
    #age = models.IntegerField(default = 0)
    #weight = models.DecimalField(decimal_places=6, max_digits=6)
class User(AbstractUser):

    nutrition =  models.IntegerField(default = 0, validators=[
            MaxValueValidator(100),
            MinValueValidator(1)
        ])
    age = models.IntegerField(null = True)
    price = models.IntegerField(default = 0)
    lat = models.CharField(max_length = 30, null = True)
    long = models.CharField(max_length=30, null = True)
    nutritionw = models.DecimalField(decimal_places=4, max_digits=6, null = True, default = 0.25)
    disw = models.DecimalField(decimal_places=4, max_digits=6, null = True, default = 0.25)
    cuisw = models.DecimalField(decimal_places=4, max_digits=6, null = True, default = 0.25)
    ratew = models.DecimalField(decimal_places=4, max_digits=6, null = True, default = 0.25)
    pricew = models.DecimalField(decimal_places=4, max_digits=6, null = True, default = 0.25)
    cuis_matrix = models.CharField(max_length = 500, null = True, default = init_cuisine())
    preference = models.CharField(max_length = 30, null = True)


    class Meta:
        verbose_name_plural = "User"

class DishPref(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, default=None, null=True)

    nutrition = models.IntegerField(default = 0)
    eaten_dish = models.CharField(max_length =30)
    class Meta:
        verbose_name_plural = "Dish Preferences History"

class RestaurantPref(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, default=None, null=True)
    type = models.CharField(max_length = 30)
    price = models.DecimalField(max_digits = 7, decimal_places=2, default = 0)
    rating = models.IntegerField(default = 0, validators=[
            MaxValueValidator(5),
            MinValueValidator(1)
        ])
    item1 = models.BooleanField(default = False)
    item2 = models.BooleanField(default = False)
    item3 = models.BooleanField(default = False)

    class Meta:
        verbose_name_plural = "Restaurant Preferences History"

class Restaurant(models.Model):
    user = models.ForeignKey(User,on_delete=models.CASCADE, default=None, null=True)
    rest_name = models.CharField(max_length = 30)
    cuisine = MultiSelectField(choices = a, max_choices=10)

    def __str__(self):
        return self.rest_name
    class Meta:
        verbose_name_plural = "Restaurants"



class Dish(models.Model):
    name = models.CharField(max_length = 30)
    price = models.DecimalField(max_digits = 7, decimal_places=2, default = 0)
    restaurant = models.ForeignKey(Restaurant, on_delete=models.CASCADE)

    class Meta:
        verbose_name_plural = "Dishes"

    def __str__(self):
        return self.name
