-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 15, 2020 at 11:59 AM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `stone`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_brand`
--

CREATE TABLE `tbl_brand` (
  `brand_id` int(11) NOT NULL,
  `brand` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_brand`
--

INSERT INTO `tbl_brand` (`brand_id`, `brand`) VALUES
(1, 'LG');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_cart`
--

CREATE TABLE `tbl_cart` (
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` bigint(100) NOT NULL,
  `party_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_collection`
--

CREATE TABLE `tbl_collection` (
  `collection_id` int(11) NOT NULL,
  `collection` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_collection`
--

INSERT INTO `tbl_collection` (`collection_id`, `collection`) VALUES
(1, 'collection 1');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_color`
--

CREATE TABLE `tbl_color` (
  `color_id` int(11) NOT NULL,
  `color_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_color`
--

INSERT INTO `tbl_color` (`color_id`, `color_name`) VALUES
(1, 'red'),
(2, 'blue'),
(5, 'pink'),
(6, 'yellow'),
(7, 'green'),
(8, 'maroon'),
(9, 'black'),
(10, 'white'),
(11, 'skyblue'),
(12, 'silver'),
(14, 'Indigo');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_main_cat`
--

CREATE TABLE `tbl_main_cat` (
  `main_cat_id` int(11) NOT NULL,
  `main_cat_name` varchar(50) NOT NULL,
  `main_cat_img` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_main_cat`
--

INSERT INTO `tbl_main_cat` (`main_cat_id`, `main_cat_name`, `main_cat_img`) VALUES
(1, 'love', 'uploads\\15.3.png'),
(2, 'hate', 'uploads\\main\\ic_team.png'),
(5, 'party', 'uploads\\main\\ic_winner.png');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_order`
--

CREATE TABLE `tbl_order` (
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `party_id` int(11) NOT NULL,
  `order_status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_owner`
--

CREATE TABLE `tbl_owner` (
  `owner_id` int(11) NOT NULL,
  `owner_name` varchar(50) NOT NULL,
  `mobile_number` varchar(50) NOT NULL,
  `birth_date` varchar(50) NOT NULL,
  `blood_group` varchar(50) NOT NULL,
  `anniversary_date` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_owner`
--

INSERT INTO `tbl_owner` (`owner_id`, `owner_name`, `mobile_number`, `birth_date`, `blood_group`, `anniversary_date`) VALUES
(2, 'admin', '1234567890', '4-2-1996', 'A+', '10-10-2020');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_party`
--

CREATE TABLE `tbl_party` (
  `party_id` int(11) NOT NULL,
  `party_code` varchar(50) NOT NULL,
  `party_name` varchar(50) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `party_address` varchar(200) NOT NULL,
  `pincode` varchar(50) NOT NULL,
  `area` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `reference_by` varchar(50) NOT NULL,
  `mobile_number` varchar(20) NOT NULL,
  `email_id` varchar(50) NOT NULL,
  `pan_no` varchar(50) NOT NULL,
  `gst_no` varchar(50) NOT NULL,
  `party_logo` varchar(1000) NOT NULL,
  `card_front` varchar(1000) NOT NULL,
  `card_back` varchar(1000) NOT NULL,
  `party_type` int(11) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_party`
--

INSERT INTO `tbl_party` (`party_id`, `party_code`, `party_name`, `owner_id`, `party_address`, `pincode`, `area`, `city`, `state`, `reference_by`, `mobile_number`, `email_id`, `pan_no`, `gst_no`, `party_logo`, `card_front`, `card_back`, `party_type`, `password`) VALUES
(6, '1', 'party1', 2, 'surat', '395006', 'surat', 'surat', 'gujarat', 'other', '1234567890', 'party1@gmail.com', '123123', '456456', 'uploads\\party\\logo\\party_logo_1601890749208.png', 'uploads\\party\\front\\card_front_1601890749209.png', 'uploads\\party\\back\\card_back_1601890749209.png', 0, 'party1@123'),
(7, '2', 'admin', 2, 'surat', '395006', 'surat', 'surat', 'gujarat', 'other', '1237890', 'admin@gmail.com', '456', '123', '1.png', '2.png', '3.png', 1, 'admin@123');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_product`
--

CREATE TABLE `tbl_product` (
  `product_id` int(11) NOT NULL,
  `sub_cat_id` int(11) NOT NULL,
  `product_name` varchar(200) NOT NULL,
  `quantity` int(11) NOT NULL,
  `size` int(11) NOT NULL,
  `size_info` varchar(50) NOT NULL,
  `color_id` int(11) NOT NULL,
  `tone_id` int(11) NOT NULL,
  `purity_id` int(11) NOT NULL,
  `shape_id` int(11) NOT NULL,
  `brand_id` int(11) NOT NULL,
  `remarks` varchar(100) NOT NULL,
  `collection_id` int(11) NOT NULL,
  `pro_type_id` int(11) NOT NULL,
  `price` bigint(20) NOT NULL,
  `mrp` bigint(20) NOT NULL,
  `product_image` varchar(500) NOT NULL,
  `group_id` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_product`
--

INSERT INTO `tbl_product` (`product_id`, `sub_cat_id`, `product_name`, `quantity`, `size`, `size_info`, `color_id`, `tone_id`, `purity_id`, `shape_id`, `brand_id`, `remarks`, `collection_id`, `pro_type_id`, `price`, `mrp`, `product_image`, `group_id`) VALUES
(5, 1, 'product 5', 10, 10, 'small', 1, 1, 1, 1, 1, 'product 1', 1, 1, 1000, 1000, 'uploads\\product\\7.3.png', '1'),
(6, 5, 'product 6', 10, 10, 'small', 2, 1, 1, 1, 1, 'product 1', 1, 1, 1000, 1000, 'uploads\\product\\2.png', '1');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_pro_type`
--

CREATE TABLE `tbl_pro_type` (
  `pro_type_id` int(11) NOT NULL,
  `pro_type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_pro_type`
--

INSERT INTO `tbl_pro_type` (`pro_type_id`, `pro_type`) VALUES
(1, 'type 1');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_purity`
--

CREATE TABLE `tbl_purity` (
  `purity_id` int(11) NOT NULL,
  `purity` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_purity`
--

INSERT INTO `tbl_purity` (`purity_id`, `purity`) VALUES
(1, 'black');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_shape`
--

CREATE TABLE `tbl_shape` (
  `shape_id` int(11) NOT NULL,
  `shape` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_shape`
--

INSERT INTO `tbl_shape` (`shape_id`, `shape`) VALUES
(1, 'circle'),
(3, 'rectangle');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sub_cat`
--

CREATE TABLE `tbl_sub_cat` (
  `sub_cat_id` int(11) NOT NULL,
  `main_cat_id` int(11) NOT NULL,
  `sub_cat_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_sub_cat`
--

INSERT INTO `tbl_sub_cat` (`sub_cat_id`, `main_cat_id`, `sub_cat_name`) VALUES
(1, 1, 'Indigo'),
(5, 1, 'black'),
(7, 2, 'white');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_tone`
--

CREATE TABLE `tbl_tone` (
  `tone_id` int(11) NOT NULL,
  `tone_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_tone`
--

INSERT INTO `tbl_tone` (`tone_id`, `tone_name`) VALUES
(1, 'Indigo');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `userid` int(11) NOT NULL,
  `username` text NOT NULL,
  `password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`userid`, `username`, `password`) VALUES
(1, 'admin', 'admin@123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_brand`
--
ALTER TABLE `tbl_brand`
  ADD PRIMARY KEY (`brand_id`),
  ADD KEY `INDEX` (`brand_id`);

--
-- Indexes for table `tbl_cart`
--
ALTER TABLE `tbl_cart`
  ADD PRIMARY KEY (`cart_id`),
  ADD KEY `INDEX` (`cart_id`) USING BTREE,
  ADD KEY `INDEX2` (`product_id`) USING BTREE,
  ADD KEY `INDEX3` (`party_id`);

--
-- Indexes for table `tbl_collection`
--
ALTER TABLE `tbl_collection`
  ADD PRIMARY KEY (`collection_id`),
  ADD KEY `INDEX` (`collection_id`);

--
-- Indexes for table `tbl_color`
--
ALTER TABLE `tbl_color`
  ADD PRIMARY KEY (`color_id`),
  ADD KEY `INDEX` (`color_id`);

--
-- Indexes for table `tbl_main_cat`
--
ALTER TABLE `tbl_main_cat`
  ADD PRIMARY KEY (`main_cat_id`),
  ADD KEY `INDEX` (`main_cat_id`);

--
-- Indexes for table `tbl_order`
--
ALTER TABLE `tbl_order`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `INDEX` (`order_id`),
  ADD KEY `INDEX2` (`product_id`) USING BTREE,
  ADD KEY `INDEX3` (`party_id`);

--
-- Indexes for table `tbl_owner`
--
ALTER TABLE `tbl_owner`
  ADD PRIMARY KEY (`owner_id`),
  ADD KEY `INDEX` (`owner_id`) USING BTREE;

--
-- Indexes for table `tbl_party`
--
ALTER TABLE `tbl_party`
  ADD PRIMARY KEY (`party_id`),
  ADD KEY `INDEX` (`party_id`),
  ADD KEY `OWNER_INDEX` (`owner_id`) USING BTREE;

--
-- Indexes for table `tbl_product`
--
ALTER TABLE `tbl_product`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `INDEX` (`product_id`),
  ADD KEY `tone_id` (`tone_id`),
  ADD KEY `collection_id` (`collection_id`),
  ADD KEY `pro_type_id` (`pro_type_id`),
  ADD KEY `color_id` (`color_id`),
  ADD KEY `shape_id` (`shape_id`),
  ADD KEY `purity_id` (`purity_id`),
  ADD KEY `brand_id` (`brand_id`),
  ADD KEY `INDEX2` (`sub_cat_id`,`color_id`,`tone_id`,`purity_id`,`shape_id`,`brand_id`,`collection_id`,`pro_type_id`) USING BTREE;

--
-- Indexes for table `tbl_pro_type`
--
ALTER TABLE `tbl_pro_type`
  ADD PRIMARY KEY (`pro_type_id`),
  ADD KEY `INDEX` (`pro_type_id`);

--
-- Indexes for table `tbl_purity`
--
ALTER TABLE `tbl_purity`
  ADD PRIMARY KEY (`purity_id`),
  ADD KEY `INDEX` (`purity_id`);

--
-- Indexes for table `tbl_shape`
--
ALTER TABLE `tbl_shape`
  ADD PRIMARY KEY (`shape_id`),
  ADD KEY `INDEX` (`shape_id`);

--
-- Indexes for table `tbl_sub_cat`
--
ALTER TABLE `tbl_sub_cat`
  ADD PRIMARY KEY (`sub_cat_id`),
  ADD KEY `INDEX` (`sub_cat_id`),
  ADD KEY `INDEX2` (`main_cat_id`) USING BTREE;

--
-- Indexes for table `tbl_tone`
--
ALTER TABLE `tbl_tone`
  ADD PRIMARY KEY (`tone_id`),
  ADD KEY `INDEX` (`tone_id`);

--
-- Indexes for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`userid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_brand`
--
ALTER TABLE `tbl_brand`
  MODIFY `brand_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_cart`
--
ALTER TABLE `tbl_cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `tbl_collection`
--
ALTER TABLE `tbl_collection`
  MODIFY `collection_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_color`
--
ALTER TABLE `tbl_color`
  MODIFY `color_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `tbl_main_cat`
--
ALTER TABLE `tbl_main_cat`
  MODIFY `main_cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tbl_order`
--
ALTER TABLE `tbl_order`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tbl_owner`
--
ALTER TABLE `tbl_owner`
  MODIFY `owner_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_party`
--
ALTER TABLE `tbl_party`
  MODIFY `party_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `tbl_product`
--
ALTER TABLE `tbl_product`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tbl_pro_type`
--
ALTER TABLE `tbl_pro_type`
  MODIFY `pro_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_purity`
--
ALTER TABLE `tbl_purity`
  MODIFY `purity_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_shape`
--
ALTER TABLE `tbl_shape`
  MODIFY `shape_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tbl_sub_cat`
--
ALTER TABLE `tbl_sub_cat`
  MODIFY `sub_cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `tbl_tone`
--
ALTER TABLE `tbl_tone`
  MODIFY `tone_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_user`
--
ALTER TABLE `tbl_user`
  MODIFY `userid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_cart`
--
ALTER TABLE `tbl_cart`
  ADD CONSTRAINT `tbl_cart_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `tbl_product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_cart_ibfk_2` FOREIGN KEY (`party_id`) REFERENCES `tbl_party` (`party_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_order`
--
ALTER TABLE `tbl_order`
  ADD CONSTRAINT `tbl_order_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `tbl_product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_order_ibfk_2` FOREIGN KEY (`party_id`) REFERENCES `tbl_party` (`party_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_party`
--
ALTER TABLE `tbl_party`
  ADD CONSTRAINT `tbl_party_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `tbl_owner` (`owner_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_product`
--
ALTER TABLE `tbl_product`
  ADD CONSTRAINT `tbl_product_ibfk_1` FOREIGN KEY (`sub_cat_id`) REFERENCES `tbl_sub_cat` (`sub_cat_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_2` FOREIGN KEY (`tone_id`) REFERENCES `tbl_tone` (`tone_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_3` FOREIGN KEY (`collection_id`) REFERENCES `tbl_collection` (`collection_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_4` FOREIGN KEY (`pro_type_id`) REFERENCES `tbl_pro_type` (`pro_type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_5` FOREIGN KEY (`color_id`) REFERENCES `tbl_color` (`color_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_6` FOREIGN KEY (`shape_id`) REFERENCES `tbl_shape` (`shape_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_7` FOREIGN KEY (`purity_id`) REFERENCES `tbl_purity` (`purity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_8` FOREIGN KEY (`brand_id`) REFERENCES `tbl_brand` (`brand_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_sub_cat`
--
ALTER TABLE `tbl_sub_cat`
  ADD CONSTRAINT `tbl_sub_cat_ibfk_1` FOREIGN KEY (`main_cat_id`) REFERENCES `tbl_main_cat` (`main_cat_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
