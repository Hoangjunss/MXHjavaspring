-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th7 21, 2024 lúc 04:23 PM
-- Phiên bản máy phục vụ: 10.4.28-MariaDB
-- Phiên bản PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `mxh`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `about`
--

CREATE TABLE `about` (
  `id_about` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `about`
--

INSERT INTO `about` (`id_about`, `name`) VALUES
(1, 'NickName'),
(2, 'Address'),
(3, 'Genor');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `comment`
--

CREATE TABLE `comment` (
  `id_comment` bigint(20) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `user_send_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `image`
--

CREATE TABLE `image` (
  `id` bigint(20) NOT NULL,
  `url_image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `interact`
--

CREATE TABLE `interact` (
  `id_interact` bigint(20) NOT NULL,
  `interact_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `interact`
--

INSERT INTO `interact` (`id_interact`, `interact_type`) VALUES
(1, 'like'),
(2, 'love'),
(3, 'haha'),
(4, 'sad'),
(5, 'angry');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `interaction`
--

CREATE TABLE `interaction` (
  `id_interaction` bigint(20) NOT NULL,
  `id_interact` bigint(20) DEFAULT NULL,
  `id_post` bigint(20) DEFAULT NULL,
  `id_user` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message`
--

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `is_seen` bit(1) NOT NULL,
  `relationship_id` bigint(20) NOT NULL,
  `from_user_id` bigint(20) NOT NULL,
  `to_user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notification`
--

CREATE TABLE `notification` (
  `id_notification` bigint(20) NOT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `is_checked` bit(1) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `id_user` bigint(20) DEFAULT NULL,
  `id_user_send` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `post`
--

CREATE TABLE `post` (
  `id_post` bigint(20) NOT NULL,
  `content` text DEFAULT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `update_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `post_comment`
--

CREATE TABLE `post_comment` (
  `id_post` bigint(20) NOT NULL,
  `id_comment` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `post_image`
--

CREATE TABLE `post_image` (
  `id` bigint(20) DEFAULT NULL,
  `id_post` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `post_user`
--

CREATE TABLE `post_user` (
  `id_user` bigint(20) DEFAULT NULL,
  `id_post` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `relationship`
--

CREATE TABLE `relationship` (
  `id` bigint(20) NOT NULL,
  `id_status` bigint(20) DEFAULT NULL,
  `user_one_id` bigint(20) NOT NULL,
  `user_two_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `status_relationship`
--

CREATE TABLE `status_relationship` (
  `id_status` bigint(20) NOT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `status_relationship`
--

INSERT INTO `status_relationship` (`id_status`, `status`) VALUES
(1, 'nguoila'),
(2, 'dagui'),
(3, 'banbe');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `test`
--

CREATE TABLE `test` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id_user` bigint(20) NOT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users_image`
--

CREATE TABLE `users_image` (
  `id` bigint(20) DEFAULT NULL,
  `id_user` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users_post`
--

CREATE TABLE `users_post` (
  `id_user` bigint(20) NOT NULL,
  `id_post` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user_about`
--

CREATE TABLE `user_about` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_about` bigint(20) DEFAULT NULL,
  `id_user` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `verifycation_token`
--

CREATE TABLE `verifycation_token` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `set_expiry_date` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `about`
--
ALTER TABLE `about`
  ADD PRIMARY KEY (`id_about`);

--
-- Chỉ mục cho bảng `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id_comment`),
  ADD KEY `FKdiccckdmkxlcvhyssijk3vnxq` (`user_send_id`);

--
-- Chỉ mục cho bảng `image`
--
ALTER TABLE `image`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `interact`
--
ALTER TABLE `interact`
  ADD PRIMARY KEY (`id_interact`);

--
-- Chỉ mục cho bảng `interaction`
--
ALTER TABLE `interaction`
  ADD PRIMARY KEY (`id_interaction`),
  ADD KEY `FK50rss0h4kwit59hy7jjt6wf4a` (`id_interact`),
  ADD KEY `FKtckdct4jk2tos7jg92hbh8it0` (`id_post`),
  ADD KEY `FK25bk2mf7ue4rctidkaaptrx1c` (`id_user`);

--
-- Chỉ mục cho bảng `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKcgdq20gga571c8npc70whvcgi` (`relationship_id`),
  ADD KEY `FK3nju8asf4v72h0d7g6vgtx7p2` (`from_user_id`),
  ADD KEY `FKgm8awic1hpa2cgr7pv7j8yn0` (`to_user_id`);

--
-- Chỉ mục cho bảng `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id_notification`),
  ADD KEY `FKjsqpq32j3cp7sbi81on7xo3jg` (`id_user`),
  ADD KEY `FKjt0738ekuctil3n3kmnbdxlwt` (`id_user_send`);

--
-- Chỉ mục cho bảng `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`id_post`);

--
-- Chỉ mục cho bảng `post_comment`
--
ALTER TABLE `post_comment`
  ADD UNIQUE KEY `UKn8em7kp9sh8nsqy0weuvjv2y5` (`id_comment`),
  ADD KEY `FKrci98mqckdb76m0epwupaty53` (`id_post`);

--
-- Chỉ mục cho bảng `post_image`
--
ALTER TABLE `post_image`
  ADD PRIMARY KEY (`id_post`),
  ADD UNIQUE KEY `UKgw91nwinbi1olrsihunog7b3c` (`id`);

--
-- Chỉ mục cho bảng `post_user`
--
ALTER TABLE `post_user`
  ADD PRIMARY KEY (`id_post`),
  ADD KEY `FKahxus4xr3xro6lryh63drepcv` (`id_user`);

--
-- Chỉ mục cho bảng `relationship`
--
ALTER TABLE `relationship`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjy10vnfjonfcbv91mrkbyk3pu` (`id_status`),
  ADD KEY `FKqrnyn7dfug7p6lsnibx398k9y` (`user_one_id`),
  ADD KEY `FKgod8uv3xxb87xgmiof1ku4fex` (`user_two_id`);

--
-- Chỉ mục cho bảng `status_relationship`
--
ALTER TABLE `status_relationship`
  ADD PRIMARY KEY (`id_status`);

--
-- Chỉ mục cho bảng `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- Chỉ mục cho bảng `users_image`
--
ALTER TABLE `users_image`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `UKf9epht1dqjy1vc7t9gf5jil8u` (`id`);

--
-- Chỉ mục cho bảng `users_post`
--
ALTER TABLE `users_post`
  ADD UNIQUE KEY `UKb0tqegvvhhiiqettnarn9e139` (`id_post`),
  ADD KEY `FKb5oh0kk3lncbyim8xowgfj360` (`id_user`);

--
-- Chỉ mục cho bảng `user_about`
--
ALTER TABLE `user_about`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKq6eo7uilxm6md59arqmxvup61` (`id_about`),
  ADD KEY `FKquaksijagocfodn4psy4wpi3v` (`id_user`);

--
-- Chỉ mục cho bảng `verifycation_token`
--
ALTER TABLE `verifycation_token`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `relationship`
--
ALTER TABLE `relationship`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `test`
--
ALTER TABLE `test`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `FKdiccckdmkxlcvhyssijk3vnxq` FOREIGN KEY (`user_send_id`) REFERENCES `user` (`id_user`);

--
-- Các ràng buộc cho bảng `interaction`
--
ALTER TABLE `interaction`
  ADD CONSTRAINT `FK25bk2mf7ue4rctidkaaptrx1c` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `FK50rss0h4kwit59hy7jjt6wf4a` FOREIGN KEY (`id_interact`) REFERENCES `interact` (`id_interact`),
  ADD CONSTRAINT `FKtckdct4jk2tos7jg92hbh8it0` FOREIGN KEY (`id_post`) REFERENCES `post` (`id_post`);

--
-- Các ràng buộc cho bảng `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FK3nju8asf4v72h0d7g6vgtx7p2` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `FKcgdq20gga571c8npc70whvcgi` FOREIGN KEY (`relationship_id`) REFERENCES `relationship` (`id`),
  ADD CONSTRAINT `FKgm8awic1hpa2cgr7pv7j8yn0` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id_user`);

--
-- Các ràng buộc cho bảng `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `FKjsqpq32j3cp7sbi81on7xo3jg` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `FKjt0738ekuctil3n3kmnbdxlwt` FOREIGN KEY (`id_user_send`) REFERENCES `user` (`id_user`);

--
-- Các ràng buộc cho bảng `post_comment`
--
ALTER TABLE `post_comment`
  ADD CONSTRAINT `FKrci98mqckdb76m0epwupaty53` FOREIGN KEY (`id_post`) REFERENCES `post` (`id_post`),
  ADD CONSTRAINT `FKtjdb5cp14jfdc24yu173ca5o0` FOREIGN KEY (`id_comment`) REFERENCES `comment` (`id_comment`);

--
-- Các ràng buộc cho bảng `post_image`
--
ALTER TABLE `post_image`
  ADD CONSTRAINT `FKamr6mitc2nv4hdxclthf4ovvm` FOREIGN KEY (`id`) REFERENCES `image` (`id`),
  ADD CONSTRAINT `FKdmdcvgnjtqvw0jdnsp9dysu8r` FOREIGN KEY (`id_post`) REFERENCES `post` (`id_post`);

--
-- Các ràng buộc cho bảng `post_user`
--
ALTER TABLE `post_user`
  ADD CONSTRAINT `FKahxus4xr3xro6lryh63drepcv` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `FKrehvjr0ix4njqs7hmpraspxmn` FOREIGN KEY (`id_post`) REFERENCES `post` (`id_post`);

--
-- Các ràng buộc cho bảng `relationship`
--
ALTER TABLE `relationship`
  ADD CONSTRAINT `FKgod8uv3xxb87xgmiof1ku4fex` FOREIGN KEY (`user_two_id`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `FKjy10vnfjonfcbv91mrkbyk3pu` FOREIGN KEY (`id_status`) REFERENCES `status_relationship` (`id_status`),
  ADD CONSTRAINT `FKqrnyn7dfug7p6lsnibx398k9y` FOREIGN KEY (`user_one_id`) REFERENCES `user` (`id_user`);

--
-- Các ràng buộc cho bảng `users_image`
--
ALTER TABLE `users_image`
  ADD CONSTRAINT `FK48u4f4v1ff8anjgmd580otd2s` FOREIGN KEY (`id`) REFERENCES `image` (`id`),
  ADD CONSTRAINT `FKnmstml8qp0x2uhra17hd5wcqd` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

--
-- Các ràng buộc cho bảng `users_post`
--
ALTER TABLE `users_post`
  ADD CONSTRAINT `FKb5oh0kk3lncbyim8xowgfj360` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `FKh21sebr01qlorth0cnofxiqwg` FOREIGN KEY (`id_post`) REFERENCES `post` (`id_post`);

--
-- Các ràng buộc cho bảng `user_about`
--
ALTER TABLE `user_about`
  ADD CONSTRAINT `FKq6eo7uilxm6md59arqmxvup61` FOREIGN KEY (`id_about`) REFERENCES `about` (`id_about`),
  ADD CONSTRAINT `FKquaksijagocfodn4psy4wpi3v` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
