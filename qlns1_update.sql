-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: qlns1
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chi_tiet_hoa_don`
--

DROP TABLE IF EXISTS `chi_tiet_hoa_don`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_hoa_don` (
  `don_gia` decimal(38,2) DEFAULT NULL,
  `hoa_don_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `sach_id` int DEFAULT NULL,
  `so_luong` int NOT NULL,
  `hinh_anh` varchar(255) DEFAULT NULL,
  `ten_sach` varchar(255) DEFAULT NULL,
  `thanh_tien` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8u22ain8he9p3mgvuhkw82jkq` (`hoa_don_id`),
  KEY `FK6nnc5g25h2kfb8p3crlem96ni` (`sach_id`),
  CONSTRAINT `FK6nnc5g25h2kfb8p3crlem96ni` FOREIGN KEY (`sach_id`) REFERENCES `sach` (`id`),
  CONSTRAINT `FK8u22ain8he9p3mgvuhkw82jkq` FOREIGN KEY (`hoa_don_id`) REFERENCES `hoa_don` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_hoa_don`
--

LOCK TABLES `chi_tiet_hoa_don` WRITE;
/*!40000 ALTER TABLE `chi_tiet_hoa_don` DISABLE KEYS */;
/*!40000 ALTER TABLE `chi_tiet_hoa_don` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_phieu_giu`
--

DROP TABLE IF EXISTS `chi_tiet_phieu_giu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_phieu_giu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `so_luong` int NOT NULL,
  `phieu_giu_id` int DEFAULT NULL,
  `sach_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKry314u2w7f58e1rqth56qr2ba` (`phieu_giu_id`),
  KEY `FKryxx4gsw9w0noklycajbwxu54` (`sach_id`),
  CONSTRAINT `FKry314u2w7f58e1rqth56qr2ba` FOREIGN KEY (`phieu_giu_id`) REFERENCES `phieu_dat_giu_sach` (`id`),
  CONSTRAINT `FKryxx4gsw9w0noklycajbwxu54` FOREIGN KEY (`sach_id`) REFERENCES `sach` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_phieu_giu`
--

LOCK TABLES `chi_tiet_phieu_giu` WRITE;
/*!40000 ALTER TABLE `chi_tiet_phieu_giu` DISABLE KEYS */;
/*!40000 ALTER TABLE `chi_tiet_phieu_giu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_phieu_nhap`
--

DROP TABLE IF EXISTS `chi_tiet_phieu_nhap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_phieu_nhap` (
  `gia_nhap` decimal(38,2) DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `phieu_nhap_id` int DEFAULT NULL,
  `sach_id` int DEFAULT NULL,
  `so_luong` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrwv42blu18960otrpapu1xyet` (`phieu_nhap_id`),
  KEY `FKava81m0t695l5kls3p5ht0o0p` (`sach_id`),
  CONSTRAINT `FKava81m0t695l5kls3p5ht0o0p` FOREIGN KEY (`sach_id`) REFERENCES `sach` (`id`),
  CONSTRAINT `FKrwv42blu18960otrpapu1xyet` FOREIGN KEY (`phieu_nhap_id`) REFERENCES `phieu_nhap` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_phieu_nhap`
--

LOCK TABLES `chi_tiet_phieu_nhap` WRITE;
/*!40000 ALTER TABLE `chi_tiet_phieu_nhap` DISABLE KEYS */;
/*!40000 ALTER TABLE `chi_tiet_phieu_nhap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chuc_nang`
--

DROP TABLE IF EXISTS `chuc_nang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chuc_nang` (
  `ma_chuc_nang` int NOT NULL AUTO_INCREMENT,
  `ten_chuc_nang` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ma_chuc_nang`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chuc_nang`
--

LOCK TABLES `chuc_nang` WRITE;
/*!40000 ALTER TABLE `chuc_nang` DISABLE KEYS */;
INSERT INTO `chuc_nang` VALUES (1,'QUAN_LY_NGUOI_DUNG'),(2,'QUAN_LY_SACH'),(3,'QUAN_LY_HOA_DON'),(4,'QUAN_LY_KHACH_HANG'),(5,'QUAN_LY_PHIEU_NHAP'),(6,'QUAN_LY_PHIEU_GIU'),(7,'QUAN_LY_NHA_CUNG_CAP'),(8,'QUAN_LY_NHA_XUAT_BAN'),(9,'QUAN_LY_TAC_GIA'),(10,'QUAN_LY_THE_LOAI'),(11,'XEM_BAO_CAO');
/*!40000 ALTER TABLE `chuc_nang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoa_don`
--

DROP TABLE IF EXISTS `hoa_don`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoa_don` (
  `id` int NOT NULL AUTO_INCREMENT,
  `khach_hang_id` int DEFAULT NULL,
  `nhan_vien_id` int DEFAULT NULL,
  `tong_tien` decimal(38,2) DEFAULT NULL,
  `ngay_ban` datetime(6) DEFAULT NULL,
  `ma_hoa_don` varchar(255) DEFAULT NULL,
  `trang_thai` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfvowmias4ycehn19gyv8t5dys` (`khach_hang_id`),
  KEY `FKr03ok7jbei2jgb0w4to186rg4` (`nhan_vien_id`),
  CONSTRAINT `FKfvowmias4ycehn19gyv8t5dys` FOREIGN KEY (`khach_hang_id`) REFERENCES `khach_hang` (`id`),
  CONSTRAINT `FKr03ok7jbei2jgb0w4to186rg4` FOREIGN KEY (`nhan_vien_id`) REFERENCES `nguoi_dung` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoa_don`
--

LOCK TABLES `hoa_don` WRITE;
/*!40000 ALTER TABLE `hoa_don` DISABLE KEYS */;
/*!40000 ALTER TABLE `hoa_don` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoa_don_danh_sach_chi_tiet`
--

DROP TABLE IF EXISTS `hoa_don_danh_sach_chi_tiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoa_don_danh_sach_chi_tiet` (
  `danh_sach_chi_tiet_id` int NOT NULL,
  `hoa_don_id` int NOT NULL,
  UNIQUE KEY `UK91xxr69mchbl2rc5bpdggt4gy` (`danh_sach_chi_tiet_id`),
  KEY `FK9357v1ynsuh03kjbadb9pmcv3` (`hoa_don_id`),
  CONSTRAINT `FK9357v1ynsuh03kjbadb9pmcv3` FOREIGN KEY (`hoa_don_id`) REFERENCES `hoa_don` (`id`),
  CONSTRAINT `FKpotce1n8c2g2sl6mji5k8otk9` FOREIGN KEY (`danh_sach_chi_tiet_id`) REFERENCES `chi_tiet_hoa_don` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoa_don_danh_sach_chi_tiet`
--

LOCK TABLES `hoa_don_danh_sach_chi_tiet` WRITE;
/*!40000 ALTER TABLE `hoa_don_danh_sach_chi_tiet` DISABLE KEYS */;
/*!40000 ALTER TABLE `hoa_don_danh_sach_chi_tiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khach_hang`
--

DROP TABLE IF EXISTS `khach_hang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khach_hang` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `ho_ten` varchar(255) DEFAULT NULL,
  `sdt` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `diem_tich_luy` int DEFAULT '0',
  `hang_thanh_vien` varchar(50) DEFAULT '??ng',
  `trang_thai` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khach_hang`
--

LOCK TABLES `khach_hang` WRITE;
/*!40000 ALTER TABLE `khach_hang` DISABLE KEYS */;
/*!40000 ALTER TABLE `khach_hang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nguoi_dung`
--

DROP TABLE IF EXISTS `nguoi_dung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nguoi_dung` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ten_nhom` int DEFAULT NULL,
  `ho_ten` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `sdt` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `ca_lam_viec` varchar(100) DEFAULT NULL,
  `dia_chi` varchar(255) DEFAULT NULL,
  `ngay_vao_lam` varchar(50) DEFAULT NULL,
  `luong_co_ban` bigint DEFAULT NULL,
  `ghi_chu` text,
  PRIMARY KEY (`id`),
  KEY `FKpgps79lcn7imp5cdisg2v1wqp` (`ten_nhom`),
  CONSTRAINT `FKpgps79lcn7imp5cdisg2v1wqp` FOREIGN KEY (`ten_nhom`) REFERENCES `nhom_nguoi_dung` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nguoi_dung`
--

LOCK TABLES `nguoi_dung` WRITE;
/*!40000 ALTER TABLE `nguoi_dung` DISABLE KEYS */;
INSERT INTO `nguoi_dung` VALUES (1,1,'Admin','$2a$10$xR22tfHwe4dxhP1uCJoNh.DGHTtpC5l3u3jFUsgrIXtyouabCNkCe',NULL,'0814750791','admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,2,'Nhan vien 01','$2a$10$N33eW5IXDDSdSJtqkCGf0O6xOrp2uxU7v85JPau0og6TW.ykCTlBi',NULL,'0900000001','staff01',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `nguoi_dung` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nha_cung_cap`
--

DROP TABLE IF EXISTS `nha_cung_cap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nha_cung_cap` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ten_ncc` varchar(150) NOT NULL,
  `sdt` varchar(255) DEFAULT NULL,
  `dia_chi` varchar(255) DEFAULT NULL,
  `tenncc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nha_cung_cap`
--

LOCK TABLES `nha_cung_cap` WRITE;
/*!40000 ALTER TABLE `nha_cung_cap` DISABLE KEYS */;
INSERT INTO `nha_cung_cap` VALUES (1,'Công ty Sách Miền Nam','0909123456',NULL,NULL),(2,'Fahasa','0901234567',NULL,NULL);
/*!40000 ALTER TABLE `nha_cung_cap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nha_xuat_ban`
--

DROP TABLE IF EXISTS `nha_xuat_ban`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nha_xuat_ban` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ten_nxb` varchar(150) NOT NULL,
  `dia_chi` varchar(255) DEFAULT NULL,
  `tennxb` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nha_xuat_ban`
--

LOCK TABLES `nha_xuat_ban` WRITE;
/*!40000 ALTER TABLE `nha_xuat_ban` DISABLE KEYS */;
INSERT INTO `nha_xuat_ban` VALUES (1,'NXB Trẻ',NULL,NULL),(2,'NXB Kim Đồng',NULL,NULL),(3,'NXB Tổng Hợp',NULL,NULL);
/*!40000 ALTER TABLE `nha_xuat_ban` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhan_vien`
--

DROP TABLE IF EXISTS `nhan_vien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhan_vien` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ho_ten` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `vai_tro` varchar(255) DEFAULT NULL,
  `sdt` varchar(255) DEFAULT NULL,
  `trang_thai` tinyint(1) DEFAULT '1',
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ca_lam_viec` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhan_vien`
--

LOCK TABLES `nhan_vien` WRITE;
/*!40000 ALTER TABLE `nhan_vien` DISABLE KEYS */;
INSERT INTO `nhan_vien` VALUES (103,'Thien','admin','$2a$10$Qi18Bhu7U9CGM0PvEEMppupe2yRNM62WjO8Z3FxqPBwBMsf7PRXam','ADMIN','0814750791',1,NULL,NULL,NULL),(104,'Nhan Vien A','staff','$2a$10$5YOs6bgtX504w0bUoFPjYeHeY8h9f/BGdYAPC0FVTT.sVTMBISd4K','STAFF','0909111222',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `nhan_vien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhan_vien_seq`
--

DROP TABLE IF EXISTS `nhan_vien_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhan_vien_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhan_vien_seq`
--

LOCK TABLES `nhan_vien_seq` WRITE;
/*!40000 ALTER TABLE `nhan_vien_seq` DISABLE KEYS */;
INSERT INTO `nhan_vien_seq` VALUES (201);
/*!40000 ALTER TABLE `nhan_vien_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhom_nguoi_dung`
--

DROP TABLE IF EXISTS `nhom_nguoi_dung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhom_nguoi_dung` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ten_nhom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhom_nguoi_dung`
--

LOCK TABLES `nhom_nguoi_dung` WRITE;
/*!40000 ALTER TABLE `nhom_nguoi_dung` DISABLE KEYS */;
INSERT INTO `nhom_nguoi_dung` VALUES (1,'ADMIN'),(2,'STAFF');
/*!40000 ALTER TABLE `nhom_nguoi_dung` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phan_quyen`
--

DROP TABLE IF EXISTS `phan_quyen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phan_quyen` (
  `ma_chuc_nang` int NOT NULL,
  `ma_nguoi_dung` int NOT NULL,
  PRIMARY KEY (`ma_chuc_nang`,`ma_nguoi_dung`),
  KEY `FKldgv3m7t3t70hhq2hjxfmrepo` (`ma_nguoi_dung`),
  CONSTRAINT `FKldgv3m7t3t70hhq2hjxfmrepo` FOREIGN KEY (`ma_nguoi_dung`) REFERENCES `nguoi_dung` (`id`),
  CONSTRAINT `FKnbc4rt31xunsp1rqbbej85gx` FOREIGN KEY (`ma_chuc_nang`) REFERENCES `chuc_nang` (`ma_chuc_nang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phan_quyen`
--

LOCK TABLES `phan_quyen` WRITE;
/*!40000 ALTER TABLE `phan_quyen` DISABLE KEYS */;
INSERT INTO `phan_quyen` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(1,3),(4,3),(5,3);
/*!40000 ALTER TABLE `phan_quyen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phieu_dat_giu_sach`
--

DROP TABLE IF EXISTS `phieu_dat_giu_sach`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phieu_dat_giu_sach` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `expired_at` datetime(6) DEFAULT NULL,
  `khach_hang_id` int NOT NULL,
  `trang_thai` enum('CANCELLED','CONFIRMED','EXPIRED','PENDING') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieu_dat_giu_sach`
--

LOCK TABLES `phieu_dat_giu_sach` WRITE;
/*!40000 ALTER TABLE `phieu_dat_giu_sach` DISABLE KEYS */;
/*!40000 ALTER TABLE `phieu_dat_giu_sach` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phieu_nhap`
--

DROP TABLE IF EXISTS `phieu_nhap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phieu_nhap` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nha_cung_cap_id` int DEFAULT NULL,
  `nhan_vien_id` int DEFAULT NULL,
  `tong_tien` decimal(38,2) DEFAULT NULL,
  `ngay_nhap` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5lmupwyqjjdup4ki1g1dq2cct` (`nha_cung_cap_id`),
  KEY `FK5faclpy1xoeqt24hpwnxpd4le` (`nhan_vien_id`),
  CONSTRAINT `FK5faclpy1xoeqt24hpwnxpd4le` FOREIGN KEY (`nhan_vien_id`) REFERENCES `nguoi_dung` (`id`),
  CONSTRAINT `FK5lmupwyqjjdup4ki1g1dq2cct` FOREIGN KEY (`nha_cung_cap_id`) REFERENCES `nha_cung_cap` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieu_nhap`
--

LOCK TABLES `phieu_nhap` WRITE;
/*!40000 ALTER TABLE `phieu_nhap` DISABLE KEYS */;
/*!40000 ALTER TABLE `phieu_nhap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phieu_nhap_danh_sach_chi_tiet`
--

DROP TABLE IF EXISTS `phieu_nhap_danh_sach_chi_tiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phieu_nhap_danh_sach_chi_tiet` (
  `danh_sach_chi_tiet_id` int NOT NULL,
  `phieu_nhap_id` int NOT NULL,
  UNIQUE KEY `UKqswys6vgqv82lijn9ov44v1v` (`danh_sach_chi_tiet_id`),
  KEY `FK2enke0t5lob2jks0w0rpccrdv` (`phieu_nhap_id`),
  CONSTRAINT `FK2enke0t5lob2jks0w0rpccrdv` FOREIGN KEY (`phieu_nhap_id`) REFERENCES `phieu_nhap` (`id`),
  CONSTRAINT `FKfhm9gqd1d3eb1i8q15tx3n8vk` FOREIGN KEY (`danh_sach_chi_tiet_id`) REFERENCES `chi_tiet_phieu_nhap` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieu_nhap_danh_sach_chi_tiet`
--

LOCK TABLES `phieu_nhap_danh_sach_chi_tiet` WRITE;
/*!40000 ALTER TABLE `phieu_nhap_danh_sach_chi_tiet` DISABLE KEYS */;
/*!40000 ALTER TABLE `phieu_nhap_danh_sach_chi_tiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refreshtoken`
--

DROP TABLE IF EXISTS `refreshtoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refreshtoken` (
  `revoked` bit(1) NOT NULL,
  `expiry_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(64) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refreshtoken`
--

LOCK TABLES `refreshtoken` WRITE;
/*!40000 ALTER TABLE `refreshtoken` DISABLE KEYS */;
INSERT INTO `refreshtoken` VALUES (_binary '\0','2026-06-02 07:25:53.386000',8,'EMknmC2pAOhUVPYeGP1noSXh5DqXxSCPHTnDdCynKoQ','admin');
/*!40000 ALTER TABLE `refreshtoken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sach`
--

DROP TABLE IF EXISTS `sach`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sach` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ten_sach` varchar(255) NOT NULL,
  `gia_ban` decimal(38,2) DEFAULT NULL,
  `so_luong_ton` int DEFAULT '0',
  `hinh_anh` varchar(255) DEFAULT NULL,
  `nam_xuat_ban` int DEFAULT NULL,
  `the_loai_id` int DEFAULT NULL,
  `nha_xuat_ban_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5p8sdg0mvyxuwduai1n8fmbgd` (`nha_xuat_ban_id`),
  KEY `FKfbd36vlmaloqjnaasyr4jp438` (`the_loai_id`),
  CONSTRAINT `FK5p8sdg0mvyxuwduai1n8fmbgd` FOREIGN KEY (`nha_xuat_ban_id`) REFERENCES `nha_xuat_ban` (`id`),
  CONSTRAINT `FKfbd36vlmaloqjnaasyr4jp438` FOREIGN KEY (`the_loai_id`) REFERENCES `the_loai` (`id`),
  CONSTRAINT `sach_ibfk_1` FOREIGN KEY (`the_loai_id`) REFERENCES `the_loai` (`id`),
  CONSTRAINT `sach_ibfk_2` FOREIGN KEY (`nha_xuat_ban_id`) REFERENCES `nha_xuat_ban` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sach`
--

LOCK TABLES `sach` WRITE;
/*!40000 ALTER TABLE `sach` DISABLE KEYS */;
INSERT INTO `sach` VALUES (1,'Mắt Biếc',85000.00,78,NULL,2020,1,1);
/*!40000 ALTER TABLE `sach` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sach_tac_gia`
--

DROP TABLE IF EXISTS `sach_tac_gia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sach_tac_gia` (
  `sach_id` int NOT NULL,
  `tac_gia_id` int NOT NULL,
  KEY `FK92k87r7rjd4r4a0690cefuk94` (`tac_gia_id`),
  KEY `FKfl0rp68p6n5stmodpl0w7mwwd` (`sach_id`),
  CONSTRAINT `FK92k87r7rjd4r4a0690cefuk94` FOREIGN KEY (`tac_gia_id`) REFERENCES `tac_gia` (`id`),
  CONSTRAINT `FKfl0rp68p6n5stmodpl0w7mwwd` FOREIGN KEY (`sach_id`) REFERENCES `sach` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sach_tac_gia`
--

LOCK TABLES `sach_tac_gia` WRITE;
/*!40000 ALTER TABLE `sach_tac_gia` DISABLE KEYS */;
/*!40000 ALTER TABLE `sach_tac_gia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tac_gia`
--

DROP TABLE IF EXISTS `tac_gia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tac_gia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ho_ten` varchar(255) DEFAULT NULL,
  `quoc_tich` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tac_gia`
--

LOCK TABLES `tac_gia` WRITE;
/*!40000 ALTER TABLE `tac_gia` DISABLE KEYS */;
INSERT INTO `tac_gia` VALUES (1,'Nguyễn Văn A','Việt Nam');
/*!40000 ALTER TABLE `tac_gia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `the_loai`
--

DROP TABLE IF EXISTS `the_loai`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `the_loai` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ten_the_loai` varchar(255) DEFAULT NULL,
  `mo_ta` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `the_loai`
--

LOCK TABLES `the_loai` WRITE;
/*!40000 ALTER TABLE `the_loai` DISABLE KEYS */;
INSERT INTO `the_loai` VALUES (1,'Văn học',NULL),(2,'Khoa học',NULL),(3,'Kỹ năng sống',NULL),(4,'Thiếu nhi',NULL);
/*!40000 ALTER TABLE `the_loai` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-31 15:14:25
