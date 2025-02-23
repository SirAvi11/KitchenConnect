/* Populating users table */
INSERT INTO users (id, email, first_name, last_name, password, role, user_name) VALUES
(1, 'chef1@example.com', 'Vikas', 'Khanna', 'hashed_password1', 'chef', 'vikas_k'),
(2, 'chef2@example.com', 'Sanjeev', 'Kapoor', 'hashed_password2', 'chef', 'sanjeev_k'),
(3, 'foodlover1@example.com', 'Priya', 'Sharma', 'hashed_password3', 'food lover', 'priya_s'),
(4, 'foodlover2@example.com', 'Rohan', 'Verma', 'hashed_password4', 'food lover', 'rohan_v'),
(5, 'chef3@example.com', 'Ranveer', 'Brar', 'hashed_password5', 'chef', 'ranveer_b')
ON DUPLICATE KEY UPDATE 
email = VALUES(email), 
first_name = VALUES(first_name),
last_name = VALUES(last_name),
password = VALUES(password),
role = VALUES(role),
user_name = VALUES(user_name);


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

