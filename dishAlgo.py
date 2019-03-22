from pyzomato import Pyzomato
from math import *
import requests, json
import operator

## Lat, long from tiru
lat = 12.96166
lon = 77.752940


def findCuisineValue(cuisines):
	cuisval = 0
	for c in cuisines:
		cuisval += cuismatrix[c]
	return cuisval

def distanceBetween(lat1, lon1, lat2, lon2):
	## return math.pow((lat1-lat2)**2+(lon1-lon2)**2, 1/2)
	return .637101 * acos(sin(lat1)*sin(lat2) + cos(lat1)*cos(lat2)*cos(lon1 - lon2))

def findHealthIndex(restaurant):
	dishes = [] ## Call menu api or use Rohan's implementation
	totalhealthindex = 0
	for dish in dishes:
		totalhealthindex += findDishHealth(dish)
	return totalhealthindex/len(dishes)

def get_restaurants(lat, lon, userpreferences):

	p = Pyzomato("c9637e1f0c0413b00d0d182199c7bf62")
	geocode_data = p.getByGeocode(lat, lon)
	nearbyrest = geocode_data[u'popularity'][u'nearby_res']

	if first_time_user:
		initialize_variables()

	restindex = {}
	nameval = {}

	for rest in nearbyrest:
		rest = int(rest)
		details = p.getRestaurantDetails(rest)
		restlat = float(details[u'location'][u'latitude'])
		restlon = float(details[u'location'][u'longitude'])
		price = int(details[u'price_range'])
		rating = float(details[u'user_rating'][u'aggregate_rating'])
		locuisine = details[u'cuisines']
		cuisines = locuisine.split(', ')
		cuisval = findCuisineValue(cuisines)
		distance = distanceBetween(lat, lon, restlat, restlat)
		cumhealth = findHealthIndex(rest)
		value = disweight*distance + 5*cuisw*cuisval + rateweight*rating - priceweight*price + nutritionweight*cumhealth
		restindex[rest] = value
		restname = details[u'name']
		nameval[restname] = value

		sorted_rest = sorted(nameval.items(), key=operator.itemgetter(1))
		sorted_rest.reverse()

		bestrest = max(nameval, key=nameval.get)

def evaluate_choices():
	## GIVEN THE RESTAURANT RATING (USED FOR CUISINE PREFERENCES), AND WHICH DISH CHOSEN (NUTRITIONAL)

	nutritional_importance = 0 ## Keeping as multiplier to the nutritional weight

	if first_choice_chosen:
		nutritional_importance += 2.5

	if second_choice_chosen:
		nutritional_importance += 2

	if third_choice_chosen:
		nutritional_importance += 1.5

	if not (first_choice_chosen or second_choice_chosen or third_choice_chosen):
		nutritional_importance = 0.8

	if restaurant_rating == 5:
		cuisine_multiplier = 3
	elif restaurant_rating == 4:
		cuisine_multiplier = 2
	elif restaurant_rating == 3:
		cuisine_multiplier = 1
	elif restaurant_rating == 2:
		cuisine_multiplier = 0.8
	elif restaurant_rating == 1:
		cuisine_multiplier = 0.6
	else:
		cuisine_multiplier = 0.4

	relevant_cuisines = findRelevantCuisines(restaurantid)

	for cuisine in relevant_cuisines:
		cuismatrix[cuisine] *= cuisine_multiplier

	## PUSH TO DATABASE AGAIN

def initialize_variables():

	## WRITE THIS TO THE DATABASE

	disweight = 0.25 ## Fixed input
	rateweight = 0.25 ## Fixed input
	priceweight = 0.25 ## Fixed input
	cuisw = 0.25 ## Fixed input
	nutritionweight = 0 ## Dynamic but asked in beginning

	init_cuisine()

def init_cuisine():
	cuislist = []
	allcuisines = p.getCuisines(4)[u'cuisines'] ## Hardcoded for Bangalore
	for cuisob in allcuisines:
		cuisineinfo = cuisob[u'cuisine']
		cuislist.append(cuisineinfo[u'cuisine_name'])
	
	## WRITE TO THE DATABASE
	cuismatrix = {}
	for c in cuislist:
		cuismatrix[c] = 1

	## Convert dict to string and truncate it when you ask for number

def findRelevantCuisines(restaurantid):

	details = p.getRestaurantDetails(rest)
	locuisine = details[u'cuisines']
	cuisines = locuisine.split(', ')

