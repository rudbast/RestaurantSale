-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 02, 2014 at 10:51 AM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `resto_sale`
--
CREATE DATABASE IF NOT EXISTS `resto_sale` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `resto_sale`;

-- --------------------------------------------------------

--
-- Table structure for table `Menu`
--

CREATE TABLE IF NOT EXISTS `Menu` (
`ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `PRICE` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

--
-- Dumping data for table `Menu`
--

INSERT INTO `Menu` (`ID`, `NAME`, `TYPE`, `PRICE`) VALUES
(1, 'Fried Rice', 'FOOD', 12000),
(2, 'Chicken Wings', 'FOOD', 14996),
(3, 'Ice Lemon Tea', 'BEVERAGE', 8000),
(4, 'Cold Sweet Tea', 'BEVERAGE', 4000),
(5, 'Chicken Tempura', 'FOOD', 21000),
(6, 'Curry Rice', 'FOOD', 19000),
(7, 'Chicken Tempura', 'FOOD', 21000),
(8, 'Curry Rice', 'FOOD', 19000),
(9, 'Omelette Rice', 'FOOD', 17000),
(10, 'Original Ramen', 'FOOD', 19000),
(11, 'Omelette Rice', 'FOOD', 17000),
(12, 'Original Ramen', 'FOOD', 19000),
(13, 'Milk Tea', 'BEVERAGE', 10000),
(14, 'Carbonated Can', 'BEVERAGE', 8000),
(15, 'Milk Tea', 'BEVERAGE', 10000),
(16, 'Carbonated Can', 'BEVERAGE', 8000),
(17, 'Coffee', 'BEVERAGE', 6000),
(18, 'Fruit Juice', 'BEVERAGE', 9000),
(19, 'Coffee', 'BEVERAGE', 6000),
(20, 'Fruit Juice', 'BEVERAGE', 9000);

-- --------------------------------------------------------

--
-- Table structure for table `Queue`
--

CREATE TABLE IF NOT EXISTS `Queue` (
`NO` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `TABLE_NO` varchar(5) NOT NULL,
  `QUANTITY` int(11) NOT NULL,
  `STATUS` varchar(5) NOT NULL DEFAULT 'QUEUE'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

--
-- Dumping data for table `Queue`
--

INSERT INTO `Queue` (`NO`, `NAME`, `TABLE_NO`, `QUANTITY`, `STATUS`) VALUES
(1, 'Cold Sweet Tea', 'A15', 1, 'DONE'),
(3, 'Chicken Wings', 'A15', 1, 'DONE'),
(4, 'Fried Rice', 'A15', 2, 'DONE'),
(5, 'Ice Lemon Tea', 'A15', 1, 'DONE'),
(6, 'Cold Sweet Tea', 'B5', 2, 'DONE'),
(7, 'Fried Rice', 'B5', 1, 'DONE'),
(10, 'Cold Sweet Tea', '12', 3, 'DONE'),
(11, 'Ice Lemon Tea', '12', 3, 'DONE'),
(14, 'Fried Rice', '12', 1, 'DONE');

-- --------------------------------------------------------

--
-- Table structure for table `Sales`
--

CREATE TABLE IF NOT EXISTS `Sales` (
`NO` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `TABLE_NO` varchar(5) NOT NULL,
  `QUANTITY` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

--
-- Dumping data for table `Sales`
--

INSERT INTO `Sales` (`NO`, `NAME`, `TABLE_NO`, `QUANTITY`) VALUES
(1, 'Cold Sweet Tea', 'A15', 1),
(6, 'Cold Sweet Tea', 'B5', 2),
(7, 'Fried Rice', 'B5', 1),
(8, 'Chicken Wings', '12', 1),
(9, 'Cold Sweet Tea', '12', 1),
(10, 'Cold Sweet Tea', '12', 2),
(11, 'Ice Lemon Tea', '12', 1),
(12, 'Ice Lemon Tea', '12', 1),
(13, 'Ice Lemon Tea', '12', 1),
(14, 'Fried Rice', '12', 1),
(15, 'Cold Sweet Tea', '12', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Menu`
--
ALTER TABLE `Menu`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Queue`
--
ALTER TABLE `Queue`
 ADD PRIMARY KEY (`NO`);

--
-- Indexes for table `Sales`
--
ALTER TABLE `Sales`
 ADD PRIMARY KEY (`NO`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Menu`
--
ALTER TABLE `Menu`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=21;
--
-- AUTO_INCREMENT for table `Queue`
--
ALTER TABLE `Queue`
MODIFY `NO` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `Sales`
--
ALTER TABLE `Sales`
MODIFY `NO` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=16;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;