/* Populating users table */
INSERT INTO users (id, email, first_name, last_name, password, role, user_name, address, phone_number) VALUES
(1, 'chef1@example.com', 'Vikas', 'Khanna', 'hashed_password1', 'CHEF', 'vikas_k', 'Abc Street', '1234567892' ),
(2, 'chef2@example.com', 'Sanjeev', 'Kapoor', 'hashed_password2', 'CHEF', 'sanjeev_k', 'Abc Street', '1234567892'),
(3, 'foodlover1@example.com', 'Priya', 'Sharma', 'hashed_password3', 'FOOD_LOVER', 'priya_s', 'Abc Street', '1234567892'),
(4, 'foodlover2@example.com', 'Rohan', 'Verma', 'hashed_password4', 'FOOD_LOVER', 'rohan_v', 'Abc Street', '1234567892'),
(5, 'chef3@example.com', 'Ranveer', 'Brar', 'hashed_password5', 'CHEF', 'ranveer_b', 'Abc Street', '1234567892')
ON DUPLICATE KEY UPDATE 
email = VALUES(email), 
first_name = VALUES(first_name),
last_name = VALUES(last_name),
password = VALUES(password),
role = VALUES(role),
user_name = VALUES(user_name),
address = VALUES(address),
phone_number = VALUES(phone_number);


/* Populating chef table */
INSERT INTO chefs (chef_id, biography, chef_profile_picture, favourite_dishes, user_id) VALUES
(1, 'I am Vikas Khanna, an award-winning Indian chef and restaurateur. 
My journey began in the streets of Amritsar, learning the art of authentic Punjabi cuisine from my grandmother. 
Over the years, I have traveled across India, perfecting the delicate balance of flavors in traditional dishes. 
I believe that food is deeply connected to culture, and I take pride in bringing rich Indian heritage to global dining tables. 
My signature dishes, like Dal Makhani and Butter Chicken, reflect my deep love for Indian spices and flavors.', 
 'vikas.jpg', 'Dal Makhani, Butter Chicken', 
    (SELECT id FROM users WHERE email = 'chef1@example.com')),

(2, 'I am Sanjeev Kapoor, a chef, entrepreneur, and television host who has transformed Indian home cooking. 
My passion lies in making Indian cuisine accessible and simple for everyone. 
With a focus on authentic flavors, I believe in giving a modern twist to traditional recipes while keeping their essence intact. 
From rich Mughlai curries to healthy millet-based dishes, my cooking philosophy revolves around taste, nutrition, and creativity.', 
 'sanjeev.jpg', 'Paneer Butter Masala, Pulao', 
    (SELECT id FROM users WHERE email = 'chef2@example.com')),

(3, 'I am Ranveer Brar, a chef, storyteller, and food explorer. 
Born in Lucknow, I grew up surrounded by the aromas of kebabs and biryanis, which ignited my love for cooking. 
For me, Indian food is an experience—it’s about history, memories, and emotions served on a plate. 
From the street food of Delhi to the royal kitchens of Rajasthan, I find inspiration in every corner of India. 
Biryani, Kebabs, and fusion dishes are my specialties, blending old-world charm with contemporary flavors.', 
 'ranveer.jpg', 'Lucknowi Biryani, Galouti Kebab', 
    (SELECT id FROM users WHERE email = 'chef3@example.com'))
ON DUPLICATE KEY UPDATE 
biography = VALUES(biography),
chef_profile_picture = VALUES(chef_profile_picture),
favourite_dishes = VALUES(favourite_dishes);


/* Populating Kitchen table */
INSERT INTO kitchens (kitchen_id, available_days, delivery_fees, kitchen_description, kitchen_image, 
                     kitchen_name, max_delivery_time, min_delivery_time, overall_rating, speciality, 
                     total_ratings_count, chef_id) 
VALUES
(1, 'Monday - Sunday', 50, 'A premium North Indian kitchen specializing in slow-cooked curries and tandoori delights. 
We use fresh, locally sourced spices to bring out the authentic flavors of Punjab. 
Every meal reflects the tradition of slow-cooking, ensuring rich, aromatic dishes with deep flavors.', 
 'vikas_kitchen.jpg', 'Khanna’s Indian Feast', 40, 20, 4.8, 'North Indian Cuisine', 500, 
    (SELECT chef_id FROM chefs WHERE user_id = (SELECT id FROM users WHERE email = 'chef1@example.com'))),

(2, 'Tuesday - Saturday', 30, 'A home-style kitchen bringing you the flavors of everyday Indian cooking. 
From comforting dal to indulgent biryanis, we serve wholesome meals prepared with love and tradition. 
Our menu includes a mix of North and South Indian favorites, ensuring a taste of India in every bite.', 
 'sanjeev_kitchen.jpg', 'Kapoor’s Home Kitchen', 35, 15, 4.6, 'Indian Comfort Food', 650, 
    (SELECT chef_id FROM chefs WHERE user_id = (SELECT id FROM users WHERE email = 'chef2@example.com'))),

(3, 'Wednesday - Sunday', 75, 'A fusion kitchen inspired by the diverse regional cuisines of India. 
From the streets of Mumbai to the royal kitchens of Hyderabad, our dishes are crafted with heritage recipes and modern twists. 
Expect bold flavors, hand-ground spices, and a taste of India’s culinary history.', 
 'ranveer_kitchen.jpg', 'Brar’s Heritage Bites', 45, 25, 4.9, 'Fusion Indian Cuisine', 700, 
    (SELECT chef_id FROM chefs WHERE user_id = (SELECT id FROM users WHERE email = 'chef3@example.com')))
ON DUPLICATE KEY UPDATE 
available_days = VALUES(available_days),
delivery_fees = VALUES(delivery_fees),
kitchen_description = VALUES(kitchen_description),
kitchen_image = VALUES(kitchen_image),
kitchen_name = VALUES(kitchen_name),
max_delivery_time = VALUES(max_delivery_time),
min_delivery_time = VALUES(min_delivery_time),
overall_rating = VALUES(overall_rating),
speciality = VALUES(speciality),
total_ratings_count = VALUES(total_ratings_count);

/*Populating Food Items table*/


INSERT INTO fooditem (id, kitchen_id, name, picture, price, overall_rating, rating_count, availability, description)
VALUES
-- Kitchen 1: Khanna’s Indian Feast (North Indian Cuisine)
(1, 1, 'Butter Chicken', 'butter_chicken.jpg', 320.00, 4.6, 250, TRUE, 'Creamy tomato-based gravy with tender chicken pieces.'),
(2, 1, 'Dal Makhani', 'dal_makhani.jpg', 210.00, 4.7, 180, TRUE, 'Slow-cooked black lentils with butter and cream.'),
(3, 1, 'Tandoori Roti', 'tandoori_roti.jpg', 40.00, 4.5, 150, TRUE, 'Soft, whole wheat roti baked in a clay tandoor.'),

-- Kitchen 2: Kapoor’s Home Kitchen (Indian Comfort Food)
(4, 2, 'Vegetable Pulao', 'vegetable_pulao.jpg', 180.00, 4.9, 140, TRUE, 'Basmati rice cooked with aromatic spices and fresh vegetables.'),
(5, 2, 'Palak Paneer', 'palak_paneer.jpg', 250.00, 3.7, 200, TRUE, 'Creamy spinach curry with soft paneer cubes.'),
(6, 2, 'Aloo Poori', 'aloo_poori.jpg', 150.00, 4.5, 120, TRUE, 'Fluffy deep-fried poori served with spiced potato curry.'),

-- Kitchen 3: Brar’s Heritage Bites (Fusion Indian Cuisine)
(7, 3, 'Mutton Rogan Josh', 'mutton_rogan_josh.jpg', 400.00, 4.9, 300, TRUE, 'Kashmiri-style slow-cooked lamb in a rich red gravy.'),
(8, 3, 'Prawn Malai Curry', 'prawn_malai_curry.jpg', 380.00, 4.8, 220, TRUE, 'Bengali delicacy with prawns in a coconut milk curry.'),
(9, 3, 'Chicken Tikka', 'chicken_tikka.jpg', 280.00, 4.7, 190, TRUE, 'Marinated and grilled chicken served with mint chutney.')
ON DUPLICATE KEY UPDATE 
name = VALUES(name),
picture = VALUES(picture),
price = VALUES(price),
overall_rating = VALUES(overall_rating),
rating_count = VALUES(rating_count),
availability = VALUES(availability),
description = VALUES(description);

