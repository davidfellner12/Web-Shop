-- insert initial test data
-- the IDs are hardcoded to enable references between further test data
-- negative IDs are used to not interfere with user-entered data and allow clean deletion of test data

MERGE INTO customers (id, first_name, last_name, date_of_birth, email)
    VALUES (-1, 'Max', 'Mustermann', '1999-02-05', 'max.mustermann@example.com'),
    (-2, 'Anna', 'Schmidt', '1985-11-23', 'anna.schmidt@example.com'),
    (-3, 'John', 'Doe', '1990-07-14', 'john.doe@example.com'),
    (-4, 'Jane', 'Smith', '1988-09-30', 'jane.smith@example.com'),
    (-5, 'Emily', 'Jones', '1993-04-17', 'emily.jones@example.com'),
    (-6, 'Michael', 'Brown', '1978-12-01', 'michael.brown@example.com'),
    (-7, 'Sarah', 'Davis', '1995-06-22', 'sarah.davis@example.com'),
    (-8, 'David', 'Wilson', '1982-03-15', 'david.wilson@example.com'),
    (-9, 'Laura', 'Taylor', '1991-10-04', 'laura.taylor@example.com'),
    (-10, 'James', 'Anderson', '1987-08-30', 'james.anderson@example.com');

MERGE INTO articles (id, designation, description, price, image, image_type) VALUES
    (-1, 'Laptop', '15-inch laptop with 16GB RAM and 512GB SSD', 99999, NULL, 'jpg'),
    (-2, 'Smartphone', 'Latest model smartphone with 128GB storage', 79999, NULL, 'png'),
    (-3, 'Wireless Headphones', 'Noise-canceling over-ear headphones', 19999, NULL, 'jpg'),
    (-4, 'Smartwatch', 'Fitness smartwatch with heart rate monitor', 24999, NULL, 'png'),
    (-5, 'Bluetooth Speaker', 'Portable Bluetooth speaker with rich sound', 14999, NULL, 'jpg'),
    (-6, '4K TV', '55-inch 4K Ultra HD Smart TV', 69999, NULL, 'jpg'),
    (-7, 'Gaming Console', 'Next-gen gaming console with exclusive games', 49999, NULL, 'png'),
    (-8, 'Tablet', '10-inch tablet with stylus support', 39999, NULL, 'jpg'),
    (-9, 'Digital Camera', 'DSLR camera with 24MP resolution', 89999, NULL, 'jpg'),
    (-10, 'External Hard Drive', '1TB external hard drive for backups', 8999, NULL, 'jpg');
