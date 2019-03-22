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


headers = {
    'x-app-id': 'ad5a69a3',
    'x-app-key': '355005d5e8bb9b8b6b91b172ebe74e0b'
}


dish = "Vegetable Hakka Noodles"
data = '{"query":"%s"}' % dish
response = requests.post('https://trackapi.nutritionix.com/v2/natural/nutrients', headers=headers, data=data)
json_data = response.json()

totalfats = 0
fibre = 0
carbs = 0
protein = 0
sat_fats = 0
sugars = 0
cholestrol = 0
sodium = 0

for i in range(len(json_data[u'foods'])):
    totalfats += json_data[u'foods'][i][u'nf_total_fat']
    fibre += json_data[u'foods'][i][u'nf_dietary_fiber']
    carbs += json_data[u'foods'][i][u'nf_dietary_fiber']
    protein += json_data[u'foods'][i][u'nf_protein']
    sat_fats += json_data[u'foods'][i][u'nf_saturated_fat']
    sugars += json_data[u'foods'][i][u'nf_sugars']
    cholestrol += json_data[u'foods'][i][u'nf_cholesterol']
    sodium += json_data[u'foods'][i][u'nf_sodium']

def findProteinIndex(age):
    overThirty = (age - 30)
    if overThirty > 0:
        required = (1.05*overThirty*(5/3))
    else:
        required = (50/3)
    protein_index = abs(required - protein)
    return protein_index


def findCarbsIndex():

    required = (310/3)
    carbs_index = abs(required - carbs)
    return carbs_index


def findFatIndex():

    required = (70/3)
    fats_index = abs(required - totalfats)
    return fats_index


def findSATFatIndex():

    required = 8
    sat_fats_index = abs(required - totalfats)
    return sat_fats_index


def findFibreIndex(age):

    if age < 5:
        required = 5
    elif age < 11:
        required = 20/3
    elif age < 16:
        required = 25/3
    else:
        required = 10
    fibre_index = abs(required - fibre)
    return fibre_index


def findSodiumIndex():

    required = 2.3/3
    sodium_index = abs(required - totalfats)
    return sodium_index


def findSugarsIndex():

    required = 10
    sugars_index = abs(required - totalfats)
    return sugars_index


def findDishHealth(age):

    dish_health_index = findSugarsIndex() + findSodiumIndex() + findFibreIndex(age) + findFatIndex() + findSATFatIndex() + findCarbsIndex() + findProteinIndex(age)
    return dish_health_index

