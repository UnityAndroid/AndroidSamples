-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 23, 2020 at 01:22 PM
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
-- Database: `jwell`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_banner`
--

CREATE TABLE `tbl_banner` (
  `banner_id` int(11) NOT NULL,
  `sub_cat_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `subtitle` varchar(100) NOT NULL,
  `banner_image` varchar(1000) NOT NULL,
  `designs` varchar(100) NOT NULL,
  `start_from` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_banner`
--

INSERT INTO `tbl_banner` (`banner_id`, `sub_cat_id`, `title`, `subtitle`, `banner_image`, `designs`, `start_from`) VALUES
(2, 1, 'title1', 'subtitle1', 'uploads\\main\\7.png', '10 designs', 'Starting From 1200'),
(5, 5, 'title1', 'subtitle1', 'uploads\\main\\7.png', '10 designs', 'Starting From 1200');

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
(4, 'Samsung'),
(5, 'asdasd'),
(6, 'Test1'),
(7, 'Test2'),
(9, 'Test3');

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
(1, 'collection 1'),
(3, 'asdasdasdasd');

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
(1, 'love', 'http://shreeinfinityinfotech.com/magically/Images/video_images/1595243496096.jpg'),
(2, 'hate', 'http://shreeinfinityinfotech.com/magically/Images/video_images/1595243484605.jpg'),
(5, 'party', 'http://shreeinfinityinfotech.com/magically/Images/video_images/1595425376380.jpg'),
(6, 'cat 2', 'uploads\\main\\image_2020_10_07T11_16_28_014Z.png');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_metal_color`
--

CREATE TABLE `tbl_metal_color` (
  `metal_color_id` int(11) NOT NULL,
  `metal_color_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_metal_color`
--

INSERT INTO `tbl_metal_color` (`metal_color_id`, `metal_color_name`) VALUES
(2, 'black'),
(19, 'black');

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
  `design_number` varchar(500) NOT NULL,
  `quantity` int(11) NOT NULL,
  `size` int(11) NOT NULL,
  `size_info` varchar(50) NOT NULL,
  `weight` varchar(100) NOT NULL,
  `gross_weight` varchar(100) NOT NULL,
  `less_weight` varchar(100) NOT NULL,
  `net_weight` varchar(100) NOT NULL,
  `metal_color_id` int(11) NOT NULL,
  `rodium_color_id` int(11) NOT NULL,
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
  `product_video` varchar(500) NOT NULL,
  `group_id` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_product`
--

INSERT INTO `tbl_product` (`product_id`, `sub_cat_id`, `design_number`, `quantity`, `size`, `size_info`, `weight`, `gross_weight`, `less_weight`, `net_weight`, `metal_color_id`, `rodium_color_id`, `tone_id`, `purity_id`, `shape_id`, `brand_id`, `remarks`, `collection_id`, `pro_type_id`, `price`, `mrp`, `product_image`, `product_video`, `group_id`) VALUES
(17, 5, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_1603366179711.png', 'uploads\\product\\video\\product_video_1603366179714.png', '1'),
(18, 5, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_1603366186305.png', 'uploads\\product\\video\\product_video_1603366186307.png', '1'),
(19, 1, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_1603366346140.png', 'uploads\\product\\video\\product_video_1603366346142.png', '1'),
(21, 7, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_1603366467150.png', 'uploads\\product\\video\\product_video_1603366467152.png', '1'),
(22, 5, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_1603366559970.png', 'uploads\\product\\video\\product_video_1603366559972.png', '1'),
(23, 5, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_1603366587061.png', 'uploads\\product\\video\\product_video_1603366587063.png', '1'),
(24, 7, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_1603368655549.png', 'uploads\\product\\video\\product_video_1603368655552.png', '1'),
(25, 7, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_7.png_1603370260651.png', 'uploads\\product\\video\\product_video_4.png_1603370260653.png', '1'),
(26, 7, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_undefined_1603370410973.png', 'uploads\\product\\video\\product_video_undefined_1603370410975.png', '1'),
(27, 7, 'DESIGN1', 10, 10, 'small', '10gm', '10gm', '10gm', '10gm', 2, 2, 1, 1, 1, 4, 'product 2', 1, 1, 1000, 1000, 'uploads\\product\\main\\product_image_7_1603370484153.png', 'uploads\\product\\video\\product_video_4_1603370484155.png', '1');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_product_imgs`
--

CREATE TABLE `tbl_product_imgs` (
  `img_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_image` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_product_imgs`
--

INSERT INTO `tbl_product_imgs` (`img_id`, `product_id`, `product_image`) VALUES
(1, 23, 'uploads\\product\\other\\other_images_1603366587065.png'),
(2, 23, 'uploads\\product\\other\\other_images_1603366587066.png'),
(3, 23, 'uploads\\product\\other\\other_images_1603366587066.png'),
(4, 24, 'uploads\\product\\other\\other_images_1603368655554.png'),
(5, 24, 'uploads\\product\\other\\other_images_1603368655555.png'),
(6, 24, 'uploads\\product\\other\\other_images_1603368655555.png'),
(7, 25, 'uploads\\product\\other\\other_images_9.png_1603370260655.png'),
(8, 25, 'uploads\\product\\other\\other_images_10.png_1603370260655.png'),
(9, 25, 'uploads\\product\\other\\other_images_11.1.png_1603370260656.png'),
(10, 26, 'uploads\\product\\other\\other_images_undefined_1603370410977.png'),
(11, 26, 'uploads\\product\\other\\other_images_undefined_1603370410978.png'),
(12, 26, 'uploads\\product\\other\\other_images_undefined_1603370410978.png'),
(13, 27, 'uploads\\product\\other\\other_images_9_1603370484157.png'),
(14, 27, 'uploads\\product\\other\\other_images_10_1603370484157.png'),
(15, 27, 'uploads\\product\\other\\other_images_11.1_1603370484158.png');

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
(1, 'black1');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_rodium_color`
--

CREATE TABLE `tbl_rodium_color` (
  `rodium_color_id` int(11) NOT NULL,
  `rodium_color_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_rodium_color`
--

INSERT INTO `tbl_rodium_color` (`rodium_color_id`, `rodium_color_name`) VALUES
(2, 'white');

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
  `sub_cat_name` varchar(50) NOT NULL,
  `sub_cat_img` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_sub_cat`
--

INSERT INTO `tbl_sub_cat` (`sub_cat_id`, `main_cat_id`, `sub_cat_name`, `sub_cat_img`) VALUES
(1, 1, 'Indigo', ''),
(5, 1, 'black', ''),
(7, 2, 'white', ''),
(9, 6, 'test1', ''),
(12, 6, 'Test3', ''),
(13, 6, 'Test3', ''),
(14, 6, 'Test33', ''),
(15, 6, 'Test44444', ''),
(16, 6, 'test1111', '');

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
(1, 'Indigo1');

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
-- Indexes for table `tbl_banner`
--
ALTER TABLE `tbl_banner`
  ADD PRIMARY KEY (`banner_id`);

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
-- Indexes for table `tbl_metal_color`
--
ALTER TABLE `tbl_metal_color`
  ADD PRIMARY KEY (`metal_color_id`),
  ADD KEY `INDEX` (`metal_color_id`);

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
  ADD KEY `shape_id` (`shape_id`),
  ADD KEY `purity_id` (`purity_id`),
  ADD KEY `brand_id` (`brand_id`),
  ADD KEY `INDEX2` (`sub_cat_id`,`tone_id`,`purity_id`,`shape_id`,`brand_id`,`collection_id`,`pro_type_id`) USING BTREE,
  ADD KEY `INDEX3` (`metal_color_id`,`rodium_color_id`) USING BTREE,
  ADD KEY `rodium_color_id` (`rodium_color_id`);

--
-- Indexes for table `tbl_product_imgs`
--
ALTER TABLE `tbl_product_imgs`
  ADD PRIMARY KEY (`img_id`),
  ADD KEY `INDEX2` (`product_id`);

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
-- Indexes for table `tbl_rodium_color`
--
ALTER TABLE `tbl_rodium_color`
  ADD PRIMARY KEY (`rodium_color_id`),
  ADD KEY `INDEX` (`rodium_color_id`);

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
-- AUTO_INCREMENT for table `tbl_banner`
--
ALTER TABLE `tbl_banner`
  MODIFY `banner_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `tbl_brand`
--
ALTER TABLE `tbl_brand`
  MODIFY `brand_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `tbl_cart`
--
ALTER TABLE `tbl_cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `tbl_collection`
--
ALTER TABLE `tbl_collection`
  MODIFY `collection_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tbl_color`
--
ALTER TABLE `tbl_color`
  MODIFY `color_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `tbl_main_cat`
--
ALTER TABLE `tbl_main_cat`
  MODIFY `main_cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tbl_metal_color`
--
ALTER TABLE `tbl_metal_color`
  MODIFY `metal_color_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

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
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `tbl_product_imgs`
--
ALTER TABLE `tbl_product_imgs`
  MODIFY `img_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

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
-- AUTO_INCREMENT for table `tbl_rodium_color`
--
ALTER TABLE `tbl_rodium_color`
  MODIFY `rodium_color_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tbl_shape`
--
ALTER TABLE `tbl_shape`
  MODIFY `shape_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tbl_sub_cat`
--
ALTER TABLE `tbl_sub_cat`
  MODIFY `sub_cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `tbl_tone`
--
ALTER TABLE `tbl_tone`
  MODIFY `tone_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

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
  ADD CONSTRAINT `tbl_product_ibfk_10` FOREIGN KEY (`metal_color_id`) REFERENCES `tbl_metal_color` (`metal_color_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_2` FOREIGN KEY (`tone_id`) REFERENCES `tbl_tone` (`tone_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_3` FOREIGN KEY (`collection_id`) REFERENCES `tbl_collection` (`collection_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_4` FOREIGN KEY (`pro_type_id`) REFERENCES `tbl_pro_type` (`pro_type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_6` FOREIGN KEY (`shape_id`) REFERENCES `tbl_shape` (`shape_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_7` FOREIGN KEY (`purity_id`) REFERENCES `tbl_purity` (`purity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_8` FOREIGN KEY (`brand_id`) REFERENCES `tbl_brand` (`brand_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tbl_product_ibfk_9` FOREIGN KEY (`rodium_color_id`) REFERENCES `tbl_rodium_color` (`rodium_color_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_product_imgs`
--
ALTER TABLE `tbl_product_imgs`
  ADD CONSTRAINT `tbl_product_imgs_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `tbl_product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_sub_cat`
--
ALTER TABLE `tbl_sub_cat`
  ADD CONSTRAINT `tbl_sub_cat_ibfk_1` FOREIGN KEY (`main_cat_id`) REFERENCES `tbl_main_cat` (`main_cat_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
