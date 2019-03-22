from rest_framework import serializers

from .models import *


class DishPrefSerializer(serializers.ModelSerializer):

    class Meta:
        model = DishPref
        fields = [
            'nutrition'
        ]
        read_only_fields = ['user']

class UserSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = [
            'username',
            'password',
            'nutrition',
            'price'
        ]
