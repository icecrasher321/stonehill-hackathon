from rest_framework import serializers

from .models import *


class DishPrefSerializer(serializers.ModelSerializer):

    class Meta:
        model = DishPref
        fields = [
            'nutrition',
            'eaten_dish'
        ]
        read_only_fields = ['user']

class UserSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = [
            'id',
            'username',
            'password',
            'nutrition',
            'price',


        ]
        read_only_fields = ['id','username','password']

class UserUpdateSerializer(serializers.ModelSerializer):
      class Meta:
          model = User
          fields = [
            'lat',
            'long',
            'preference',
              ]
