-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ecommerce
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `seller_id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `sales_price` smallint unsigned NOT NULL,
  `stock` smallint unsigned NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`),
  KEY `fk_product_category_category_id_idx` (`category_id`),
  KEY `fk_product_seller_id_idx` (`seller_id`),
  CONSTRAINT `fk_product_category_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_product_seller_id` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`seller_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100037 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (3,1,2,'물품',10,1650,'2025-03-05 08:17:09','2025-03-14 10:27:36',0),(4,1,2,'물품',30000,200,'2025-03-05 08:17:12','2025-03-12 15:03:36',0),(5,1,2,'물품',30000,200,'2025-03-05 08:17:12','2025-03-05 08:17:12',0),(6,1,2,'물품',30000,200,'2025-03-05 08:17:12','2025-03-05 08:17:12',0),(7,1,2,'물품',30000,200,'2025-03-05 08:17:13','2025-03-05 08:17:13',0),(8,1,2,'물품',30000,200,'2025-03-05 08:17:13','2025-03-05 08:17:13',0),(9,2,3,'fewsfsdfwe',21342,1234,'2025-03-07 11:52:36','2025-03-07 11:52:36',0),(10,2,3,'fewsfsdfwe',0,1234,'2025-03-07 11:53:00','2025-03-07 11:55:01',1),(11,3,3,'gregre',0,0,'2025-03-07 11:53:39','2025-03-07 11:58:37',0),(100014,100016,100020,'1',10,1000,'2025-03-12 14:36:37','2025-03-12 15:03:36',0),(100016,100018,100022,'1',10,1000,'2025-03-13 08:13:35','2025-03-13 08:13:35',0),(100017,100019,100023,'1',10,1001,'2025-03-13 08:14:14','2025-03-13 17:09:24',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-14 17:28:36
