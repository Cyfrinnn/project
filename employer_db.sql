-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 12, 2025 at 09:56 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `employer_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'Offline',
  `last_active` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `username`, `password`, `status`, `last_active`) VALUES
(2, 'Lawrence', 'admin', 'Offline', '2025-04-04 08:08:32'),
(3, 'Oyo', '$2y$10$DAL7DQ/jlBin6mfzp3dHiOxgq7AaP55XSgNqsmxOj8ZqXgJv8jGkO', 'Offline', '2025-03-30 15:31:12'),
(4, 'Paul', '$2y$10$hOSX69fZF87Jodof81H5b./IbT9CJOQCJSzJuHW7kLx2qTj6JDmDu', 'Offline', '2025-03-20 00:38:23'),
(14, '[', 'admin', 'Offline', '2025-03-31 06:51:00');

-- --------------------------------------------------------

--
-- Table structure for table `applicants`
--

CREATE TABLE `applicants` (
  `id` int(11) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `total_applicants` int(11) DEFAULT 0,
  `address` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `applicants`
--

INSERT INTO `applicants` (`id`, `full_name`, `email`, `password`, `total_applicants`, `address`) VALUES
(1, 'John Doe', 'john.doe@example.com', '$2y$10$FgdxayQa6cEhGjO9UXFKX.pRF1/p.Xb3aCmZmnYcsSq/Tx5lII2sa', 0, ''),
(2, 'ed eniele mangilit', 'ed@example.com', '$2y$10$V50HOylqbtFGelB.RJH9Me3eLhYzTuHWlv3uPi3.8L4rKwo9iwgMG', 0, ''),
(3, 'ed', 'ed@example.com', '$2y$10$V2CjSLH/hBt.doKDVirOru1ORKOicDoSqeo.pwDz/0brMuKOLuo7y', 0, ''),
(4, 'lorens', 'lorens@example.com', '$2y$10$w5gVOb7IVY22LHXIuiPbte9576ntFqMKB6m5wILxlc.h1aMCki.T6', 0, ''),
(5, 'sample_applicant', 'sample@example.com', '$2y$10$Bw1Lps23edroxDBatVw.O.uPPBW1Tv68eSLTZHJWBHMH4yuQkwSdy', 0, ''),
(7, 'lawrence', 'lawrence@example.com', '$2y$10$QtOfmssUo3XK/Xv.3xmi6us.Zo8fBmgL.anlPsRO923fv54F9s40O', 0, ''),
(8, '', '', '$2y$10$y5aDsOimtPeT.dnHFIvUnezIT8CkU0SeWWCOy9KjWtaza4WAt0/Jq', 0, ''),
(9, '', '', '$2y$10$XAWI5rnUMyC5QvnqbIExsO/MNFfwHBxX/yacoNWsMeCsnuXiHt9uG', 0, ''),
(12, 'Skibidi Sigma', 'skibidisigma@example.com', '$2y$10$MeYdPlBW7X2NGC67Ycox5.jTBdVRLdd8XQ57a7Hq5BRrDeFIUh3PS', 0, ''),
(13, 'Bago to', 'bago@example.com', '$2y$10$XU/cx1qKakxjzLyRuS/0RubZzuGoMJjJSa65dHz3e6WuZ9vAoMyVi', 0, ''),
(14, 'Test Applicant', 'test@example.com', '$2y$10$l7L1tDPGuuHoKhI/N3gn5OixMxRPE7gyRsQHgyMkBgpW0wHYa4iSC', 0, '');

-- --------------------------------------------------------

--
-- Table structure for table `employers`
--

CREATE TABLE `employers` (
  `id` int(11) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_employers` int(11) DEFAULT 0,
  `file_path` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employers`
--

INSERT INTO `employers` (`id`, `company_name`, `email`, `password`, `status`, `total_employers`, `file_path`, `updated_at`) VALUES
(1, 'Example Company', 'example@example.com', '$2y$10$DqU1BsXxGjHZWZho3j9Xrut/v6b0BJFoWTTXwjrOJMKom//nl1Gte', 'Approved', 0, NULL, '2025-03-31 14:53:46'),
(2, 'linux', 'linux@example.com', '$2y$10$VVs4l5D/H9lkEmLPT1d7n.PNB9Y/Nbc18aX3XFmYJ1b5jiv7pBaVS', 'Denied', 0, NULL, '2025-03-31 14:52:43'),
(3, 'WorkBridgers', 'workbridge@example.co', '$2y$10$t7W.8BPh6YFP48rZgupTj.j3kAUMwip/MJpjeLYZHY5CjAX7DM/la', 'Approved', 0, NULL, '2025-03-19 07:37:00'),
(7, 'The Smiths', 'thesmiths@example.com', '$2y$10$JJpqLdKLCAF8rv2kx7bXWuak5ABSQA91FmN46dsDSERJ8tZl7Feyq', 'Approved', 0, NULL, '2025-03-19 07:39:12'),
(8, 'hahaha', 'hahaha@example.com', '$2y$10$hJGzp5/1DFAFqSnNw3uSIelC6ZKa8aJZHe7aRAdXVDtrmpTMfE.RO', 'Approved', 0, NULL, '2025-03-19 07:39:12'),
(9, 'asd', 'asd@example.com', '$2y$10$jFmeOe/uAB9UmT7A36/S2.XT8FEbpyDTVKtqJXb6dHWKJdf4qAGA.', 'Approved', 0, NULL, '2025-03-19 07:39:13'),
(22, 'mhw', 'mhw@examle.com', '$2y$10$FvsInImuunGG1vkAOwHikO5pVu6L4Ay92KQL0BOJRCFB6GYCX52nG', 'Approved', 0, NULL, '2025-03-19 05:49:36'),
(26, 'acer', 'acer@example.com', '$2y$10$uA1EmHpikL1sY02xOU453umZtSdgfOoXS36wbRTYUJuvIKhBXE16W', 'Approved', 0, NULL, NULL),
(36, 'haha', 'haha@example.com', '$2y$10$chi93yVFPI.t9xrSUNBIG.t8ar7Kzy/hP1Tx0YGz7HJcehpaiN3q.', NULL, 0, NULL, NULL),
(37, 'bago to', 'bago@example.com', '$2y$10$KVF/A2wXVSwBKeSpfIZVSO9.0Kg7ky21EzX/HzMaNPHlE06iyDrDi', NULL, 0, NULL, NULL),
(38, 'luma to', 'luma@example.com', '$2y$10$6/3wutGio24EjXx6pQAEh.XID2bDTHOJmGiL5EIR8YUXgTlokDjSK', NULL, 0, NULL, NULL),
(39, 'luma to', 'WorkBridgers@example.com', '$2y$10$jlF458NLbyBXhwXb.y3R.uNnCAjfmNoNy6nK/4rUTk2Cq1sNujsK.', 'Approved', 0, NULL, '2025-03-26 06:48:50'),
(40, 'Skibidi Sigma', 'skibidisigma@example.com', '$2y$10$H3uYk77BnQUQOdR2MmX4iuIsUqMZKkgukqkEdMGnV6CiTNXNHXeNK', NULL, 0, NULL, NULL),
(41, 'Last na to', 'last@example.com', '$2y$10$AEcmFZdGeWR04h9NrTSJkep26/6KDGtEqj9imB/NCgY1mh6MQEWLS', NULL, 0, NULL, NULL),
(42, 'tite', 'tite@example.com', '$2y$10$QwVGnx019SkQ2Iq2CHf7ouJsikOCQHMvvg37HS/ZY.dFXIBSIiNfi', 'Approved', 0, NULL, '2025-03-31 14:21:27'),
(43, 'BAGO TO', 'bagoto@example.com', '$2y$10$hyFOHx1sQldtt7bPHon2pO/9ZBAiKv8pQRDhzYKMxWlvh7HqCQ2AG', NULL, 0, NULL, NULL),
(44, 'Cybertron', 'cybertron@example.com', '$2y$10$dV90ePHKWpDxJ39jc7MAXegElZUFZ3EPlTrQF/2gStnkzi7V4p40i', NULL, 0, NULL, NULL),
(45, 'Lawrence kuprahan', 'lawrence@example.com', '$2y$10$WWJIoM9tXSc2iu51mis3Wef8fDlwgU4JlwNaFgrPijIwpxlGZd3H6', NULL, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `hiring_history`
--

CREATE TABLE `hiring_history` (
  `id` int(11) NOT NULL,
  `applicant_id` int(11) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `hired_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `job_posts`
--

CREATE TABLE `job_posts` (
  `id` int(11) NOT NULL,
  `employer_id` int(11) NOT NULL,
  `job_title` varchar(255) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `job_description` text NOT NULL,
  `salary_range` varchar(100) DEFAULT NULL,
  `job_type` varchar(50) DEFAULT NULL,
  `job_location` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `job_posts`
--

INSERT INTO `job_posts` (`id`, `employer_id`, `job_title`, `company_name`, `job_description`, `salary_range`, `job_type`, `job_location`, `created_at`) VALUES
(103, 42, 'waeawe', 'sd', 'aw', '1243134', 'Full-Time', 'asdasdasd', '2025-03-30 15:24:51'),
(104, 26, 'awe', 'asd', 'awe', '124', 'Full-Time', 'qwe', '2025-03-30 16:12:43'),
(105, 26, 'HACKER', 'ACER', 'ASDASDASD', '1243', 'Full-Time', 'SUAL', '2025-03-30 19:37:53'),
(106, 26, 'araw', 'wra', 'awr', '124', 'Full-Time', 'eq', '2025-03-30 19:43:44'),
(107, 26, 'wqdas', 'qwdas', 'wfqa', '14', 'Full-Time', 'asc', '2025-03-30 20:38:11'),
(108, 26, 'awesda', 'qwe', 'qwe', '123', 'Full-Time', 'asd', '2025-03-30 21:40:33'),
(109, 26, 'TEST TITLE', 'TEST COMPANY NAME', 'TEST JOB DESCRIPTION', '9999', 'Full-Time', 'TEST JOB LOCATION', '2025-03-30 22:06:38'),
(110, 26, 'TEST 5', 'TEST 5', 'TEST 5', '123', 'Full-Time', 'TEST5', '2025-03-31 02:32:37'),
(111, 26, 'eawrawf', 'asdas', 'dasd', '21351', 'Full-Time', 'asdasd', '2025-03-31 03:59:16'),
(112, 26, 'gagagagag', 'hahahah', 'hahahah', '646646464', 'Full-Time', 'hahahahahah', '2025-03-31 05:34:22'),
(113, 26, 'WEB DEV', 'ACER', 'DAPAT MAGALING', '9999', 'Full-Time', 'DAGUPAN', '2025-03-31 06:55:48'),
(114, 45, 'sdfgh', 'sdfg', 'sdfg', '12345', 'Full-Time', 'dfg', '2025-03-31 07:00:27');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `applicants`
--
ALTER TABLE `applicants`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `employers`
--
ALTER TABLE `employers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `hiring_history`
--
ALTER TABLE `hiring_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `applicant_id` (`applicant_id`);

--
-- Indexes for table `job_posts`
--
ALTER TABLE `job_posts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `employer_id` (`employer_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `applicants`
--
ALTER TABLE `applicants`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `employers`
--
ALTER TABLE `employers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `hiring_history`
--
ALTER TABLE `hiring_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `job_posts`
--
ALTER TABLE `job_posts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `hiring_history`
--
ALTER TABLE `hiring_history`
  ADD CONSTRAINT `hiring_history_ibfk_1` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`id`);

--
-- Constraints for table `job_posts`
--
ALTER TABLE `job_posts`
  ADD CONSTRAINT `job_posts_ibfk_1` FOREIGN KEY (`employer_id`) REFERENCES `employers` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
