/*
 Navicat Premium Data Transfer

 Source Server         : Baymax
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : wk_school

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 02/02/2023 16:08:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ec_classes
-- ----------------------------
DROP TABLE IF EXISTS `ec_classes`;
CREATE TABLE `ec_classes`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '班级名称',
  `course_id` int(11) UNSIGNED NOT NULL COMMENT '课程id',
  `teacher_id` int(11) NOT NULL COMMENT '老师id:为了方便查找',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE,
  INDEX `teacher_id`(`teacher_id`) USING BTREE,
  CONSTRAINT `ec_classes_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `ec_courses` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ec_classes_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `es_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ec_classes
-- ----------------------------
INSERT INTO `ec_classes` VALUES (1, '计算机211', 1, 10, '2022-10-13 17:58:40', '2022-10-14 19:26:59');
INSERT INTO `ec_classes` VALUES (2, '大数据234', 1, 10, '2022-10-13 17:58:56', '2022-10-14 19:27:06');
INSERT INTO `ec_classes` VALUES (3, '科状技', 5, 10, '2022-10-14 22:32:40', '2022-10-14 22:38:39');
INSERT INTO `ec_classes` VALUES (4, '口响争展二', 2, 10, '2022-10-14 22:33:14', '2022-10-14 22:33:14');
INSERT INTO `ec_classes` VALUES (5, '口响争展二', 3, 10, '2022-10-14 22:33:29', '2022-10-14 22:33:29');
INSERT INTO `ec_classes` VALUES (6, '口响争展二', 5, 10, '2022-10-14 22:33:59', '2022-10-14 22:33:59');
INSERT INTO `ec_classes` VALUES (12, '计科211', 6, 10, '2022-10-16 16:42:40', '2022-12-03 20:27:59');
INSERT INTO `ec_classes` VALUES (13, '计算机', 9, 10, '2022-10-16 15:33:04', '2022-10-16 15:33:04');
INSERT INTO `ec_classes` VALUES (14, '+66', 9, 10, '2022-10-16 15:37:45', '2022-10-16 15:37:53');
INSERT INTO `ec_classes` VALUES (15, '机械班', 9, 10, '2022-12-03 20:02:06', '2022-12-03 20:28:03');
INSERT INTO `ec_classes` VALUES (17, '物流321', 6, 10, '2022-10-16 19:52:04', '2022-10-16 19:52:04');
INSERT INTO `ec_classes` VALUES (18, '毕业班', 7, 10, '2022-10-23 20:18:55', '2022-10-23 20:18:55');

-- ----------------------------
-- Table structure for ec_courses
-- ----------------------------
DROP TABLE IF EXISTS `ec_courses`;
CREATE TABLE `ec_courses`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '课程名称',
  `cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '课程封面',
  `introduce` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '课程介绍',
  `is_public` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否公开:0：不公开（邀请码）,1公开（搜索）',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '课程状态:0:正常，1：结课',
  `user_id` int(11) NOT NULL COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `ec_courses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `es_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '课程信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ec_courses
-- ----------------------------
INSERT INTO `ec_courses` VALUES (1, 'SpringCloud课程设计', 'https://pic.616pic.com/bg_w1180/00/01/57/CetpiQ3xVI.jpg!/both/561x313', 'hello hello ', 1, 1, 10, '2022-10-13 17:12:39', '2022-10-15 15:52:10');
INSERT INTO `ec_courses` VALUES (2, '点边对价管向日', 'https://pic.616pic.com/bg_w1180/00/01/57/CetpiQ3xVI.jpg!/both/561x313', 'sunt Lorem', 0, 0, 10, '2022-10-13 19:22:51', '2022-10-15 15:52:08');
INSERT INTO `ec_courses` VALUES (3, '格义又成单音务', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F8%2F57a2ffa73a41e.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1668473671&t=78c2227ae4dde72232ed04b099544b35', 'cillum Ut', 0, 1, 10, '2022-10-13 19:23:55', '2022-10-16 08:54:40');
INSERT INTO `ec_courses` VALUES (4, '修改测试哈哈哈', 'https://pic.616pic.com/bg_w1180/00/01/57/CetpiQ3xVI.jpg!/both/561x313', 'nulla esse Excepteur', 0, 1, 8, '2022-10-14 21:17:08', '2022-10-15 15:52:05');
INSERT INTO `ec_courses` VALUES (5, '修改测试哈哈哈', 'https://pic.616pic.com/bg_w1180/00/01/57/CetpiQ3xVI.jpg!/both/561x313', 'nulla esse Excepteur', 0, 1, 10, '2022-10-14 21:18:55', '2022-10-15 15:51:44');
INSERT INTO `ec_courses` VALUES (6, '基于Spring Vue的考试系统', 'https://pic.616pic.com/bg_w1180/00/01/57/CetpiQ3xVI.jpg!/both/561x313', '6666你好spring ', 1, 0, 10, '2022-10-15 18:37:41', '2022-10-15 18:37:41');
INSERT INTO `ec_courses` VALUES (7, '安科毕业论文答辩', 'https://pic.616pic.com/bg_w1180/00/00/15/JdzVtay3MN.jpg!/both/561x313', '66666666', 0, 0, 10, '2022-10-15 19:05:06', '2022-10-16 08:53:48');
INSERT INTO `ec_courses` VALUES (8, 'java 666', 'https://pic.616pic.com/bg_w1180/00/01/57/CetpiQ3xVI.jpg!/both/561x313', '22222', 1, 1, 10, '2022-10-15 19:05:50', '2022-10-15 19:05:50');
INSERT INTO `ec_courses` VALUES (9, 'Java高级基础', 'https://pic.616pic.com/bg_w1180/00/01/57/CetpiQ3xVI.jpg!/both/561x313', '111111', 0, 0, 10, '2022-10-16 08:52:51', '2022-10-16 08:52:51');

-- ----------------------------
-- Table structure for ec_join_class
-- ----------------------------
DROP TABLE IF EXISTS `ec_join_class`;
CREATE TABLE `ec_join_class`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `class_id` int(11) NOT NULL COMMENT '课程id',
  `student_id` int(11) NOT NULL COMMENT '学生id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`, `class_id`, `student_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  CONSTRAINT `ec_join_class_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `es_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ec_join_class_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `ec_classes` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 171 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ec_join_class
-- ----------------------------
INSERT INTO `ec_join_class` VALUES (71, 13, 10, '2022-12-03 20:32:33', '2022-12-03 20:36:08');
INSERT INTO `ec_join_class` VALUES (72, 14, 12, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (73, 13, 13, '2022-12-03 20:32:33', '2023-02-02 11:10:55');
INSERT INTO `ec_join_class` VALUES (74, 13, 14, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (75, 14, 15, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (76, 15, 16, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (77, 13, 17, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (78, 14, 18, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (79, 15, 19, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (80, 13, 20, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (81, 14, 21, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (82, 15, 22, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (83, 13, 23, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (84, 14, 24, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (85, 15, 25, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (86, 13, 26, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (87, 14, 27, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (88, 15, 28, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (89, 13, 29, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (90, 14, 30, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (91, 15, 31, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (92, 13, 32, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (93, 14, 33, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (94, 15, 34, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (95, 13, 35, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (96, 14, 36, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (97, 15, 37, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (98, 13, 38, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (99, 14, 39, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (100, 15, 40, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (101, 13, 41, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (102, 14, 42, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (103, 15, 43, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (104, 13, 44, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (105, 14, 45, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (106, 15, 46, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (107, 13, 47, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (108, 14, 48, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (109, 15, 49, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (110, 13, 50, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (111, 14, 51, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (112, 15, 52, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (113, 13, 53, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (114, 14, 54, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (115, 15, 55, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (116, 13, 56, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (117, 14, 57, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (118, 15, 58, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (119, 13, 59, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (120, 14, 60, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (121, 15, 61, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (122, 13, 62, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (123, 14, 63, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (124, 15, 64, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (125, 13, 65, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (126, 14, 66, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (127, 15, 67, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (128, 13, 68, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (129, 14, 69, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (130, 15, 70, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (131, 13, 71, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (132, 14, 72, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (133, 15, 73, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (134, 13, 74, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (135, 14, 75, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (136, 15, 76, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (137, 13, 77, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (138, 14, 78, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (139, 15, 79, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (140, 13, 80, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (141, 14, 81, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (142, 15, 82, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (143, 13, 83, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (144, 14, 84, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (145, 15, 85, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (146, 13, 86, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (147, 14, 87, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (148, 15, 88, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (149, 13, 89, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (150, 14, 90, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (151, 15, 91, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (152, 13, 92, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (153, 14, 93, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (154, 15, 94, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (155, 13, 95, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (156, 14, 96, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (157, 15, 97, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (158, 13, 98, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (159, 14, 99, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (160, 15, 100, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (161, 13, 101, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (162, 14, 102, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (163, 15, 103, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (164, 13, 104, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (165, 14, 105, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (166, 15, 106, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (167, 13, 107, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (168, 14, 108, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (169, 15, 109, '2022-12-03 20:32:33', '2022-12-03 20:32:33');
INSERT INTO `ec_join_class` VALUES (170, 13, 110, '2022-12-03 20:32:33', '2022-12-03 20:32:33');

-- ----------------------------
-- Table structure for es_school
-- ----------------------------
DROP TABLE IF EXISTS `es_school`;
CREATE TABLE `es_school`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '学校名称',
  `site` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '学校域名',
  `introduce` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '学校介绍',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '学校信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of es_school
-- ----------------------------
INSERT INTO `es_school` VALUES (1, NULL, '安徽科技学院', '        \r\n        \r\nhttps://www.ahstu.edu.cn/', '安徽科技学院（Anhui Science And Technology University），主校区位于安徽省滁州市凤阳县，是经国家教育部批准成立的全日制普通本科高等学校，安徽省地方应用型高水平大学建设高校，服务国家特殊需求硕士专业学位人才培养项目试点单位，入选教育部“卓越工程师教育培养计划”、“卓越农林人才教育培养计划”、“新工科研究与实践项目”、国家级大学生创新创业训练计划、安徽省高等教育振兴计划。', '2022-12-03 18:41:04', '2022-12-03 18:41:04');

-- ----------------------------
-- Table structure for es_school_auth
-- ----------------------------
DROP TABLE IF EXISTS `es_school_auth`;
CREATE TABLE `es_school_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `student_id` int(11) NOT NULL COMMENT '学生id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '学生认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of es_school_auth
-- ----------------------------
INSERT INTO `es_school_auth` VALUES (1, 10, 3, '2022-12-14 08:49:43', '2022-12-14 08:49:43');

-- ----------------------------
-- Table structure for es_school_department
-- ----------------------------
DROP TABLE IF EXISTS `es_school_department`;
CREATE TABLE `es_school_department`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'logo',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '班级名称/部门',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级部门',
  `leader_id` int(11) NOT NULL COMMENT '辅导员/部门管理',
  `school_id` int(11) NOT NULL COMMENT '学校id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `leader_id`(`leader_id`) USING BTREE,
  INDEX `school_id`(`school_id`) USING BTREE,
  CONSTRAINT `es_school_department_ibfk_2` FOREIGN KEY (`leader_id`) REFERENCES `es_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `es_school_department_ibfk_3` FOREIGN KEY (`school_id`) REFERENCES `es_school` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '学习部门信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of es_school_department
-- ----------------------------
INSERT INTO `es_school_department` VALUES (2, NULL, '计算机专升本', NULL, 10, 1, '2022-12-03 18:42:19', '2022-12-03 18:42:38');

-- ----------------------------
-- Table structure for es_school_student
-- ----------------------------
DROP TABLE IF EXISTS `es_school_student`;
CREATE TABLE `es_school_student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '工号/学号',
  `real_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '姓名',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `department_id` int(11) NOT NULL COMMENT '部门/班级',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `department_id`(`department_id`) USING BTREE,
  CONSTRAINT `es_school_student_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `es_school_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '学校用户认证信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of es_school_student
-- ----------------------------
INSERT INTO `es_school_student` VALUES (3, '270123456', '**家', NULL, NULL, 2, '2022-12-03 19:11:18', '2022-12-18 13:55:13');

-- ----------------------------
-- Table structure for es_user
-- ----------------------------
DROP TABLE IF EXISTS `es_user`;
CREATE TABLE `es_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '别名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '密码',
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `bg_picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '背景图像',
  `autograph` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '签名',
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '邮箱',
  `auth_id` int(11) NULL DEFAULT NULL COMMENT '认证id',
  `role` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '0：学生 1：教师',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态，0：启用，>0:封禁',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`, `username`, `email`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `auth_id`(`auth_id`) USING BTREE,
  CONSTRAINT `es_user_ibfk_1` FOREIGN KEY (`auth_id`) REFERENCES `es_school_auth` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 113 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of es_user
-- ----------------------------
INSERT INTO `es_user` VALUES (8, '马明111', '刘丽', '$2a$10$XQM1Qu/zNScKUBRmhJYvHO4hZztDiZbJhFG/FE0J5y4nO3iiBnhki', NULL, NULL, NULL, NULL, 'r.dfs@qq.com', NULL, '0', 1, '2022-10-08 18:40:43', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (9, '马明-马明', '刘丽1111', '$2a$10$Qnu0vwIelSnyo1P4L3RBnufZG9EWmcFo0bwHwA4O5NFuWHvl3sZXy', NULL, NULL, NULL, NULL, 'r.nmhyi@qq.com', NULL, '0', 1, '2022-10-09 15:38:04', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (10, 'baymax', 'baymax', '$2a$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'https://avatars.githubusercontent.com/u/62382059?v=4', NULL, NULL, NULL, '123456@qq.sjj', 1, '0', 1, '2022-10-11 14:58:23', '2022-12-14 09:16:49');
INSERT INTO `es_user` VALUES (11, '薛军=薛军', '熊刚111', '$2a$10$lVUGnxb39BEOZHAffDJ90.5UITvj9tPme/XUxjorfiNoKHDGA1UTe', NULL, NULL, NULL, NULL, 'd.lsmgpa@qq.com', NULL, '0', 1, '2022-10-12 19:32:17', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (12, 'testbaymax', 'testbaymax', '$2a$10$4TZQDF.e0yozNroyOstsquKM0J56lH1rxYt7g.qkkW/3Vxiij3rcy', NULL, NULL, NULL, NULL, 'n.yxmj@qq.com', NULL, '0', 1, '2022-10-19 20:38:17', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (13, 'Steven', 'Michael Wilson', '$2a$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f28379&text=Elizabeth Thomas', NULL, NULL, NULL, 'h.rlnpwclmj@nampbc.pa', NULL, '0', 1, '2022-12-03 19:58:55', '2023-02-02 11:09:05');
INSERT INTO `es_user` VALUES (14, 'Scott Jones', 'John Rodriguez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/7992f2&text=Anthony White', NULL, NULL, NULL, 'u.jsktbv@sieokbirx.bd', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (15, 'Angela Perez', 'Christopher Anderson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/b5f279&text=Carol Gonzalez', NULL, NULL, NULL, 'w.lcimmo@awfryfkcjb.az', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (16, 'Michael Gonzalez', 'Helen Gonzalez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279d9&text=Laura Perez', NULL, NULL, NULL, 'h.enmgwwsop@lgomkkxux.sg', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (17, 'Karen Thompson', 'Anna Jackson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2e7&text=Maria Johnson', NULL, NULL, NULL, 'h.sudqfkk@iufxujjt.fk', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (18, 'John Thompson', 'William Martinez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2c479&text=Sarah Garcia', NULL, NULL, NULL, 'o.gidx@rrkqhg.gu', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (19, 'Michelle Garcia', 'Elizabeth Martinez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/a179f2&text=Timothy Jones', NULL, NULL, NULL, 'x.uafc@fhexi.th', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (20, 'Mark Perez', 'Gary Rodriguez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f27d&text=Joseph Jones', NULL, NULL, NULL, 'k.xldlsgsv@ymy.al', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (21, 'Gary Davis', 'Anthony Garcia', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f27997&text=Mary Hernandez', NULL, NULL, NULL, 'q.pxda@wyjtv.mo', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (22, 'Susan Lee', 'Michael Smith', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79bbf2&text=Shirley Anderson', NULL, NULL, NULL, 'r.fntgui@qqtoe.se', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (23, 'Gary Brown', 'Laura White', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/def279&text=Elizabeth Jackson', NULL, NULL, NULL, 'e.fhk@mbunjqdqt.sn', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (24, 'Sandra Martin', 'Edward Williams', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/e279f2&text=Patricia Walker', NULL, NULL, NULL, 'i.vrmqgiee@nlkskbgrm.it', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (25, 'Charles Jones', 'James Davis', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2bf&text=Mary Johnson', NULL, NULL, NULL, 'j.qpbht@xwsr.gov.cn', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (26, 'Margaret Williams', 'Timothy White', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f29c79&text=Carol Allen', NULL, NULL, NULL, 'o.bppbucn@djyogjhmt.中国互联.网络', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (27, 'Sarah Rodriguez', 'Elizabeth Williams', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/7979f2&text=Shirley Thomas', NULL, NULL, NULL, 't.jven@uiiehhgvq.nu', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (28, 'Joseph Harris', 'Joseph Martinez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/9cf279&text=James Harris', NULL, NULL, NULL, 'g.njnqnfhue@tpm.pe', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (29, 'John Thomas', 'Jennifer Garcia', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279c0&text=Susan Harris', NULL, NULL, NULL, 'v.nyqq@fubqdx.zm', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (30, 'Jeffrey White', 'Linda Martin', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79e3f2&text=Lisa Miller', NULL, NULL, NULL, 'p.sqb@xcibntvhq.bb', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (31, 'Matthew Thomas', 'Mark Thomas', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2dd79&text=Shirley Jackson', NULL, NULL, NULL, 's.ekwguj@ebypbgiqok.qa', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (32, 'Michael Moore', 'Brenda Lewis', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/ba79f2&text=Deborah Walker', NULL, NULL, NULL, 'c.nnxyn@sicsk.kn', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (33, 'Charles Young', 'Angela Young', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f296&text=Helen Moore', NULL, NULL, NULL, 'j.qqkrffz@rieco.do', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (34, 'Karen Davis', 'Deborah Jackson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2797e&text=Jessica Gonzalez', NULL, NULL, NULL, 'n.epjltteor@yqnzg.id', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (35, 'Gary Thomas', 'Dorothy Moore', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79a2f2&text=Mary Thompson', NULL, NULL, NULL, 'b.jryty@gkljwyk.dj', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (36, 'Susan Hall', 'Donna Lewis', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/c5f279&text=Donald Perez', NULL, NULL, NULL, 'o.tlq@gxkrwn.hn', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (37, 'Scott Wilson', 'Gary Thomas', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279e8&text=Shirley Moore', NULL, NULL, NULL, 'o.civlrtyrr@samdfnd.ec', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (38, 'Frank Garcia', 'Jeffrey Thomas', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2d8&text=Mary Jackson', NULL, NULL, NULL, 'o.ajuttde@cttml.tv', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (39, 'Brian Robinson', 'Melissa Moore', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2b579&text=Charles Lee', NULL, NULL, NULL, 'l.gcguunmzlt@skgrkymnq.hm', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (40, 'Gary Clark', 'Steven Garcia', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/9179f2&text=Kevin Hernandez', NULL, NULL, NULL, 'c.nqpbvfh@uqhrols.ye', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (41, 'Ruth Wilson', 'Brian Thomas', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/83f279&text=Dorothy Lopez', NULL, NULL, NULL, 'p.kzxhmujxc@bbtecksj.bm', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (42, 'Donald White', 'Robert Hall', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279a7&text=Dorothy Taylor', NULL, NULL, NULL, 'r.rmx@ieipjkld.bi', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (43, 'Dorothy Williams', 'Edward Hall', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79caf2&text=Betty Brown', NULL, NULL, NULL, 'w.knwsmr@leyg.ba', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (44, 'Jose Harris', 'Lisa Davis', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/edf279&text=Donald Williams', NULL, NULL, NULL, 'f.oykjht@jmeecnlkx.ga', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (45, 'Christopher Taylor', 'John Jackson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/d379f2&text=Barbara Lewis', NULL, NULL, NULL, 'k.qxrlulyi@lmkdk.cg', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (46, 'Nancy Rodriguez', 'Patricia Perez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2af&text=Michael Lewis', NULL, NULL, NULL, 'y.eqvlcpc@uwv.et', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (47, 'Sharon Rodriguez', 'Carol Clark', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f28c79&text=Kevin Hall', NULL, NULL, NULL, 'l.seujkmkni@xnhajcte.ae', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (48, 'Shirley Lee', 'Frank Clark', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/7989f2&text=Thomas Gonzalez', NULL, NULL, NULL, 'p.jghlutge@ktyqf.tt', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (49, 'Elizabeth Anderson', 'Steven Walker', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/acf279&text=Michael Robinson', NULL, NULL, NULL, 'r.fpwieulqd@qtbje.pk', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (50, 'Shirley Young', 'Matthew Young', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279cf&text=Michelle Allen', NULL, NULL, NULL, 'z.rtiwharo@izfp.cn', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (51, 'Linda Brown', 'Margaret Garcia', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2f1&text=Sharon Moore', NULL, NULL, NULL, 'g.wzjqsowd@bfeblbkpa.al', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (52, 'Kimberly Hall', 'Brian Lewis', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2ce79&text=Barbara Hall', NULL, NULL, NULL, 'p.qozwyqxmwg@auqcfeku.rw', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (53, 'Helen Thomas', 'Richard Jones', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/aa79f2&text=Thomas Anderson', NULL, NULL, NULL, 'd.dtzlo@rgumuwcg.sd', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (54, 'Karen Williams', 'Michael Lewis', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f287&text=Jeffrey Johnson', NULL, NULL, NULL, 'k.cbzx@djjpjt.cv', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (55, 'Eric Thompson', 'Helen Jones', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2798e&text=Christopher Johnson', NULL, NULL, NULL, 'w.fbodl@vapcyi.ki', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (56, 'Carol Walker', 'Elizabeth Taylor', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79b1f2&text=Kimberly Jones', NULL, NULL, NULL, 'q.rbwjouxkq@pgerbtqwh.rw', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (57, 'Karen Moore', 'Edward Robinson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/d4f279&text=Linda Perez', NULL, NULL, NULL, 'k.lowohfv@tjyk.ng', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (58, 'Linda White', 'Barbara Rodriguez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/ec79f2&text=Nancy Clark', NULL, NULL, NULL, 'u.lxlmuf@frcepyk.fk', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (59, 'Donna Garcia', 'Donald Lee', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2c8&text=Daniel Rodriguez', NULL, NULL, NULL, 'c.iarbtmt@qroigvl.mp', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (60, 'Larry White', 'Michael Anderson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2a579&text=Susan Lewis', NULL, NULL, NULL, 'l.centb@ogvhtt.vc', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (61, 'Jose Anderson', 'Edward Anderson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/8279f2&text=Elizabeth Walker', NULL, NULL, NULL, 'c.qqo@dtkn.uk', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (62, 'Jason Miller', 'Donna Williams', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/93f279&text=Maria Harris', NULL, NULL, NULL, 'e.jncyjnb@jgo.gd', NULL, '0', 1, '2022-12-03 19:58:55', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (63, 'Ruth Perez', 'Gary Clark', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279b6&text=Richard Lee', NULL, NULL, NULL, 'c.lpus@vwsxihmmy.pt', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (64, 'Anthony Robinson', 'Donald Perez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79daf2&text=Carol Walker', NULL, NULL, NULL, 't.uuffvoxy@mqateqa.dk', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (65, 'Charles Johnson', 'Nancy Lee', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2e779&text=Deborah Hall', NULL, NULL, NULL, 'y.xkwynt@vfxqv.sb', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (66, 'Kevin Martin', 'Donald Wilson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/c379f2&text=Carol Walker', NULL, NULL, NULL, 't.ryhvl@vgyqurarek.ge', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (67, 'John Wilson', 'Lisa Jackson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2a0&text=Robert Walker', NULL, NULL, NULL, 'j.iqadotdkl@zijs.re', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (68, 'Deborah Anderson', 'Helen Garcia', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f27d79&text=Richard Clark', NULL, NULL, NULL, 'r.syfqgih@yiih.br', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (69, 'Anna Rodriguez', 'Anthony Lewis', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/7998f2&text=Laura Garcia', NULL, NULL, NULL, 't.bysf@prjviqvet.ve', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (70, 'James Robinson', 'Robert Johnson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/bbf279&text=Brenda Hernandez', NULL, NULL, NULL, 'u.igqy@dqdge.ky', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (71, 'Eric Moore', 'George Thompson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279df&text=Kimberly Brown', NULL, NULL, NULL, 'k.lxwxfv@enjgbh.中国互联.公司', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (72, 'Margaret Davis', 'Steven Garcia', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2e1&text=Jessica Jackson', NULL, NULL, NULL, 'r.nvslye@hydm.pf', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (73, 'David Johnson', 'Gary White', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2be79&text=David Lewis', NULL, NULL, NULL, 'x.lossqqqz@avilsh.kz', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (74, 'Michael Davis', 'Dorothy Jones', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/9b79f2&text=Kevin Moore', NULL, NULL, NULL, 'q.hhgiwfsw@aewwps.hk', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (75, 'Michael Anderson', 'Kenneth Perez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/7af279&text=Ruth Davis', NULL, NULL, NULL, 'u.xlcuqejj@lncaju.sn', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (76, 'David Anderson', 'Gary Smith', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2799d&text=Larry Moore', NULL, NULL, NULL, 'b.jfnkshq@bwjaz.hn', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (77, 'Kimberly Hall', 'John Jackson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79c0f2&text=Brian Robinson', NULL, NULL, NULL, 'r.kedybeo@wdzmjdiyr.mv', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (78, 'Ronald Garcia', 'Brenda Taylor', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/e4f279&text=Edward Young', NULL, NULL, NULL, 'n.uleatase@dnipql.fr', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (79, 'Michael Davis', 'George Thomas', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/dc79f2&text=William Wilson', NULL, NULL, NULL, 'c.brpsj@qdlbmdh.ma', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (80, 'Edward Moore', 'Jose Allen', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2b9&text=Brenda Martin', NULL, NULL, NULL, 'j.ddympxlj@ftfrjsp.gp', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (81, 'Patricia Garcia', 'Timothy Smith', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f29679&text=Shirley Williams', NULL, NULL, NULL, 'y.mxjokewl@umnogquly.az', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (82, 'Jennifer White', 'Scott Harris', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/797ff2&text=Gary Anderson', NULL, NULL, NULL, 'x.fvz@khab.cc', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (83, 'Gary Gonzalez', 'Sandra Thompson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/a2f279&text=Jessica Hernandez', NULL, NULL, NULL, 'e.ikxn@yfmjnw.af', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (84, 'Kenneth Davis', 'Michelle Smith', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279c6&text=Michelle Wilson', NULL, NULL, NULL, 'g.wrvezncd@xhmswiwu.gb', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (85, 'Margaret Thomas', 'Christopher Hernandez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79e9f2&text=Barbara Thomas', NULL, NULL, NULL, 'q.nqgo@ztdh.gh', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (86, 'Gary Harris', 'Anthony Thomas', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2d779&text=Scott Garcia', NULL, NULL, NULL, 'y.obouiis@sfiwgrn.中国互联.公司', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (87, 'Laura Jackson', 'Sandra Hernandez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/b479f2&text=Donna Thompson', NULL, NULL, NULL, 'b.qewijlg@mreavlnhe.com', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (88, 'Betty Smith', 'Jessica Clark', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f290&text=Daniel Miller', NULL, NULL, NULL, 'q.mfjdedjqt@epflq.lb', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (89, 'Sharon Allen', 'Mark Walker', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f27984&text=Melissa Martin', NULL, NULL, NULL, 'x.eevfqmk@olwslf.my', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (90, 'Sharon Rodriguez', 'George Martin', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79a7f2&text=Steven Thompson', NULL, NULL, NULL, 'u.quyrdkqx@tumlkmbj.ge', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (91, 'Jessica Perez', 'Edward Lopez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/cbf279&text=Steven Moore', NULL, NULL, NULL, 'v.muwrud@xmrvlwjwf.sy', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (92, 'Timothy Thompson', 'Carol Hernandez', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279ee&text=Betty Brown', NULL, NULL, NULL, 'u.vfgpr@bkoet.coop', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (93, 'Edward Smith', 'Christopher Williams', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2d2&text=Shirley Jones', NULL, NULL, NULL, 'j.ebk@telocemv.vu', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (94, 'Angela White', 'Nancy Taylor', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2af79&text=Jessica Hall', NULL, NULL, NULL, 'q.bcfqn@lhcodl.ie', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (95, 'William Young', 'David Garcia', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/8b79f2&text=Lisa Jones', NULL, NULL, NULL, 'p.njsfs@jhxnwanog.org', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (96, 'Christopher Hernandez', 'Michelle Thompson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/89f279&text=Sandra White', NULL, NULL, NULL, 'c.erpikvee@hgniwcrk.ke', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (97, 'Sandra Johnson', 'Gary Jones', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279ad&text=Frank Moore', NULL, NULL, NULL, 's.jvpndv@ctdxmf.ca', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (98, 'George Martin', 'Eric Jackson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79d0f2&text=Shirley Jones', NULL, NULL, NULL, 'f.kjbjstbtk@fmwyjrtzx.gu', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (99, 'Ronald Walker', 'Jose Allen', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2f079&text=Melissa Thomas', NULL, NULL, NULL, 'e.upxgfudqwn@scmknivb.ba', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (100, 'Laura Thompson', 'Linda Jones', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/cd79f2&text=Scott Thompson', NULL, NULL, NULL, 'u.zxtxpmuq@trnflagbd.museum', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (101, 'Donald Johnson', 'Jennifer Johnson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2aa&text=Susan Miller', NULL, NULL, NULL, 'y.scgoexgd@vdjsbprx.lc', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (102, 'Cynthia Young', 'Gary Smith', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f28679&text=Richard Perez', NULL, NULL, NULL, 'b.mwbyefxoer@ocjqjmglgx.rw', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (103, 'Melissa Brown', 'Karen Young', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/798ef2&text=Deborah Rodriguez', NULL, NULL, NULL, 'q.mwkbks@wmjhte.hm', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (104, 'Larry Robinson', 'Susan Hall', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/b2f279&text=Sarah Perez', NULL, NULL, NULL, 's.tygsekuof@ywuokyt.tg', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (105, 'Anthony Davis', 'Sandra Allen', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f279d5&text=Kenneth Wilson', NULL, NULL, NULL, 'e.kqhhu@cfebkpqurp.ve', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (106, 'Elizabeth Jackson', 'David Martin', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f2eb&text=Dorothy Martin', NULL, NULL, NULL, 'l.nikttj@wrojltz.mv', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (107, 'Joseph Martinez', 'Jason Smith', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f2c879&text=Paul Brown', NULL, NULL, NULL, 'z.xvhz@sjgqwmzsn.ph', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (108, 'Karen Harris', 'Angela Young', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/a479f2&text=Michael Perez', NULL, NULL, NULL, 'd.xewinfs@iwooylld.bo', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (109, 'Sharon Young', 'Eric Williams', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79f281&text=Gary Miller', NULL, NULL, NULL, 't.hqsd@sqxbdbsyep.nf', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (110, 'George Williams', 'Sarah Robinson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/f27994&text=Jason Allen', NULL, NULL, NULL, 'u.iyyyw@qikrc.ao', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (111, 'Michael Miller', 'Cynthia Wilson', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/79b7f2&text=Joseph Clark', NULL, NULL, NULL, 'u.pxi@mdwidcsor.cd', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');
INSERT INTO `es_user` VALUES (112, 'Karen Garcia', 'Elizabeth Young', '$10$EgY/iP1EGtX80SJpM9qUYuX7IAb1UOwIH596o.FZaOQ5wF5vDRPyK', 'http://dummyimage.com/200x200/daf279&text=Joseph Taylor', NULL, NULL, NULL, 'w.fbtrnkus@xvi.ms', NULL, '0', 1, '2022-12-03 19:59:11', '2022-12-07 17:02:00');

-- ----------------------------
-- View structure for es_user_auth_info
-- ----------------------------
DROP VIEW IF EXISTS `es_user_auth_info`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `es_user_auth_info` AS select `es_user`.`id` AS `user_id`,`es_user`.`username` AS `username`,`es_user`.`nickname` AS `nickname`,`es_user`.`picture` AS `picture`,`es_school_student`.`job_no` AS `job_no`,`es_school_student`.`real_name` AS `real_name`,`es_school_student`.`email` AS `email`,`es_school_student`.`id` AS `student_id`,`es_school_student`.`phone` AS `phone`,`es_school_student`.`department_id` AS `department_id`,`es_school_department`.`logo` AS `department_logo`,`es_school_department`.`name` AS `department_name`,`es_school_department`.`parent_id` AS `parent_id`,`es_school_department`.`leader_id` AS `leader_id`,`es_school_department`.`school_id` AS `school_id`,`es_school`.`logo` AS `school_logo`,`es_school`.`name` AS `school_name`,`es_school`.`site` AS `school_site`,`es_school_auth`.`created_at` AS `auth_time` from ((((`es_user` left join `es_school_auth` on((`es_user`.`auth_id` = `es_school_auth`.`id`))) left join `es_school_student` on((`es_school_auth`.`student_id` = `es_school_student`.`id`))) left join `es_school_department` on((`es_school_student`.`department_id` = `es_school_department`.`id`))) left join `es_school` on((`es_school_department`.`school_id` = `es_school`.`id`)));

SET FOREIGN_KEY_CHECKS = 1;
