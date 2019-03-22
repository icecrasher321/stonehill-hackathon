# Generated by Django 2.1.7 on 2019-03-22 17:18

from django.db import migrations
import multiselectfield.db.fields


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0015_auto_20190322_1705'),
    ]

    operations = [
        migrations.AlterField(
            model_name='restaurant',
            name='cuisine',
            field=multiselectfield.db.fields.MultiSelectField(choices=[(1, 'Afghan'), (2, 'African'), (3, 'American'), (4, 'Andhra'), (5, 'Arabian'), (6, 'Asian'), (7, 'Assamese'), (8, 'Australian'), (9, 'Awadhi'), (10, 'BBQ'), (11, 'Bakery'), (12, 'Bar Food'), (13, 'Belgian'), (14, 'Bengali'), (15, 'Beverages'), (16, 'Bihari'), (17, 'Biryani'), (18, 'Bohri'), (19, 'British'), (20, 'Bubble Tea'), (21, 'Burger'), (22, 'Burmese'), (23, 'Cafe'), (24, 'Cantonese'), (25, 'Charcoal Chicken'), (26, 'Chettinad'), (27, 'Chinese'), (28, 'Coffee'), (29, 'Continental'), (30, 'Cuisine Varies'), (31, 'Desserts'), (32, 'Drinks Only'), (33, 'European'), (34, 'Fast Food'), (35, 'Finger Food'), (36, 'French'), (37, 'German'), (38, 'Goan'), (39, 'Greek'), (40, 'Grill'), (41, 'Gujarati'), (42, 'Healthy Food'), (43, 'Hot dogs'), (44, 'Hyderabadi'), (45, 'Ice Cream'), (46, 'Indonesian'), (47, 'Iranian'), (48, 'Italian'), (49, 'Japanese'), (50, 'Jewish'), (51, 'Juices'), (52, 'Kashmiri'), (53, 'Kebab'), (54, 'Kerala'), (55, 'Konkan'), (56, 'Korean'), (57, 'Lebanese'), (58, 'Lucknowi'), (59, 'Maharashtrian'), (60, 'Malaysian'), (61, 'Malwani'), (62, 'Mangalorean'), (63, 'Mediterranean'), (64, 'Mexican'), (65, 'Middle Eastern'), (66, 'Mithai'), (67, 'Modern Indian'), (68, 'Momos'), (69, 'Mongolian'), (70, 'Mughlai'), (71, 'Naga'), (72, 'Nepalese'), (73, 'North Eastern'), (74, 'North Indian'), (75, 'Oriya'), (76, 'Paan'), (77, 'Pan Asian'), (78, 'Parsi'), (79, 'Pizza'), (80, 'Portuguese'), (81, 'Rajasthani'), (82, 'Raw Meats'), (83, 'Roast Chicken'), (84, 'Rolls'), (85, 'Russian'), (86, 'Salad'), (87, 'Sandwich'), (88, 'Seafood'), (89, 'Sindhi'), (90, 'Singaporean'), (91, 'Soul Food'), (92, 'South American'), (93, 'South Indian'), (94, 'Spanish'), (95, 'Sri Lankan'), (96, 'Steak'), (97, 'Street Food'), (98, 'Sushi'), (99, 'Tamil'), (100, 'Tea'), (101, 'Tex-Mex'), (102, 'Thai'), (103, 'Tibetan'), (104, 'Turkish'), (105, 'Vegan'), (106, 'Vietnamese'), (107, 'Wraps')], max_length=319),
        ),
    ]
