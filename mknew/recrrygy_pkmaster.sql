-- phpMyAdmin SQL Dump
-- version 4.9.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 18, 2021 at 07:04 AM
-- Server version: 5.7.23-23
-- PHP Version: 7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `recrrygy_pkmaster`
--

-- --------------------------------------------------------

--
-- Table structure for table `apps`
--

CREATE TABLE `apps` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `package` text NOT NULL,
  `url` text NOT NULL,
  `logo` text NOT NULL,
  `logo_path` text NOT NULL,
  `status` int(11) NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `app_info`
--

CREATE TABLE `app_info` (
  `id` int(11) NOT NULL,
  `message` text NOT NULL,
  `status` int(11) NOT NULL,
  `url` text NOT NULL,
  `version_code` int(11) NOT NULL,
  `appid` text NOT NULL,
  `banner` text NOT NULL,
  `inter` text NOT NULL,
  `reward` text NOT NULL,
  `native` text NOT NULL,
  `fbanner` text NOT NULL,
  `finter` text NOT NULL,
  `fnative` text NOT NULL,
  `gstatus` int(11) NOT NULL,
  `fstatus` int(11) NOT NULL,
  `g2status` int(11) NOT NULL,
  `f2status` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `notify`
--

CREATE TABLE `notify` (
  `id` int(11) NOT NULL,
  `title` text NOT NULL,
  `message` text NOT NULL,
  `ntime` text NOT NULL,
  `image_path` text NOT NULL,
  `image_url` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sounds`
--

CREATE TABLE `sounds` (
  `id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `sound_name` text NOT NULL,
  `sound_url` text NOT NULL,
  `sound_full_url` text NOT NULL,
  `sound_size` text NOT NULL,
  `status` int(11) NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sound_category`
--

CREATE TABLE `sound_category` (
  `category_id` int(11) NOT NULL,
  `category_name` text NOT NULL,
  `status` int(11) NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(100) NOT NULL,
  `password` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `android_id` text NOT NULL,
  `token` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `videos`
--

CREATE TABLE `videos` (
  `Id` int(11) NOT NULL,
  `Cat_Id` int(11) NOT NULL,
  `Theme_Name` text NOT NULL,
  `Thumnail_Path` text NOT NULL,
  `Thumnail_Url` text NOT NULL,
  `bundle_path` text NOT NULL,
  `bundle_url` text NOT NULL,
  `SoundName` text NOT NULL,
  `SoundFile` text NOT NULL,
  `sound_size` text NOT NULL,
  `GameobjectName` text NOT NULL,
  `Status` int(11) NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `videos_new`
--

CREATE TABLE `videos_new` (
  `new_id` int(11) NOT NULL,
  `Id` int(11) NOT NULL,
  `Cat_Id` int(11) NOT NULL,
  `Theme_Name` text COLLATE utf8_unicode_ci NOT NULL,
  `Thumnail_Path` text COLLATE utf8_unicode_ci NOT NULL,
  `Thumnail_Url` text COLLATE utf8_unicode_ci NOT NULL,
  `bundle_path` text COLLATE utf8_unicode_ci NOT NULL,
  `bundle_url` text COLLATE utf8_unicode_ci NOT NULL,
  `SoundName` text COLLATE utf8_unicode_ci NOT NULL,
  `SoundFile` text COLLATE utf8_unicode_ci NOT NULL,
  `sound_size` text COLLATE utf8_unicode_ci NOT NULL,
  `GameobjectName` text COLLATE utf8_unicode_ci NOT NULL,
  `Status` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `new_position` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `video_category`
--

CREATE TABLE `video_category` (
  `Cat_Id` int(11) NOT NULL,
  `Category_Name` text NOT NULL,
  `Icon_path` text NOT NULL,
  `Icon_url` text NOT NULL,
  `status` int(11) NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `apps`
--
ALTER TABLE `apps`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `app_info`
--
ALTER TABLE `app_info`
  ADD UNIQUE KEY `PRI` (`id`);

--
-- Indexes for table `notify`
--
ALTER TABLE `notify`
  ADD UNIQUE KEY `PRIM` (`id`);

--
-- Indexes for table `sounds`
--
ALTER TABLE `sounds`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sound_category`
--
ALTER TABLE `sound_category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `videos`
--
ALTER TABLE `videos`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `videos_new`
--
ALTER TABLE `videos_new`
  ADD PRIMARY KEY (`new_id`);

--
-- Indexes for table `video_category`
--
ALTER TABLE `video_category`
  ADD PRIMARY KEY (`Cat_Id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `apps`
--
ALTER TABLE `apps`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notify`
--
ALTER TABLE `notify`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sounds`
--
ALTER TABLE `sounds`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sound_category`
--
ALTER TABLE `sound_category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `videos`
--
ALTER TABLE `videos`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `videos_new`
--
ALTER TABLE `videos_new`
  MODIFY `new_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `video_category`
--
ALTER TABLE `video_category`
  MODIFY `Cat_Id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
