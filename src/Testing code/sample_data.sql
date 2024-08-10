
--User
INSERT INTO User_info (User_ID, User_Name, User_Type, Verified, Password) VALUES (1, 'John Doe', 'Admin', 1, 'pass123');
INSERT INTO User_info (User_ID, User_Name, User_Type, Verified, Password) VALUES (2, 'Jane Smith', 'Customer', 0, 'password123');
INSERT INTO User_info (User_ID, User_Name, User_Type, Verified, Password) VALUES (3, 'Mike Johnson', 'Customer', 1, 'mikepass');
INSERT INTO User_info (User_ID, User_Name, User_Type, Verified, Password) VALUES (4, 'Emily Brown', 'Admin', 0, 'emily123');
INSERT INTO User_info (User_ID, User_Name, User_Type, Verified, Password) VALUES (5, 'David Wilson', 'Customer', 1, 'davidpass');

--Address
INSERT INTO Address_Book (Address_ID, User_Address, User_ID) VALUES (1, '123 Main Street', 2);
INSERT INTO Address_Book (Address_ID, User_Address, User_ID) VALUES  (2, '456 Elm Avenue', 3);
INSERT INTO Address_Book (Address_ID, User_Address, User_ID) VALUES (3, '789 Oak Lane', 5);

--Payment Method
INSERT INTO Payment_Method (PID, User_ID, Method, Details, Address_ID) VALUES (1, 2, 'Credit Card', '1234 5678 9012 3456', 1);
INSERT INTO Payment_Method (PID, User_ID, Method, Details, Address_ID) VALUES   (2, 3, 'PayPal', 'jane@example.com', 2);
INSERT INTO Payment_Method (PID, User_ID, Method, Details, Address_ID) VALUES  (3, 5, 'Bank Transfer', 'Account Number: 1234567890', 3);

--Category
INSERT INTO Category (Category_ID, Category_Name)VALUES (1, 'Fruit');

INSERT INTO Category (Category_ID, Category_Name)VALUES (2, 'Drink');

INSERT INTO Category (Category_ID, Category_Name)VALUES (3, 'Dairy Product');

INSERT INTO Category (Category_ID, Category_Name)VALUES (4, 'Seasoning');

INSERT INTO Category (Category_ID, Category_Name)VALUES (5, 'Meat');

--Product
-- Category: Fruit
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (1, 'Apple', 1.99, 1);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (2, 'Banana', 0.99, 1);
/*
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (3, 'Orange', 1.49, 1);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (4, 'Strawberry', 2.99, 1);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (5, 'Watermelon', 4.99, 1);
*/

-- Category: Drink
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (6, 'Cola', 1.49, 2);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (7, 'Lemonade', 1.29, 2);
/*
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (8, 'Iced Tea', 1.99, 2);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (9, 'Orange Juice', 2.49, 2);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (10, 'Bottled Water', 0.99, 2);
*/

-- Category: Dairy Product
/*
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (11, 'Milk', 2.99, 3);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (12, 'Yogurt', 1.49, 3);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (13, 'Cheese', 3.99, 3);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (14, 'Butter', 2.49, 3);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (15, 'Ice Cream', 4.99, 3);
*/

-- Category: Seasoning
/*
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (16, 'Salt', 1.29, 4);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (17, 'Pepper', 1.29, 4);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (18, 'Garlic Powder', 2.49, 4);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (19, 'Onion Powder', 2.49, 4);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (20, 'Italian Seasoning', 3.49, 4);
*/

-- Category: Meat

INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (21, 'Chicken Breast (500g)', 5.99, 5);

/*
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (22, 'Beef Steak', 9.99, 5);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (23, 'Pork Chops', 7.99, 5);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (24, 'Lamb Leg', 12.99, 5);
INSERT INTO Product (Product_ID, Product_Name, Price, Category_ID)VALUES (25, 'Salmon Fillet', 8.99, 5);
*/


--Warehouse
INSERT INTO Warehouse (Warehouse_ID, Address)VALUES (1, '11 Yuk Choi Road, Hung Hom');
INSERT INTO Warehouse (Warehouse_ID, Address)VALUES (2, 'Tat Chee Avenue, Kowloon Tong');
INSERT INTO Warehouse (Warehouse_ID, Address)VALUES (3, '224 Waterloo Road, Kowloon Tsa');

--product stock
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (1, 1, 20);
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (2, 1, 20);
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (6, 1, 20);
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (7, 1, 20);
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (21, 1, 20);
/*
-- For Product_ID: P1
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (1, 1, 20);
-- For Product_ID: P2
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (2, 1, 20);
-- For Product_ID: P3
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (3, 1, 20);
-- For Product_ID: P4
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (4, 1, 20);
-- For Product_ID: P5
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (5, 1, 20);
-- For Product_ID: P6
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (6, 1, 20);
-- For Product_ID: P7
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (7, 1, 20);
-- For Product_ID: P8
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (8, 1, 20);
-- For Product_ID: P9
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (9, 1, 20);
-- For Product_ID: 10
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (10, 1, 20);
-- For Product_ID: 11
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (11, 1, 20);
-- For Product_ID: 12
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (12, 1, 20);
-- For Product_ID: 13
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (13, 1, 20);
-- For Product_ID: 14
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (14, 1, 20);
-- For Product_ID: 15
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (15, 1, 20);
-- For Product_ID: 16
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (16, 1, 20);
-- For Product_ID: 17
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (17, 1, 20);
-- For Product_ID: 18
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (18, 1, 20);
-- For Product_ID: 19
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (19, 1, 20);
-- For Product_ID: 20
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (20, 1, 20);
-- For Product_ID: 21
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (21, 1, 20);
-- For Product_ID: 22
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (22, 1, 20);
-- For Product_ID: 23
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (23, 1, 20);
-- For Product_ID: 24
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (24, 1, 20);
-- For Product_ID: 25
INSERT INTO Product_Stock (Product_ID, Warehouse_ID, Amount)VALUES (25, 1, 20);

*/
--product comment
-- Comment 1
INSERT INTO Product_Comment (Product_ID, User_ID, Comment_Date, Product_Comment, Rating)
VALUES (1, 2, '2023-11-28', 'Fresh and yummy', 5);

-- Comment 2
INSERT INTO Product_Comment (Product_ID, User_ID, Comment_Date, Product_Comment, Rating)
VALUES (2, 2, '2023-11-28', 'Fresh and yummy.', 5);

-- Comment 3
INSERT INTO Product_Comment (Product_ID, User_ID, Comment_Date, Product_Comment, Rating)
VALUES (21, 3, '2023-11-28', 'It smell bad when I get it. I would not buy again!', 1);


--Order
--Payment ID create later, the concept is Order appear (PID is null) -> create a payment -> link payment to order -> wait the payment is paid
INSERT INTO Shop_Order (Order_Date, Order_Number, User_ID, Address_ID, Payment_ID) VALUES ('2023-11-27', 1, 2, 1, NULL);
INSERT INTO Shop_Order (Order_Date, Order_Number, User_ID, Address_ID, Payment_ID) VALUES ('2023-11-28', 2, 3, 2, NULL);

--Payment Record
INSERT INTO Payment_Record (Payment_ID, Payment_Date, Order_Number, PID, Payment_Status) VALUES (1, '2023-11-27', 1, 1, 'Paid');
INSERT INTO Payment_Record (Payment_ID, Payment_Date, Order_Number, PID, Payment_Status) VALUES (2, '2023-11-28', 2, 2, 'Waiting');

--Update the Order
UPDATE Shop_Order SET Payment_ID = 1 WHERE Order_Number = 1;
UPDATE Shop_Order SET Payment_ID = 2 WHERE Order_Number = 2;

--Order Details
INSERT INTO Order_Details (Order_Number, Product_ID, Amount, Price) VALUES (1, 1, 5, 10);
INSERT INTO Order_Details (Order_Number, Product_ID, Amount, Price) VALUES (1, 2, 3, 2.5);
INSERT INTO Order_Details (Order_Number, Product_ID, Amount, Price) VALUES (2, 6, 1, 5.99);


--Transportation Detail
INSERT INTO Transportation_Detail (Ship_Number, Last_Updated, Status)
VALUES (1, '2023-11-28', 'Finished');

--Transportation 
INSERT INTO Transporation (Order_Number, Ship_Number)VALUES (1, 1);
INSERT INTO Transporation (Order_Number, Ship_Number)VALUES (2, 1);