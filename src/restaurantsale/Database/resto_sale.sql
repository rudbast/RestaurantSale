-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 28, 2014 at 06:30 AM
-- Server version: 5.6.16
-- PHP Version: 5.5.11

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
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `PRICE` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `Menu`
--

INSERT INTO `Menu` (`ID`, `NAME`, `TYPE`, `PRICE`) VALUES
(1, 'Fried Rice', 'FOOD', 12000),
(2, 'Chicken Wings', 'FOOD', 14996),
(3, 'Ice Lemon Tea', 'BEVERAGE', 8000),
(4, 'Cold Sweet Tea', 'BEVERAGE', 4000);

-- --------------------------------------------------------

--
-- Table structure for table `Queue`
--

CREATE TABLE IF NOT EXISTS `Queue` (
  `NO` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `TABLE_NO` varchar(5) NOT NULL,
  `QUANTITY` int(11) NOT NULL,
  `STATUS` varchar(5) NOT NULL DEFAULT 'QUEUE',
  PRIMARY KEY (`NO`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `Queue`
--

INSERT INTO `Queue` (`NO`, `NAME`, `TABLE_NO`, `QUANTITY`, `STATUS`) VALUES
(1, 'Cold Sweet Tea', 'A15', 1, 'DONE'),
(3, 'Chicken Wings', 'A15', 1, 'DONE'),
(4, 'Fried Rice', 'A15', 2, 'DONE'),
(5, 'Ice Lemon Tea', 'A15', 1, 'DONE'),
(6, 'Cold Sweet Tea', 'B5', 2, 'QUEUE'),
(7, 'Fried Rice', 'B5', 1, 'QUEUE');

-- --------------------------------------------------------

--
-- Table structure for table `Sales`
--

CREATE TABLE IF NOT EXISTS `Sales` (
  `NO` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `TABLE_NO` varchar(5) NOT NULL,
  `QUANTITY` int(11) NOT NULL,
  PRIMARY KEY (`NO`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `Sales`
--

INSERT INTO `Sales` (`NO`, `NAME`, `TABLE_NO`, `QUANTITY`) VALUES
(1, 'Cold Sweet Tea', 'A15', 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
