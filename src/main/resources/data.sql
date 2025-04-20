-- 1. Users Table (11 users: 6 FOOD_LOVER, 4 CHEF, 1 ADMIN)
INSERT INTO users (id, address, email, first_login, first_name, last_name, password, phone_number, role, user_name) VALUES
-- ADMIN
(1, '123 MG Road, Bangalore', 'admin@foodapp.com', 0, 'Rahul', 'Sharma', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543210', 'ADMIN', 'rahul.admin'),

-- CHEFS (4)
(2, '456 Connaught Place, Delhi', 'chef.sanjeev@foodapp.com', 1, 'Sanjeev', 'Kapoor', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543211', 'CHEF', 'chef.sanjeev'),
(3, '789 Marine Drive, Mumbai', 'chef.vikas@foodapp.com', 1, 'Vikas', 'Khanna', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543212', 'CHEF', 'chef.vikas'),
(4, '321 Jubilee Hills, Hyderabad', 'chef.ranveer@foodapp.com', 0, 'Ranveer', 'Brar', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543213', 'CHEF', 'chef.ranveer'),
(5, '654 Park Street, Kolkata', 'chef.pankaj@foodapp.com', 1, 'Pankaj', 'Bhadouria', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543214', 'CHEF', 'chef.pankaj'),

-- FOOD LOVERS (6)
(6, '101 Brigade Road, Bangalore', 'foodie.priya@foodapp.com', 1, 'Priya', 'Patel', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543215', 'FOOD_LOVER', 'priya.foodie'),
(7, '202 Linking Road, Mumbai', 'arjun.food@foodapp.com', 1, 'Arjun', 'Banerjee', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543216', 'FOOD_LOVER', 'arjun.foodie'),
(8, '303 Banjara Hills, Hyderabad', 'neha.gourmet@foodapp.com', 0, 'Neha', 'Reddy', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543217', 'FOOD_LOVER', 'neha.gourmet'),
(9, '404 Camac Street, Kolkata', 'raj.foodlover@foodapp.com', 1, 'Raj', 'Choudhury', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543218', 'FOOD_LOVER', 'raj.foodlover'),
(10, '505 MG Road, Pune', 'meena.foodie@foodapp.com', 1, 'Meena', 'Joshi', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543219', 'FOOD_LOVER', 'meena.foodie'),
(11, '606 Commercial Street, Bangalore', 'vikram.gourmand@foodapp.com', 0, 'Vikram', 'Malhotra', '$2a$10$3ZpaizcwYk6PDJHvPQyxQu.veIy.ORYVoAmfngrZC6Bmun6kVqA.q', '919876543220', 'FOOD_LOVER', 'vikram.gourmand')
ON DUPLICATE KEY UPDATE 
  address = VALUES(address), 
  email = VALUES(email), 
  first_login = VALUES(first_login),
  first_name = VALUES(first_name),
  last_name = VALUES(last_name),
  password = VALUES(password),
  phone_number = VALUES(phone_number),
  role = VALUES(role),
  user_name = VALUES(user_name);

-- 2. Kitchens Table (5 kitchens, 4 approved)
INSERT INTO kitchens (kitchen_id, accept_terms, area, city, close_time, delivery_fees, floor, 
  fssai_document_path, fssai_expiry_date, fssai_number, kitchen_description, 
  kitchen_image_path, kitchen_name, max_delivery_time, menu_image_paths, 
  min_delivery_time, open_days, open_time, overall_rating, pan_document_path, 
  pan_number, phone_number, shop_name, status, total_ratings_count, user_id) VALUES
-- Approved kitchens (4)
(1, 1, 'Koramangala', 'Bangalore', '23:00', 49.00, 'Ground Floor', 
'/uploads/kitchenRequest/2/fssai_license.pdf', '2025-12-31', 'FSSAI11223344', 'Authentic North Indian cuisine with family recipes', 
'/uploads/kitchenRequest/2/punjab_rasoi.jpg', 'Punjab Rasoi', 45, '/uploads/kitchenRequest/2/menu_image.jpg,/uploads/kitchenRequest/2/menu_image2.jpg', 
25, 'Mon,Tue,Wed,Thu,Fri,Sat,Sun', '10:00', 4.7, '/uploads/kitchenRequest/2/pan_card.jpg', 
'ABCDE1234F', '08012345678', 'Punjab Rasoi', 'APPROVED', 150, 2),
(2, 1, 'Bandra West', 'Mumbai', '22:30', 39.00, 'First Floor', 
'/uploads/kitchenRequest/3/fssai_license.pdf', '2026-06-30', 'FSSAI55667788', 'Coastal Maharashtrian seafood specialties', 
'/uploads/kitchenRequest/3/konkan_tadka.jpg', 'Konkan Tadka', 50, '/uploads/kitchenRequest/3/menu_image.jpg,/uploads/kitchenRequest/3/menu_image2.jpg', 
30, 'Mon,Tue,Wed,Thu,Fri,Sat', '11:00', 4.5, '/uploads/kitchenRequest/3/pan_card.jpg', 
'BCDEF2345G', '02223456789', 'Konkan Tadka', 'APPROVED', 95, 3),
(3, 1, 'Jubilee Hills', 'Hyderabad', '22:00', 35.00, 'Second Floor', 
'/uploads/kitchenRequest/4/fssai_license.pdf', '2025-09-30', 'FSSAI99887766', 'Authentic Hyderabadi biryanis and kebabs', 
'/uploads/kitchenRequest/4/hyderabadi_zaika.jpg', 'Hyderabadi Zaika', 40, '/uploads/kitchenRequest/4/menu_image.jpg,/uploads/kitchenRequest/4/menu_image2.jpg', 
20, 'Mon,Tue,Wed,Thu,Fri,Sat,Sun', '11:30', 4.6, '/uploads/kitchenRequest/4/pan_card.jpg', 
'CDEFG3456H', '04023456789', 'Hyderabadi Zaika', 'APPROVED', 110, 4),
(4, 1, 'Park Street', 'Kolkata', '23:30', 45.00, 'Ground Floor', 
'/uploads/kitchenRequest/5/fssai_license.pdf', '2026-03-31', 'FSSAI33445566', 'Traditional Bengali cuisine with modern presentation', 
'/uploads/kitchenRequest/5/bengali_bhoj.jpg', 'Bengali Bhoj', 50, '/uploads/kitchenRequest/5/menu_image.jpg,/uploads/kitchenRequest/5/menu_image2.jpg', 
30, 'Mon,Tue,Wed,Thu,Fri,Sat,Sun', '10:30', 4.4, '/uploads/kitchenRequest/5/pan_card.jpg', 
'DEFGH4567I', '03323456789', 'Bengali Bhoj', 'APPROVED', 85, 5),
-- Under verification kitchen (1)
(5, 1, 'Koregaon Park', 'Pune', '22:00', 40.00, 'Third Floor', 
'/uploads/kitchenRequest/11/fssai_license.pdf', '2025-11-30', 'FSSAI77889900', 'Modern Indian fusion cuisine', 
'/uploads/kitchenRequest/11/fusion_tadka.jpg', 'Fusion Tadka', 45, '/uploads/kitchenRequest/11/menu_image.jpg,/uploads/kitchenRequest/11/menu_image2.jpg', 
25, 'Mon,Tue,Wed,Thu,Fri,Sat', '12:00', 0.0, '/uploads/kitchenRequest/11/pan_card.jpg', 
'EFGHI5678J', '02023456789', 'Fusion Tadka', 'UNDER_VERIFICATION', 0, 11)
ON DUPLICATE KEY UPDATE
  accept_terms = VALUES(accept_terms),
  area = VALUES(area),
  city = VALUES(city),
  close_time = VALUES(close_time),
  delivery_fees = VALUES(delivery_fees),
  floor = VALUES(floor),
  fssai_document_path = VALUES(fssai_document_path),
  fssai_expiry_date = VALUES(fssai_expiry_date),
  fssai_number = VALUES(fssai_number),
  kitchen_description = VALUES(kitchen_description),
  kitchen_image_path = VALUES(kitchen_image_path),
  kitchen_name = VALUES(kitchen_name),
  max_delivery_time = VALUES(max_delivery_time),
  menu_image_paths = VALUES(menu_image_paths),
  min_delivery_time = VALUES(min_delivery_time),
  open_days = VALUES(open_days),
  open_time = VALUES(open_time),
  overall_rating = VALUES(overall_rating),
  pan_document_path = VALUES(pan_document_path),
  pan_number = VALUES(pan_number),
  phone_number = VALUES(phone_number),
  shop_name = VALUES(shop_name),
  status = VALUES(status),
  total_ratings_count = VALUES(total_ratings_count),
  user_id = VALUES(user_id);

-- 3. Chefs Table (4 chefs - one for each approved kitchen)
INSERT INTO chefs (chef_id, biography, chef_profile_picture, favourite_dishes, kitchen_id, user_id) VALUES
(1, 'Master of Punjabi cuisine with 15 years experience in Delhi dhabas', '/images/chef1.jpg', 'Butter Chicken, Dal Makhani, Amritsari Kulcha', 1, 2),
(2, 'Expert in Konkani cuisine with focus on fresh seafood preparations', '/images/chef2.jpg', 'Malvani Fish Curry, Bombil Fry, Sol Kadhi', 2, 3),
(3, 'Hyderabadi cuisine specialist with royal kitchen experience', '/images/chef3.jpg', 'Hyderabadi Biryani, Haleem, Kebabs', 3, 4),
(4, 'Traditional Bengali chef with expertise in fish preparations', '/images/chef4.jpg', 'Macher Jhol, Shorshe Ilish, Chingri Malai Curry', 4, 5)
ON DUPLICATE KEY UPDATE
  biography = VALUES(biography),
  chef_profile_picture = VALUES(chef_profile_picture),
  favourite_dishes = VALUES(favourite_dishes),
  kitchen_id = VALUES(kitchen_id),
  user_id = VALUES(user_id);

-- 4. kitchen_cuisines;

INSERT INTO kitchen_cuisines (kitchen_id, cuisine)
SELECT 1, 'North Indian' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 1 AND cuisine = 'North Indian')
UNION ALL
SELECT 1, 'Mughlai' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 1 AND cuisine = 'Mughlai')
UNION ALL
SELECT 2, 'Maharashtrian' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 2 AND cuisine = 'Maharashtrian')
UNION ALL
SELECT 2, 'Coastal' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 2 AND cuisine = 'Coastal')
UNION ALL
SELECT 3, 'Hyderabadi' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 3 AND cuisine = 'Hyderabadi')
UNION ALL
SELECT 3, 'Mughlai' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 3 AND cuisine = 'Mughlai')
UNION ALL
SELECT 4, 'Bengali' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 4 AND cuisine = 'Bengali')
UNION ALL
SELECT 5, 'Fusion' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 5 AND cuisine = 'Fusion')
UNION ALL
SELECT 5, 'Continental' FROM DUAL WHERE NOT EXISTS 
    (SELECT 1 FROM kitchen_cuisines WHERE kitchen_id = 5 AND cuisine = 'Continental');


-- 5. Categories
INSERT INTO category (id, name, kitchen_id, position) VALUES
(1, 'Breads', 1, 1),
(2, 'Curries', 1, 2),
(3, 'Rice Dishes', 1, 3),
(4, 'Seafood Starters', 2, 1),
(5, 'Main Course', 2, 2),
(6, 'Biryani', 3, 1),
(7, 'Kebabs', 3, 2),
(8, 'Fish Dishes', 4, 1),
(9, 'Vegetarian Specials', 4, 2),
(10, 'Fusion Starters', 5, 1),
(11, 'Signature Mains', 5, 2)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  kitchen_id = VALUES(kitchen_id),
  position = VALUES(position);

-- 6. Menu Items
INSERT INTO menu_item (id, description, image_url, is_veg, name, price, category_id, overall_rating, rating_count) VALUES
-- Punjab Rasoi items
(1, 'Traditional tandoor baked bread', '/uploads/menuImages/2/1/bread.jpg', 1, 'Butter Naan', 45.00, 1, 4.5, 120),
(2, 'Creamy tomato based chicken curry', '/uploads/menuImages/2/2/butter_chicken.jpg', 0, 'Butter Chicken', 320.00, 2, 4.8, 150),
(3, 'Aromatic basmati rice with spices', '/uploads/menuImages/2/3/biryani.jpg', 0, 'Chicken Biryani', 280.00, 3, 4.7, 180),

-- Konkan Tadka items
(4, 'Crispy fried bombay duck', '/uploads/menuImages/3/4/bombil.jpg', 0, 'Bombil Fry', 220.00, 4, 4.6, 90),
(5, 'Spicy coconut based fish curry', '/uploads/menuImages/3/5/malvani.jpg', 0, 'Malvani Fish Curry', 260.00, 5, 4.4, 75),

-- Hyderabadi Zaika items
(6, 'Famous Hyderabadi dum biryani', '/uploads/menuImages/4/6/biryani.jpg', 0, 'Hyderabadi Biryani', 350.00, 6, 4.9, 200),
(7, 'Tender chicken kebabs', '/uploads/menuImages/4/7/kebab.jpg', 0, 'Chicken Kebabs', 280.00, 7, 4.5, 150),

-- Bengali Bhoj items
(8, 'Hilsa fish in mustard sauce', '/uploads/menuImages/5/8/hilsa.jpg', 0, 'Shorshe Ilish', 400.00, 8, 4.7, 120),
(9, 'Bengali mixed vegetables', '/uploads/menuImages/5/9/mixed_veg.jpg', 1, 'Chorchori', 180.00, 9, 4.2, 80),

-- Fusion Tadka items
(10, 'Indian style bruschetta', '/uploads/menuImages/11/10/bruschetta.jpg', 1, 'Desi Bruschetta', 220.00, 10, 4.0, 40),
(11, 'Butter chicken pizza', '/uploads/menuImages/11/11/chicken_pizza.jfif', 0, 'Butter Chicken Pizza', 320.00, 11, 4.1, 50)
ON DUPLICATE KEY UPDATE
  description = VALUES(description),
  image_url = VALUES(image_url),
  is_veg = VALUES(is_veg),
  name = VALUES(name),
  price = VALUES(price),
  category_id = VALUES(category_id),
  overall_rating = VALUES(overall_rating),
  rating_count = VALUES(rating_count);

-- 7. Orders
INSERT INTO orders (id, order_date, rating, status, total_amount, kitchen_id, user_id,delivery_address,contact_number) VALUES
(1, '2023-06-15 19:30:45', 4.5, 'DELIVERED', 765.00, 1, 6,"456 Connaught Place, Delhi","8177953291"),
(2, '2023-06-16 20:15:30', 4.0, 'DELIVERED', 480.00, 2, 7,"456 Connaught Place, Delhi","8177953291"),
(3, '2023-06-17 13:45:00', 5.0, 'DELIVERED', 730.00, 3, 8,"456 Connaught Place, Delhi","8177953291"),
(4, '2023-06-18 21:00:15', 4.5, 'DELIVERED', 580.00, 4, 9,"456 Connaught Place, Delhi","8177953291"),
(5, '2023-06-19 20:30:00', 4.0, 'DELIVERED', 540.00, 1, 10,"456 Connaught Place, Delhi","8177953291")
ON DUPLICATE KEY UPDATE
  order_date = VALUES(order_date),
  rating = VALUES(rating),
  status = VALUES(status),
  total_amount = VALUES(total_amount),
  kitchen_id = VALUES(kitchen_id),
  user_id = VALUES(user_id);

-- 8. Order Details
INSERT INTO order_details (id, price, quantity, menu_item_id, order_id) VALUES
(1, 45.00, 2, 1, 1),
(2, 320.00, 1, 2, 1),
(3, 280.00, 1, 3, 1),
(4, 220.00, 2, 4, 2),
(5, 260.00, 1, 5, 2),
(6, 350.00, 1, 6, 3),
(7, 280.00, 1, 7, 3),
(8, 400.00, 1, 8, 4),
(9, 180.00, 1, 9, 4),
(10, 45.00, 2, 1, 5),
(11, 320.00, 1, 2, 5)
ON DUPLICATE KEY UPDATE
  price = VALUES(price),
  quantity = VALUES(quantity),
  menu_item_id = VALUES(menu_item_id),
  order_id = VALUES(order_id);

-- 9. Ratings
INSERT INTO rating (id, kitchen_rating, order_id, user_note) VALUES
(1, 5, 1, 'Excellent food quality and packaging'),
(2, 4, 2, 'Good taste but delivery was delayed'),
(3, 5, 3, 'Best biryani I have ever had!'),
(4, 5, 4, 'Authentic Bengali flavors'),
(5, 4, 5, 'Consistently good quality')
ON DUPLICATE KEY UPDATE
  kitchen_rating = VALUES(kitchen_rating),
  order_id = VALUES(order_id),
  user_note = VALUES(user_note);

-- 10. Item Ratings
INSERT INTO item_rating (id, item_name, rating_value, rating_id) VALUES
(1, 'Butter Chicken', 5, 1),
(2, 'Butter Naan', 4, 1),
(3, 'Bombil Fry', 4, 2),
(4, 'Hyderabadi Biryani', 5, 3),
(5, 'Shorshe Ilish', 5, 4),
(6, 'Butter Chicken', 4, 5)
ON DUPLICATE KEY UPDATE
  item_name = VALUES(item_name),
  rating_value = VALUES(rating_value),
  rating_id = VALUES(rating_id);