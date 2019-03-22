from .views import *
from django.conf.urls import url
from django.urls import path

urlpatterns = [
        path('dish/', DishPrefAPIView.as_view(), name = 'dishv'),
        path('user/', UserAPIView.as_view(), name = 'userv'),

]