from django.contrib import admin
from api.models import *
# Register your models here.
admin.site.register(User)
admin.site.register(DishPref)
admin.site.register(RestaurantPref)
admin.site.register(Restaurant)
admin.site.register(Dish)