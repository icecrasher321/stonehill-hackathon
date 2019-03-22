from django import forms
from django.contrib.auth.forms import UserCreationForm, UserChangeForm
from django.contrib.auth.models import User
from .models import *

class UserInfoForm(UserCreationForm):

    class Meta:
        model = User
        fields = ('username', 'nutrition', 'price', )

class DishForm(forms.ModelForm):

    class Meta:
        model = Dish
        fields = ('price', 'name',)

class RestaurantForm(forms.ModelForm):

    class Meta:
        model = Restaurant
        fields = ('rest_name', 'cuisine',)