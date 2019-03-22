import math

cuisines = ['Afghan', 'African', 'American', 'Andhra', 'Arabian', 'Asian', 'Assamese', 'Australian', 'Awadhi', 'BBQ', 'Bakery', 'Bar Food', 'Belgian', 'Bengali', 'Beverages', 'Bihari', 'Biryani', 'Bohri', 'British', 'Bubble Tea', 'Burger', 'Burmese', 'Cafe', 'Cantonese', 'Charcoal Chicken', 'Chettinad', 'Chinese', 'Coffee', 'Continental', 'Cuisine Varies', 'Desserts', 'Drinks Only', 'European', 'Fast Food', 'Finger Food', 'French', 'German', 'Goan', 'Greek', 'Grill', 'Gujarati', 'Healthy Food', 'Hot dogs', 'Hyderabadi', 'Ice Cream', 'Indonesian', 'Iranian', 'Italian', 'Japanese', 'Jewish', 'Juices', 'Kashmiri', 'Kebab', 'Kerala', 'Konkan', 'Korean', 'Lebanese', 'Lucknowi', 'Maharashtrian', 'Malaysian', 'Malwani', 'Mangalorean', 'Mediterranean', 'Mexican', 'Middle Eastern', 'Mithai', 'Modern Indian', 'Momos', 'Mongolian', 'Mughlai', 'Naga', 'Nepalese', 'North Eastern', 'North Indian', 'Oriya', 'Paan', 'Pan Asian', 'Parsi', 'Pizza', 'Portuguese', 'Rajasthani', 'Raw Meats', 'Roast Chicken', 'Rolls', 'Russian', 'Salad', 'Sandwich', 'Seafood', 'Sindhi', 'Singaporean', 'Soul Food', 'South American', 'South Indian', 'Spanish', 'Sri Lankan', 'Steak', 'Street Food', 'Sushi', 'Tamil', 'Tea', 'Tex-Mex', 'Thai', 'Tibetan', 'Turkish', 'Vegan', 'Vietnamese', 'Wraps']

for i in range (1,len(cuisines * 2)):
    if (i%2 == 1):
        cuisines.insert(i-1, math.ceil(i/2))

it = iter(cuisines)
zip(it, it)
a = tuple(zip(*[iter(cuisines)]*2))



