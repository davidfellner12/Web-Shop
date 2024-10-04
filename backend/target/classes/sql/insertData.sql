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

