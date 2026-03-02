INSERT INTO customers (name, email, phone, address, latitude, longitude) VALUES
('Rajesh Kumar', 'rajesh.kumar@email.com', '9876543210', '123 MG Road, Bangalore', 12.9716, 77.5946),
('Priya Sharma', 'priya.sharma@email.com', '9876543211', '456 JP Nagar, Bangalore', 12.9077, 77.5851),
('Amit Patel', 'amit.patel@email.com', '9876543212', '789 Koramangala, Bangalore', 12.9352, 77.6245),
('Sneha Gupta', 'sneha.gupta@email.com', '9876543213', '321 Whitefield, Bangalore', 12.9698, 77.7499),
('Vikram Reddy', 'vikram.reddy@email.com', '9876543214', '654 Indiranagar, Bangalore', 12.9784, 77.6408);

INSERT INTO sellers (name, email, phone, business_name) VALUES
('Ramesh Traders', 'ramesh.traders@email.com', '9876543220', 'Ramesh Wholesale'),
('Suresh Enterprises', 'suresh.ent@email.com', '9876543221', 'Suresh Distributors'),
('Mehta & Sons', 'mehta.sons@email.com', '9876543222', 'Mehta Trading Co.');

INSERT INTO warehouses (name, code, address, latitude, longitude, active) VALUES
('Bangalore Central Warehouse', 'BLR-CENTRAL', 'Peenya Industrial Area, Bangalore', 13.0289, 77.5199, true),
('Bangalore East Warehouse', 'BLR-EAST', 'Whitefield, Bangalore', 12.9698, 77.7499, true),
('Bangalore South Warehouse', 'BLR-SOUTH', 'Electronic City, Bangalore', 12.8458, 77.6608, true),
('Chennai Warehouse', 'CHN-MAIN', 'Ambattur Industrial Estate, Chennai', 13.0891, 80.1611, true),
('Hyderabad Warehouse', 'HYD-MAIN', 'Miyapur, Hyderabad', 17.4969, 78.3762, false);

INSERT INTO products (name, description, weight, length, width, height, seller_id) VALUES
('Rice Bag 25kg', 'Premium Basmati Rice', 25.0, 60.0, 40.0, 20.0, 1),
('Wheat Flour 10kg', 'Whole Wheat Atta', 10.0, 45.0, 30.0, 15.0, 1),
('Cooking Oil 5L', 'Sunflower Oil Can', 5.0, 25.0, 15.0, 30.0, 2),
('Sugar 5kg', 'Refined White Sugar', 5.0, 30.0, 20.0, 15.0, 2),
('Tea Powder 1kg', 'Premium Assam Tea', 1.0, 20.0, 15.0, 10.0, 3),
('Coffee Powder 500g', 'Filter Coffee Powder', 0.5, 15.0, 10.0, 8.0, 3),
('Spices Box', 'Mixed Spices Combo', 2.0, 25.0, 20.0, 12.0, 1),
('Pulses 5kg', 'Mixed Pulses Pack', 5.0, 35.0, 25.0, 15.0, 2),
('Detergent 2kg', 'Washing Powder', 2.0, 30.0, 20.0, 10.0, 3),
('Soap Pack', 'Bathing Soap Set', 1.0, 20.0, 15.0, 8.0, 1);
