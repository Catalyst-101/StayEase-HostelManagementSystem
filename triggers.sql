-- INSERT Trigger for User
DELIMITER $$
CREATE TRIGGER trg_User_Insert
AFTER INSERT ON User
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('User', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for User
DELIMITER $$
CREATE TRIGGER trg_User_Update
AFTER UPDATE ON User
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('User', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for User
DELIMITER $$
CREATE TRIGGER trg_User_Delete
AFTER DELETE ON User
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('User', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Hostel
DELIMITER $$
CREATE TRIGGER trg_Hostel_Insert
AFTER INSERT ON Hostel
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Hostel', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Hostel
DELIMITER $$
CREATE TRIGGER trg_Hostel_Update
AFTER UPDATE ON Hostel
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Hostel', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Hostel
DELIMITER $$
CREATE TRIGGER trg_Hostel_Delete
AFTER DELETE ON Hostel
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Hostel', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Room
DELIMITER $$
CREATE TRIGGER trg_Room_Insert
AFTER INSERT ON Room
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Room', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Room
DELIMITER $$
CREATE TRIGGER trg_Room_Update
AFTER UPDATE ON Room
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Room', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Room
DELIMITER $$
CREATE TRIGGER trg_Room_Delete
AFTER DELETE ON Room
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Room', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Student
DELIMITER $$
CREATE TRIGGER trg_Student_Insert
AFTER INSERT ON Student
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Student', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Student
DELIMITER $$
CREATE TRIGGER trg_Student_Update
AFTER UPDATE ON Student
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Student', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Student
DELIMITER $$
CREATE TRIGGER trg_Student_Delete
AFTER DELETE ON Student
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Student', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Visitor
DELIMITER $$
CREATE TRIGGER trg_Visitor_Insert
AFTER INSERT ON Visitor
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Visitor', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Visitor
DELIMITER $$
CREATE TRIGGER trg_Visitor_Update
AFTER UPDATE ON Visitor
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Visitor', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Visitor
DELIMITER $$
CREATE TRIGGER trg_Visitor_Delete
AFTER DELETE ON Visitor
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Visitor', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Invoice
DELIMITER $$
CREATE TRIGGER trg_Invoice_Insert
AFTER INSERT ON Invoice
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Invoice', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Invoice
DELIMITER $$
CREATE TRIGGER trg_Invoice_Update
AFTER UPDATE ON Invoice
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Invoice', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Invoice
DELIMITER $$
CREATE TRIGGER trg_Invoice_Delete
AFTER DELETE ON Invoice
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Invoice', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Inventory
DELIMITER $$
CREATE TRIGGER trg_Inventory_Insert
AFTER INSERT ON Inventory
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Inventory', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Inventory
DELIMITER $$
CREATE TRIGGER trg_Inventory_Update
AFTER UPDATE ON Inventory
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Inventory', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Inventory
DELIMITER $$
CREATE TRIGGER trg_Inventory_Delete
AFTER DELETE ON Inventory
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Inventory', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Employee
DELIMITER $$
CREATE TRIGGER trg_Employee_Insert
AFTER INSERT ON Employee
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Employee', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Employee
DELIMITER $$
CREATE TRIGGER trg_Employee_Update
AFTER UPDATE ON Employee
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Employee', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Employee
DELIMITER $$
CREATE TRIGGER trg_Employee_Delete
AFTER DELETE ON Employee
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Employee', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Manager
DELIMITER $$
CREATE TRIGGER trg_Manager_Insert
AFTER INSERT ON Manager
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Manager', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Manager
DELIMITER $$
CREATE TRIGGER trg_Manager_Update
AFTER UPDATE ON Manager
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Manager', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Manager
DELIMITER $$
CREATE TRIGGER trg_Manager_Delete
AFTER DELETE ON Manager
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Manager', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Caretaker
DELIMITER $$
CREATE TRIGGER trg_Caretaker_Insert
AFTER INSERT ON Caretaker
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Caretaker', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Caretaker
DELIMITER $$
CREATE TRIGGER trg_Caretaker_Update
AFTER UPDATE ON Caretaker
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Caretaker', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Caretaker
DELIMITER $$
CREATE TRIGGER trg_Caretaker_Delete
AFTER DELETE ON Caretaker
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Caretaker', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for MessHead
DELIMITER $$
CREATE TRIGGER trg_MessHead_Insert
AFTER INSERT ON MessHead
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('MessHead', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for MessHead
DELIMITER $$
CREATE TRIGGER trg_MessHead_Update
AFTER UPDATE ON MessHead
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('MessHead', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for MessHead
DELIMITER $$
CREATE TRIGGER trg_MessHead_Delete
AFTER DELETE ON MessHead
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('MessHead', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for OtherStaff
DELIMITER $$
CREATE TRIGGER trg_OtherStaff_Insert
AFTER INSERT ON OtherStaff
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('OtherStaff', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for OtherStaff
DELIMITER $$
CREATE TRIGGER trg_OtherStaff_Update
AFTER UPDATE ON OtherStaff
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('OtherStaff', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for OtherStaff
DELIMITER $$
CREATE TRIGGER trg_OtherStaff_Delete
AFTER DELETE ON OtherStaff
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('OtherStaff', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for Attendance
DELIMITER $$
CREATE TRIGGER trg_Attendance_Insert
AFTER INSERT ON Attendance
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Attendance', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for Attendance
DELIMITER $$
CREATE TRIGGER trg_Attendance_Update
AFTER UPDATE ON Attendance
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Attendance', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for Attendance
DELIMITER $$
CREATE TRIGGER trg_Attendance_Delete
AFTER DELETE ON Attendance
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('Attendance', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for StudentLeave
DELIMITER $$
CREATE TRIGGER trg_StudentLeave_Insert
AFTER INSERT ON StudentLeave
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('StudentLeave', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for StudentLeave
DELIMITER $$
CREATE TRIGGER trg_StudentLeave_Update
AFTER UPDATE ON StudentLeave
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('StudentLeave', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for StudentLeave
DELIMITER $$
CREATE TRIGGER trg_StudentLeave_Delete
AFTER DELETE ON StudentLeave
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('StudentLeave', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for StaffLeave
DELIMITER $$
CREATE TRIGGER trg_StaffLeave_Insert
AFTER INSERT ON StaffLeave
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('StaffLeave', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for StaffLeave
DELIMITER $$
CREATE TRIGGER trg_StaffLeave_Update
AFTER UPDATE ON StaffLeave
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('StaffLeave', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for StaffLeave
DELIMITER $$
CREATE TRIGGER trg_StaffLeave_Delete
AFTER DELETE ON StaffLeave
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('StaffLeave', 'DELETE');
END $$
DELIMITER ;

-- INSERT Trigger for MessStatus
DELIMITER $$
CREATE TRIGGER trg_MessStatus_Insert
AFTER INSERT ON MessStatus
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('MessStatus', 'INSERT');
END $$
DELIMITER ;

-- UPDATE Trigger for MessStatus
DELIMITER $$
CREATE TRIGGER trg_MessStatus_Update
AFTER UPDATE ON MessStatus
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('MessStatus', 'UPDATE');
END $$
DELIMITER ;

-- DELETE Trigger for MessStatus
DELIMITER $$
CREATE TRIGGER trg_MessStatus_Delete
AFTER DELETE ON MessStatus
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TableName, ActionType)
    VALUES ('MessStatus', 'DELETE');
END $$
DELIMITER ;
