-- Create Database if not exists
CREATE DATABASE IF NOT EXISTS HostelManagementSystem;
USE HostelManagementSystem;

-- Drop tables if they already exist to avoid errors during re-run
DROP TABLE IF EXISTS AuditLog, Salary, MessStatus, StaffLeave, StudentLeave, Attendance,
    OtherStaff, MessHead, Caretaker, Manager, Employee, Inventory, Invoice, Visitor,
    Student, Room, Hostel, User;

-- Base User Table
CREATE TABLE IF NOT EXISTS User (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(255) NOT NULL,
    MiddleName VARCHAR(255),
    LastName VARCHAR(255) NOT NULL,
    CNIC VARCHAR(15) UNIQUE NOT NULL,
    Gender ENUM('Male', 'Female', 'Other') NOT NULL DEFAULT 'Other',
    Phone VARCHAR(15),
    Email VARCHAR(100),
    Address TEXT,
    Password VARCHAR(255),
    ProfilePic LONGBLOB
) AUTO_INCREMENT = 1000000;

-- Hostel Table
CREATE TABLE IF NOT EXISTS Hostel (
    HostelID INT PRIMARY KEY AUTO_INCREMENT,
    HostelName VARCHAR(255),
    TotalRooms INT,
    RoomType VARCHAR(50)
) AUTO_INCREMENT = 1000000;

-- Room Table
CREATE TABLE IF NOT EXISTS Room (
    RoomID INT PRIMARY KEY AUTO_INCREMENT,
    HostelID INT,
    RoomNumber VARCHAR(20),
    Capacity INT,
    IsFull BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (HostelID) REFERENCES Hostel(HostelID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 100;

-- Student Table
CREATE TABLE IF NOT EXISTS Student (
    StudentID INT PRIMARY KEY AUTO_INCREMENT,
    UserID INT NOT NULL,
    RoomID INT,
    HostelID INT,
    Department VARCHAR(100),
    AllotmentDate DATE,
    GuardianName VARCHAR(255),
    GuardianPhone VARCHAR(15),
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (RoomID) REFERENCES Room(RoomID) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (HostelID) REFERENCES Hostel(HostelID) ON DELETE SET NULL ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Visitor Table
CREATE TABLE IF NOT EXISTS Visitor (
    VisitorID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(255),
    MiddleName VARCHAR(255),
    LastName VARCHAR(255),
    CNIC VARCHAR(15),
    Gender ENUM('Male', 'Female', 'Other') DEFAULT 'Other',
    Email VARCHAR(100),
    Phone VARCHAR(15),
    StudentID INT,
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID) ON DELETE SET NULL ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Invoice Table
CREATE TABLE IF NOT EXISTS Invoice (
    InvoiceID INT PRIMARY KEY AUTO_INCREMENT,
    StudentID INT,
    InvoiceType VARCHAR(100),
    DueDate DATE,
    Amount DECIMAL(10,2),
    Status ENUM('Paid', 'Unpaid', 'Pending') DEFAULT 'Pending',
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Inventory Table
CREATE TABLE IF NOT EXISTS Inventory (
    InventoryID INT PRIMARY KEY AUTO_INCREMENT,
    ItemName VARCHAR(100),
    Category VARCHAR(50),
    Quantity INT,
    Description TEXT,
    LastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) AUTO_INCREMENT = 1000000;

-- Employee Table
CREATE TABLE IF NOT EXISTS Employee (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(255),
    MiddleName VARCHAR(255),
    LastName VARCHAR(255),
    CNIC VARCHAR(15) UNIQUE,
    Gender ENUM('Male', 'Female', 'Other') DEFAULT 'Other',
    Phone VARCHAR(15),
    Email VARCHAR(100),
    ProfilePic LONGBLOB,
    Address TEXT,
    EmploymentDate DATE,
    JoiningDate DATE,
    IsActive BOOLEAN DEFAULT TRUE
) AUTO_INCREMENT = 1000000;

-- Manager Table
CREATE TABLE IF NOT EXISTS Manager (
    ManagerID INT PRIMARY KEY AUTO_INCREMENT,
    UserID INT,
    EmployeeID INT,
    HostelID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (HostelID) REFERENCES Hostel(HostelID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Caretaker Table
CREATE TABLE IF NOT EXISTS Caretaker (
    CaretakerID INT PRIMARY KEY AUTO_INCREMENT,
    UserID INT,
    EmployeeID INT,
    HostelID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (HostelID) REFERENCES Hostel(HostelID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Mess Head Table
CREATE TABLE IF NOT EXISTS MessHead (
    HeadID INT PRIMARY KEY AUTO_INCREMENT,
    EmployeeID INT,
    HostelID INT,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (HostelID) REFERENCES Hostel(HostelID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Other Staff Table
CREATE TABLE IF NOT EXISTS OtherStaff (
    StaffID INT PRIMARY KEY AUTO_INCREMENT,
    EmployeeID INT,
    HostelID INT,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (HostelID) REFERENCES Hostel(HostelID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Attendance Table
CREATE TABLE IF NOT EXISTS Attendance (
    AttendanceID INT PRIMARY KEY AUTO_INCREMENT,
    StudentID INT,
    AttendanceDate DATE,
    Status ENUM('Present', 'Absent', 'Leave') DEFAULT 'Present',
    RecordedBy INT,
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Student Leave Table
CREATE TABLE IF NOT EXISTS StudentLeave (
    LeaveID INT PRIMARY KEY AUTO_INCREMENT,
    StudentID INT,
    ApplyDate DATE,
    LeaveDate DATE,
    ReturnDate DATE,
    Destination TEXT,
    Reason TEXT,
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Staff Leave Table
CREATE TABLE IF NOT EXISTS StaffLeave (
    LeaveID INT PRIMARY KEY AUTO_INCREMENT,
    EmployeeID INT,
    ApplyDate DATE,
    LeaveDate DATE,
    ReturnDate DATE,
    Reason TEXT,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- MessStatus Table
CREATE TABLE IF NOT EXISTS MessStatus (
    StatusID INT PRIMARY KEY AUTO_INCREMENT,
    StudentID INT,
    LeaveDate DATE,
    JoinDate DATE,
    Reason TEXT,
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Salary Table
CREATE TABLE IF NOT EXISTS Salary (
    SalaryID INT PRIMARY KEY AUTO_INCREMENT,
    EmployeeID INT,
    Amount DECIMAL(10,2),
    Bonus DECIMAL(10,2) DEFAULT 0.00,
    Deductions DECIMAL(10,2) DEFAULT 0.00,
    NetPay DECIMAL(10,2) GENERATED ALWAYS AS (Amount + Bonus - Deductions) STORED,
    PayDate DATE,
    PaymentMode ENUM('Cash', 'Bank Transfer', 'Cheque') DEFAULT 'Bank Transfer',
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE
) AUTO_INCREMENT = 1000000;

-- Audit Log Table
CREATE TABLE IF NOT EXISTS AuditLog (
    AuditID INT PRIMARY KEY AUTO_INCREMENT,
    TableName VARCHAR(100) NOT NULL,
    ActionType ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    ActionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    OldValue TEXT,
    NewValue TEXT,
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
) AUTO_INCREMENT = 1000000;

-- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE ValidateUser(IN p_email VARCHAR(100), IN p_password VARCHAR(255))
BEGIN
    SELECT UserID, FirstName, LastName
    FROM User
    WHERE Email = p_email AND Password = p_password;
END$$

DELIMITER ;

-- --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO Employee (FirstName, LastName, CNIC, Gender, Phone, Email, Address, EmploymentDate, JoiningDate)
VALUES ('Alice', 'Manager', '12345-1234567-1', 'Female', '03001234567', 'alice.manager@hostel.com', 'Manager House', CURDATE(), CURDATE());
SET @empManager = LAST_INSERT_ID();

-- Insert Caretaker 1 Employee
INSERT INTO Employee (FirstName, LastName, CNIC, Gender, Phone, Email, Address, EmploymentDate, JoiningDate)
VALUES ('Bob', 'Caretaker', '23456-2345678-2', 'Male', '03111234567', 'bob.caretaker@hostel.com', 'Caretaker House A', CURDATE(), CURDATE());
SET @empCaretaker1 = LAST_INSERT_ID();

-- Insert Caretaker 2 Employee
INSERT INTO Employee (FirstName, LastName, CNIC, Gender, Phone, Email, Address, EmploymentDate, JoiningDate)
VALUES ('Carol', 'Caretaker', '34567-3456789-3', 'Female', '03221234567', 'carol.caretaker@hostel.com', 'Caretaker House B', CURDATE(), CURDATE());
SET @empCaretaker2 = LAST_INSERT_ID();

-- Insert into Manager table
INSERT INTO Manager (UserID, EmployeeID, HostelID)
VALUES (@userManager, @empManager, @hostelId);

-- Insert into Caretaker table
INSERT INTO Caretaker (UserID, EmployeeID, HostelID)
VALUES 
(@userCaretaker1, @empCaretaker1, @hostelId),
(@userCaretaker2, @empCaretaker2, @hostelId);