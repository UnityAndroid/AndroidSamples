-- phpMyAdmin SQL Dump
-- version 4.9.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 14, 2020 at 03:28 AM
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
-- Database: `recrrygy_lyrically`
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

--
-- Dumping data for table `app_info`
--

INSERT INTO `app_info` (`id`, `message`, `status`, `url`, `version_code`, `appid`, `banner`, `inter`, `reward`, `native`, `fbanner`, `finter`, `fnative`, `gstatus`, `fstatus`, `g2status`, `f2status`) VALUES
(1, 'demo', 1, 'https://play.google.com/store/apps/details?id=lyrically.photovideomaker.particl.ly.musicallybeat', 0, '0', '0', '0', '0', '0', '', '', '', 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `bitparticle`
--

CREATE TABLE `bitparticle` (
  `id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `theme_name` text NOT NULL,
  `prefab_name` text NOT NULL,
  `theme_path` text NOT NULL,
  `theme_url` text NOT NULL,
  `thumb_path` text NOT NULL,
  `thumb_url` text NOT NULL,
  `particle_path` text NOT NULL,
  `particle_url` text NOT NULL,
  `position` int(11) NOT NULL
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
-- Table structure for table `particle`
--

CREATE TABLE `particle` (
  `id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `theme_name` text NOT NULL,
  `prefab_name` text NOT NULL,
  `theme_path` text NOT NULL,
  `theme_url` text NOT NULL,
  `thumb_path` text NOT NULL,
  `thumb_url` text NOT NULL,
  `particle_path` text NOT NULL,
  `particle_url` text NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `particle_category`
--

CREATE TABLE `particle_category` (
  `category_id` int(11) NOT NULL,
  `category_name` text NOT NULL,
  `icon_path` text NOT NULL,
  `icon_url` text NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sortparticle`
--

CREATE TABLE `sortparticle` (
  `id` int(11) NOT NULL,
  `particle_id` int(11) NOT NULL,
  `theme_name` text NOT NULL,
  `position` int(11) NOT NULL
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
  `position` int(11) NOT NULL,
  `lyrics` text NOT NULL
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
  `Theme_Id` int(11) NOT NULL,
  `Theme_Name` text NOT NULL,
  `Thumnail_Path` text NOT NULL,
  `Thumnail_Url` text NOT NULL,
  `SoundName` text NOT NULL,
  `SoundFile` text NOT NULL,
  `sound_size` text NOT NULL,
  `GameobjectName` int(11) NOT NULL,
  `Is_Preimum` int(11) NOT NULL,
  `Status` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `lyrics` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

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
-- Indexes for table `bitparticle`
--
ALTER TABLE `bitparticle`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notify`
--
ALTER TABLE `notify`
  ADD UNIQUE KEY `PRIM` (`id`);

--
-- Indexes for table `particle`
--
ALTER TABLE `particle`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `particle_category`
--
ALTER TABLE `particle_category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `sortparticle`
--
ALTER TABLE `sortparticle`
  ADD PRIMARY KEY (`id`);

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
-- AUTO_INCREMENT for table `bitparticle`
--
ALTER TABLE `bitparticle`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notify`
--
ALTER TABLE `notify`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `particle`
--
ALTER TABLE `particle`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `particle_category`
--
ALTER TABLE `particle_category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sortparticle`
--
ALTER TABLE `sortparticle`
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
-- AUTO_INCREMENT for table `video_category`
--
ALTER TABLE `video_category`
  MODIFY `Cat_Id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
