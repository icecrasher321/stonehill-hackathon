
from .models import *

user = User.objects.get(username = "admin")
dishes = Dish.objects.all(restaurant = user.restaurant)
print(dishes)

