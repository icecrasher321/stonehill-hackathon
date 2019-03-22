from .views import *
from django.conf.urls import url
from django.urls import path, re_path

urlpatterns = [
        path('dish/', DishPrefAPIView.as_view(), name = 'dishv'),
        re_path('user/(?P<pk>\d+)', UserUpdateAPIView.as_view())


]