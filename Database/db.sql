-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: hostelmanagementsystem
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `AttendanceID` int NOT NULL AUTO_INCREMENT,
  `StudentID` int DEFAULT NULL,
  `AttendanceDate` date DEFAULT NULL,
  `Status` enum('Present','Absent','Leave') DEFAULT 'Present',
  `RecordedBy` int DEFAULT NULL,
  PRIMARY KEY (`AttendanceID`),
  KEY `StudentID` (`StudentID`),
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `student` (`StudentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (1000000,1000006,'2025-05-13','Present',0),(1000001,1000007,'2025-05-13','Absent',0);
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auditlog`
--

DROP TABLE IF EXISTS `auditlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auditlog` (
  `AuditID` int NOT NULL AUTO_INCREMENT,
  `TableName` varchar(100) NOT NULL,
  `ActionType` enum('INSERT','UPDATE','DELETE') NOT NULL,
  `ActionDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `OldValue` text,
  `NewValue` text,
  `UserID` int DEFAULT NULL,
  PRIMARY KEY (`AuditID`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `auditlog_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=1000000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditlog`
--

LOCK TABLES `auditlog` WRITE;
/*!40000 ALTER TABLE `auditlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `auditlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caretaker`
--

DROP TABLE IF EXISTS `caretaker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caretaker` (
  `CaretakerID` int NOT NULL AUTO_INCREMENT,
  `UserID` int DEFAULT NULL,
  `EmployeeID` int DEFAULT NULL,
  `HostelID` int DEFAULT NULL,
  PRIMARY KEY (`CaretakerID`),
  KEY `UserID` (`UserID`),
  KEY `EmployeeID` (`EmployeeID`),
  KEY `HostelID` (`HostelID`),
  CONSTRAINT `caretaker_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `caretaker_ibfk_2` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `caretaker_ibfk_3` FOREIGN KEY (`HostelID`) REFERENCES `hostel` (`HostelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caretaker`
--

LOCK TABLES `caretaker` WRITE;
/*!40000 ALTER TABLE `caretaker` DISABLE KEYS */;
INSERT INTO `caretaker` VALUES (1000000,NULL,NULL,1000000),(1000001,NULL,NULL,1000000),(1000002,1000014,1000004,1000000);
/*!40000 ALTER TABLE `caretaker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `EmployeeID` int NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(255) DEFAULT NULL,
  `MiddleName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  `CNIC` varchar(15) DEFAULT NULL,
  `Gender` enum('Male','Female','Other') DEFAULT 'Other',
  `Phone` varchar(15) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `ProfilePic` longblob,
  `Address` text,
  `EmploymentDate` date DEFAULT NULL,
  `JoiningDate` date DEFAULT NULL,
  `IsActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`EmployeeID`),
  UNIQUE KEY `CNIC` (`CNIC`)
) ENGINE=InnoDB AUTO_INCREMENT=1000010 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1000000,'Alice',NULL,'Manager','12345-1234567-1','Female','03001234567','alice.manager@hostel.com',NULL,'Manager House','2025-05-13','2025-05-13',1),(1000001,'Bob',NULL,'Caretaker','23456-2345678-2','Male','03111234567','bob.caretaker@hostel.com',NULL,'Caretaker House A','2025-05-13','2025-05-13',1),(1000002,'Carol',NULL,'Caretaker','34567-3456789-3','Female','03221234567','carol.caretaker@hostel.com',NULL,'Caretaker House B','2025-05-13','2025-05-13',1),(1000003,'Saad','Muhammad','Khan','1720155298191','Male','03369341134','s@gmail.com',NULL,'home','2025-05-01','2025-05-15',1),(1000004,'Saad','Muhammad','Khan','1720155298198','Male','03369341134','s2@gmail.com',NULL,'home','2025-05-01','2025-05-15',1),(1000006,'A',NULL,'H','1720155298182','Female','00','s7@gmail.com',NULL,'e','2025-05-01','2025-05-13',1),(1000008,'Abdul','Hanan','Khan','1720155298184','Male','03369341134','s9@gmail.com',NULL,'s','2025-05-02','2025-05-14',1),(1000009,'Saad',NULL,'Khan','1720155389461','Male','03369341134','s10@gmail.com',NULL,'home','2025-05-01','2025-05-22',1);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hostel`
--

DROP TABLE IF EXISTS `hostel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hostel` (
  `HostelID` int NOT NULL AUTO_INCREMENT,
  `HostelName` varchar(255) DEFAULT NULL,
  `TotalRooms` int DEFAULT NULL,
  `RoomType` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`HostelID`)
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hostel`
--

LOCK TABLES `hostel` WRITE;
/*!40000 ALTER TABLE `hostel` DISABLE KEYS */;
INSERT INTO `hostel` VALUES (1000000,'Hajveri',50,'Shared');
/*!40000 ALTER TABLE `hostel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `InventoryID` int NOT NULL AUTO_INCREMENT,
  `ItemName` varchar(100) DEFAULT NULL,
  `Category` varchar(50) DEFAULT NULL,
  `Quantity` int DEFAULT NULL,
  `Description` text,
  `LastUpdated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`InventoryID`)
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1000000,'Soap','Cleaning',14,'soap','2025-05-13 12:14:17');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `InvoiceID` int NOT NULL AUTO_INCREMENT,
  `StudentID` int DEFAULT NULL,
  `InvoiceType` varchar(100) DEFAULT NULL,
  `DueDate` date DEFAULT NULL,
  `Amount` decimal(10,2) DEFAULT NULL,
  `Status` enum('Paid','Unpaid','Pending') DEFAULT 'Pending',
  `IssueDate` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`InvoiceID`),
  KEY `StudentID` (`StudentID`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `student` (`StudentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000004 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1000000,1000006,'Mess ','2025-05-21',0.00,'Pending',NULL),(1000001,1000006,'Rent','2025-06-04',10000.00,'Pending',NULL),(1000002,1000007,'Mess ','2025-05-22',0.00,'Pending',NULL),(1000003,1000006,'Mess ','2025-05-28',0.00,'Pending',NULL);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manager` (
  `ManagerID` int NOT NULL AUTO_INCREMENT,
  `UserID` int DEFAULT NULL,
  `EmployeeID` int DEFAULT NULL,
  `HostelID` int DEFAULT NULL,
  PRIMARY KEY (`ManagerID`),
  KEY `UserID` (`UserID`),
  KEY `EmployeeID` (`EmployeeID`),
  KEY `HostelID` (`HostelID`),
  CONSTRAINT `manager_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `manager_ibfk_2` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `manager_ibfk_3` FOREIGN KEY (`HostelID`) REFERENCES `hostel` (`HostelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES (1000000,1000000,1000000,1000000);
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messhead`
--

DROP TABLE IF EXISTS `messhead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messhead` (
  `HeadID` int NOT NULL AUTO_INCREMENT,
  `EmployeeID` int DEFAULT NULL,
  `HostelID` int DEFAULT NULL,
  PRIMARY KEY (`HeadID`),
  KEY `EmployeeID` (`EmployeeID`),
  KEY `HostelID` (`HostelID`),
  CONSTRAINT `messhead_ibfk_1` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `messhead_ibfk_2` FOREIGN KEY (`HostelID`) REFERENCES `hostel` (`HostelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messhead`
--

LOCK TABLES `messhead` WRITE;
/*!40000 ALTER TABLE `messhead` DISABLE KEYS */;
/*!40000 ALTER TABLE `messhead` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messstatus`
--

DROP TABLE IF EXISTS `messstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messstatus` (
  `StatusID` int NOT NULL AUTO_INCREMENT,
  `StudentID` int DEFAULT NULL,
  `LeaveDate` date DEFAULT NULL,
  `JoinDate` date DEFAULT NULL,
  `Reason` text,
  PRIMARY KEY (`StatusID`),
  KEY `StudentID` (`StudentID`),
  CONSTRAINT `messstatus_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `student` (`StudentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messstatus`
--

LOCK TABLES `messstatus` WRITE;
/*!40000 ALTER TABLE `messstatus` DISABLE KEYS */;
INSERT INTO `messstatus` VALUES (1000000,1000006,'2025-05-13','2025-05-17','User requested via system'),(1000001,1000006,'2025-06-10','2025-06-20','User requested via system');
/*!40000 ALTER TABLE `messstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otherstaff`
--

DROP TABLE IF EXISTS `otherstaff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otherstaff` (
  `StaffID` int NOT NULL AUTO_INCREMENT,
  `EmployeeID` int DEFAULT NULL,
  `HostelID` int DEFAULT NULL,
  PRIMARY KEY (`StaffID`),
  KEY `EmployeeID` (`EmployeeID`),
  KEY `HostelID` (`HostelID`),
  CONSTRAINT `otherstaff_ibfk_1` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `otherstaff_ibfk_2` FOREIGN KEY (`HostelID`) REFERENCES `hostel` (`HostelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otherstaff`
--

LOCK TABLES `otherstaff` WRITE;
/*!40000 ALTER TABLE `otherstaff` DISABLE KEYS */;
INSERT INTO `otherstaff` VALUES (1000000,1000008,1000000),(1000001,1000009,1000000);
/*!40000 ALTER TABLE `otherstaff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `RoomID` int NOT NULL AUTO_INCREMENT,
  `HostelID` int DEFAULT NULL,
  `RoomNumber` varchar(20) DEFAULT NULL,
  `Capacity` int DEFAULT NULL,
  `IsFull` tinyint(1) DEFAULT '0',
  `Type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`RoomID`),
  KEY `HostelID` (`HostelID`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`HostelID`) REFERENCES `hostel` (`HostelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (100,1000000,'100',100,0,'three seater'),(101,1000000,'101',100,0,'2 seater'),(102,1000000,'103',100,0,'laundary room'),(103,1000000,'104',3,0,'Three Seater'),(104,1000000,'105',3,0,'Three Seater'),(105,1000000,'106',2,1,'Two Seater');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary`
--

DROP TABLE IF EXISTS `salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salary` (
  `SalaryID` int NOT NULL AUTO_INCREMENT,
  `EmployeeID` int DEFAULT NULL,
  `Amount` decimal(10,2) DEFAULT NULL,
  `Bonus` decimal(10,2) DEFAULT '0.00',
  `Deductions` decimal(10,2) DEFAULT '0.00',
  `NetPay` decimal(10,2) GENERATED ALWAYS AS (((`Amount` + `Bonus`) - `Deductions`)) STORED,
  `PayDate` date DEFAULT NULL,
  `PaymentMode` enum('Cash','Bank Transfer','Cheque') DEFAULT 'Bank Transfer',
  PRIMARY KEY (`SalaryID`),
  KEY `EmployeeID` (`EmployeeID`),
  CONSTRAINT `salary_ibfk_1` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary`
--

LOCK TABLES `salary` WRITE;
/*!40000 ALTER TABLE `salary` DISABLE KEYS */;
/*!40000 ALTER TABLE `salary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staffleave`
--

DROP TABLE IF EXISTS `staffleave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staffleave` (
  `LeaveID` int NOT NULL AUTO_INCREMENT,
  `EmployeeID` int DEFAULT NULL,
  `ApplyDate` date DEFAULT NULL,
  `LeaveDate` date DEFAULT NULL,
  `ReturnDate` date DEFAULT NULL,
  `Reason` text,
  PRIMARY KEY (`LeaveID`),
  KEY `EmployeeID` (`EmployeeID`),
  CONSTRAINT `staffleave_ibfk_1` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staffleave`
--

LOCK TABLES `staffleave` WRITE;
/*!40000 ALTER TABLE `staffleave` DISABLE KEYS */;
INSERT INTO `staffleave` VALUES (1000000,1000006,'2025-05-13','2025-05-13','2025-05-20','Nothing'),(1000001,1000004,'2025-05-13','2025-05-14','2025-05-15','R');
/*!40000 ALTER TABLE `staffleave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `StudentID` int NOT NULL AUTO_INCREMENT,
  `UserID` int NOT NULL,
  `RoomID` int DEFAULT NULL,
  `HostelID` int DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `InstiName` varchar(100) DEFAULT NULL,
  `RollNo` varchar(100) DEFAULT NULL,
  `InstiAddmissionDate` date DEFAULT NULL,
  `Department` varchar(100) DEFAULT NULL,
  `AllotmentDate` date DEFAULT NULL,
  `GuardianName` varchar(255) DEFAULT NULL,
  `GuardianPhone` varchar(15) DEFAULT NULL,
  `GaurdianEmail` varchar(255) DEFAULT NULL,
  `EmergencyNo` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`StudentID`),
  UNIQUE KEY `unique_rollno` (`RollNo`),
  KEY `UserID` (`UserID`),
  KEY `RoomID` (`RoomID`),
  KEY `HostelID` (`HostelID`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `student_ibfk_2` FOREIGN KEY (`RoomID`) REFERENCES `room` (`RoomID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `student_ibfk_3` FOREIGN KEY (`HostelID`) REFERENCES `hostel` (`HostelID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000009 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1000006,1000010,105,1000000,'2025-05-01','NUST','503774','2025-05-01','SEECS','2025-05-01','Muhammad Khan','03480230807','skyisblack95@gmail.com','03480230807'),(1000007,1000011,105,1000000,'2025-05-01','NUST','503775','2025-05-01','SEECS','2025-05-01','Muhammad Khan','03369341134','skyisblack96@gmail.com','03369341134'),(1000008,1000012,NULL,1000000,'2025-05-01','NUST','503777','2025-05-01','SEECS','2025-05-13','Muhammad Ullah','03369341134','skyisblack97@gmail.com','03369341134');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studentleave`
--

DROP TABLE IF EXISTS `studentleave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studentleave` (
  `LeaveID` int NOT NULL AUTO_INCREMENT,
  `StudentID` int DEFAULT NULL,
  `ApplyDate` date DEFAULT NULL,
  `LeaveDate` date DEFAULT NULL,
  `ReturnDate` date DEFAULT NULL,
  `Destination` text,
  `Reason` text,
  PRIMARY KEY (`LeaveID`),
  KEY `StudentID` (`StudentID`),
  CONSTRAINT `studentleave_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `student` (`StudentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studentleave`
--

LOCK TABLES `studentleave` WRITE;
/*!40000 ALTER TABLE `studentleave` DISABLE KEYS */;
INSERT INTO `studentleave` VALUES (1000002,1000006,'2025-05-14','2025-05-14','2025-05-16','R',NULL);
/*!40000 ALTER TABLE `studentleave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(255) NOT NULL,
  `MiddleName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) NOT NULL,
  `CNIC` varchar(15) NOT NULL,
  `Gender` enum('Male','Female','Other') NOT NULL DEFAULT 'Other',
  `Phone` varchar(15) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Address` text,
  `Password` varchar(255) DEFAULT NULL,
  `ProfilePic` longblob,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `CNIC` (`CNIC`),
  UNIQUE KEY `unique_cnic` (`CNIC`),
  UNIQUE KEY `unique_email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=1000015 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1000000,'Alice',NULL,'Manager','12345-1234567-1','Female','03001234567','alice.manager@hostel.com','Manager House','managerpass',NULL),(1000001,'Bob',NULL,'Caretaker','23456-2345678-2','Male','03111234567','bob.caretaker@hostel.com','Caretaker House A','caretakerpass',NULL),(1000002,'Carol',NULL,'Caretaker','34567-3456789-3','Female','03221234567','carol.caretaker@hostel.com','Caretaker House B','caretakerpass',NULL),(1000010,'Saad','Muhammad','Khan','1720155298191','Male','03369341134','skyisblack95@gmail.com','Home',NULL,NULL),(1000011,'Abdul',NULL,'Hanan','1720155298192','Male','03369341134','skyisblack96@gmail.com','Home',NULL,NULL),(1000012,'Muazam',NULL,'Khan','1720155298193','Male','03369341134','skyisblack97@gmail.com','Home',NULL,NULL),(1000014,'Saad','Muhammad','Khan','1720155298198','Male','03369341134','s2@gmail.com','home','caretakerpass',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitor`
--

DROP TABLE IF EXISTS `visitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitor` (
  `VisitorID` int NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(255) DEFAULT NULL,
  `MiddleName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  `CNIC` varchar(15) DEFAULT NULL,
  `Gender` enum('Male','Female','Other') DEFAULT 'Other',
  `Email` varchar(100) DEFAULT NULL,
  `Phone` varchar(15) DEFAULT NULL,
  `StudentID` int DEFAULT NULL,
  PRIMARY KEY (`VisitorID`),
  KEY `StudentID` (`StudentID`),
  CONSTRAINT `visitor_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `student` (`StudentID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor`
--

LOCK TABLES `visitor` WRITE;
/*!40000 ALTER TABLE `visitor` DISABLE KEYS */;
/*!40000 ALTER TABLE `visitor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-13 23:42:02
