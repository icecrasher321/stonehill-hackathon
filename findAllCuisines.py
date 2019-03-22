from pyzomato import Pyzomato
from math import *
import requests, json

p = Pyzomato("c9637e1f0c0413b00d0d182199c7bf62")
cuislist = []
allcuisines = p.getCuisines(4)[u'cuisines'] ## Hardcoded for Bangalore
for cuisob in allcuisines:
  cuisineinfo = cuisob[u'cuisine']
  cuislist.append(cuisineinfo[u'cuisine_name'])

print(cuislist)