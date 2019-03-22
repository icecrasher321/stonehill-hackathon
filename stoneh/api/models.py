
from django.contrib.auth.models import AbstractUser
from django.db import models
from django.core.validators import MinValueValidator, MaxValueValidator
# Create your models here.



class User(AbstractUser):

    nutrition =  models.IntegerField(default = 0)
    price = models.IntegerField(default = 0)
    class Meta:
        verbose_name_plural = "User"
class DishPref(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, default=None, null=True)
    nutrition = models.IntegerField(default = 0)

class RestaurantPref(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, default=None, null=True)
    cuisine = models.CharField(max_length = 30)
    type = models.CharField(max_length = 30)
    price = models.DecimalField(max_digits = 7, decimal_places=2, default = 0)



