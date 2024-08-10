DROP TABLE Order_Details;
DROP TABLE Product_Stock;
DROP TABLE Warehouse;
DROP TABLE Product_Comment;
DROP TABLE Product;
DROP TABLE Category;
Drop TABLE Transporation;
DROP TABLE Transportation_Detail;
ALTER TABLE Shop_Order
DROP CONSTRAINT fk_SO_Payment_ID;
DROP TABLE Payment_Record;
DROP TABLE Shop_Order;
DROP TABLE Payment_Method;
DROP TABLE Address_Book;
DROP TABLE User_info;



CREATE TABLE User_info (
    User_ID NUMBER NOT NULL PRIMARY KEY,
    User_Name VARCHAR(30) NOT NULL UNIQUE,
    User_Type VARCHAR(30) NOT NULL,
    Verified NUMBER NOT NULL,
    Password VARCHAR(100) NOT NULL
);

CREATE TABLE Address_Book(
    Address_ID NUMBER NOT NULL PRIMARY KEY,
    User_Address VARCHAR(255) NOT NULL,
    User_ID NUMBER NOT NULL,
    CONSTRAINT fk_AB_User_ID FOREIGN KEY (User_ID) REFERENCES User_info(User_ID) 
);

CREATE TABLE Payment_Method
(
    PID NUMBER NOT NULL PRIMARY KEY,
    User_ID NUMBER NOT NULL,
    Method  VARCHAR(255) NOT NULL,
    Details VARCHAR(255) NOT NULL,
    Address_ID NUMBER NOT NULL,
    CONSTRAINT fk_PM_Address_ID FOREIGN KEY (Address_ID) REFERENCES Address_Book(Address_ID) ,
    CONSTRAINT fk_PM_User_ID FOREIGN KEY (User_ID) REFERENCES User_info(User_ID)
);
CREATE TABLE Shop_Order
(
    Order_Date date NOT NULL,
    Order_Number NUMBER NOT NULL PRIMARY KEY,
    User_ID NUMBER NOT NULL,
    Address_ID NUMBER NOT NULL,
    Payment_ID NUMBER,
    CONSTRAINT fk_SO_User_ID FOREIGN KEY (User_ID) REFERENCES User_info(User_ID),
    CONSTRAINT fk_SO_Address_ID FOREIGN KEY (Address_ID) REFERENCES Address_Book(Address_ID)
);
ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD';

CREATE TABLE Payment_Record(
    Payment_ID NUMBER NOT NULL PRIMARY KEY,
    Payment_Date date NOT NULL,
    Order_Number NUMBER NOT NULL,
    PID NUMBER NOT NULL,
    Payment_Status VARCHAR(10) NOT NULL,
    CONSTRAINT fk_PR_Order_Number FOREIGN KEY (Order_Number) REFERENCES Shop_Order(Order_Number),
    CONSTRAINT fk_PR_PID FOREIGN KEY (PID) REFERENCES Payment_Method(PID)
);
ALTER TABLE Shop_Order
ADD CONSTRAINT fk_SO_Payment_ID FOREIGN KEY (Payment_ID) REFERENCES Payment_Record(Payment_ID); 

CREATE TABLE Transportation_Detail
(
    Ship_Number NUMBER NOT NULL PRIMARY KEY,
    Last_Updated date NOT NULL,
    Status VARCHAR(15) NOT NULL
);

CREATE TABLE Transporation(
    Order_Number NUMBER NOT NULL  ,
    Ship_Number NUMBER NOT NULL  ,
    CONSTRAINT PK_T PRIMARY KEY (Order_Number, Ship_Number)
);
CREATE TABLE Category(
    Category_ID NUMBER NOT NULL PRIMARY KEY,
    Category_Name VARCHAR(30) NOT NULL
);

CREATE TABLE Product(
    Product_ID NUMBER NOT NULL PRIMARY KEY,
    Product_Name VARCHAR(50) NOT NULL,
    Product_Descripion VARCHAR(100) DEFAULT ' ', 
    Price NUMBER(10,2) NOT NULL,
    Category_ID NUMBER,
    CONSTRAINT fk_P_Category_ID FOREIGN KEY (Category_ID) REFERENCES Category(Category_ID)
);
CREATE TABLE Product_Comment(
    Product_ID NUMBER NOT NULL,
    User_ID NUMBER NOT NULL,
    Comment_Date date NOT NULL,
    Product_Comment LONG NOT NULL,
    Rating NUMBER NOT NULL,
    CONSTRAINT PK_PC PRIMARY KEY (Product_ID, User_ID, Comment_Date),
    CONSTRAINT fk_PC_Product_ID FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID),
    CONSTRAINT fk_PC_User_ID FOREIGN KEY (User_ID) REFERENCES User_info(User_ID)
);

CREATE TABLE Warehouse(
    Warehouse_ID NUMBER NOT NULL PRIMARY KEY,
    Address LONG NOT NULL
);
CREATE TABLE Product_Stock(
    Product_ID NUMBER NOT NULL ,
    Warehouse_ID NUMBER NOT NULL,
    Amount NUMBER,
    CONSTRAINT PK_PS PRIMARY KEY (Product_ID, Warehouse_ID),
    CONSTRAINT fk_PS_Product_ID FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID),
    CONSTRAINT fk_PS_Warehouse_ID FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(Warehouse_ID)
);

CREATE TABLE Order_Details(
    Order_Number NUMBER NOT NULL,
    Product_ID NUMBER NOT NULL,
    Amount NUMBER(10,0) NOT NULL,
    Price NUMBER(10,2) NOT NULL,
    CONSTRAINT PK_OD PRIMARY KEY (Order_Number, Product_ID),
    CONSTRAINT fk_OD_Order_Number FOREIGN KEY (Order_Number) REFERENCES Shop_Order(Order_Number),
    CONSTRAINT fk_OD_Product_ID FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID)
);
