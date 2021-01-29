SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 班级信息表
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `name`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '班级名称，例如：搬砖者',
    `info`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '班级信息：例如：搬砖的人',
    `semester_id` int(10)                                                       NULL     DEFAULT NULL COMMENT '对应学期',
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `name_del` (`name`, `delete_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '班级信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 文件表
-- ----------------------------
DROP TABLE IF EXISTS `lin_file`;
CREATE TABLE `lin_file`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `path`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `type`        varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'LOCAL' COMMENT 'LOCAL 本地，REMOTE 远程',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `extension`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `size`        int(11)                                                       NULL     DEFAULT NULL,
    `md5`         varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT 'md5值，防止上传重复文件',
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `md5_del` (`md5`, `delete_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 日志表
-- ----------------------------
DROP TABLE IF EXISTS `lin_log`;
CREATE TABLE `lin_log`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `message`     varchar(450) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `user_id`     int(10) UNSIGNED                                              NOT NULL,
    `username`    varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `status_code` int(11)                                                       NULL     DEFAULT NULL,
    `method`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `path`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `permission`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 权限表
-- ----------------------------
DROP TABLE IF EXISTS `lin_permission`;
CREATE TABLE `lin_permission`
(
    `id`          int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `name`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称，例如：访问首页',
    `module`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限所属模块，例如：人员管理',
    `mount`       tinyint(1)                                                   NOT NULL DEFAULT 1 COMMENT '0：关闭 1：开启',
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 43
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 分组表
-- ----------------------------
DROP TABLE IF EXISTS `lin_group`;
CREATE TABLE `lin_group`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `name`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '分组名称，例如：搬砖者',
    `info`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '分组信息：例如：搬砖的人',
    `level`       tinyint(2)                                                    NOT NULL DEFAULT 2 COMMENT '分组级别 1：root 2：user 3：guest  root（root、guest分组只能存在一个)',
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `name_del` (`name`, `delete_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 分组-权限表
-- ----------------------------
DROP TABLE IF EXISTS `lin_group_permission`;
CREATE TABLE `lin_group_permission`
(
    `id`            int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `group_id`      int(10) UNSIGNED NOT NULL COMMENT '分组id',
    `permission_id` int(10) UNSIGNED NOT NULL COMMENT '权限id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `group_id_permission_id` (`group_id`, `permission_id`) USING BTREE COMMENT '联合索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 22
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 用户基本信息表
-- ----------------------------
DROP TABLE IF EXISTS `lin_user`;
CREATE TABLE `lin_user`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `username`    varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户名，唯一',
    `nickname`    varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '用户昵称',
    `avatar`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '头像url',
    `email`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '邮箱',
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    `is_new`      int(1)                                                        NULL     DEFAULT NULL COMMENT '是否为默认密码/新用户',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username_del` (`username`, `delete_time`) USING BTREE,
    UNIQUE INDEX `email_del` (`email`, `delete_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 用户授权信息表
# id
# user_id
# identity_type 登录类型（手机号 邮箱 用户名）或第三方应用名称（微信 微博等）
# identifier 标识（手机号 邮箱 用户名或第三方应用的唯一标识）
# credential 密码凭证（站内的保存密码，站外的不保存或保存token）
-- ----------------------------
DROP TABLE IF EXISTS `lin_user_identity`;
CREATE TABLE `lin_user_identity`
(
    `id`            int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `user_id`       int(10) UNSIGNED                                              NOT NULL COMMENT '用户id',
    `identity_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `identifier`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `credential`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`   datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time`   datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time`   datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 用户-分组表
-- ----------------------------
DROP TABLE IF EXISTS `lin_user_group`;
CREATE TABLE `lin_user_group`
(
    `id`       int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id`  int(10) UNSIGNED NOT NULL COMMENT '用户id',
    `group_id` int(10) UNSIGNED NOT NULL COMMENT '分组id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id_group_id` (`user_id`, `group_id`) USING BTREE COMMENT '联合索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 23
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 学期表
-- ----------------------------
DROP TABLE IF EXISTS `semester`;
CREATE TABLE `semester`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学期名称',
    `info`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '学期信息',
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '学期列表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 签到表
-- ----------------------------
DROP TABLE IF EXISTS `sign_list`;
CREATE TABLE `sign_list`
(
    `id`          int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `class_id`    int(10) UNSIGNED                                             NOT NULL COMMENT '班级id',
    `name`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签到项目名称',
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `end_time`    datetime(3)                                                  NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 学生-班级 关系表
-- ----------------------------
DROP TABLE IF EXISTS `student_class`;
CREATE TABLE `student_class`
(
    `id`       int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id`  int(10) UNSIGNED NOT NULL COMMENT '用户id',
    `class_id` int(10) UNSIGNED NOT NULL COMMENT '班级id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id_group_id` (`user_id`, `class_id`) USING BTREE COMMENT '联合索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 学生-签到 关系表
-- ----------------------------
DROP TABLE IF EXISTS `student_sign`;
CREATE TABLE `student_sign`
(
    `id`          int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `user_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '学生id',
    `sign_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '签到项目id',
    `ip`          varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `status`      tinyint(1)                                                   NOT NULL DEFAULT 1 COMMENT '签到状态：1-正常签到 2-迟到 3-作废',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id_sign_id` (`user_id`, `sign_id`) USING BTREE COMMENT '联合索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '学生签到表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 教师-班级 关系表
-- ----------------------------
DROP TABLE IF EXISTS `teacher_class`;
CREATE TABLE `teacher_class`
(
    `id`       int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id`  int(10) UNSIGNED NOT NULL COMMENT '用户id',
    `class_id` int(10) UNSIGNED NOT NULL COMMENT '班级id',
    `level`    tinyint(1)       NOT NULL COMMENT '教师等级 1-主教师 2-助教',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '教师-班级关系表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 插入超级管理员
-- 插入root分组
-- ----------------------------
BEGIN;

INSERT INTO lin_user(id, username, nickname)
VALUES (1, 'root', 'root');

INSERT INTO lin_user_identity (id, user_id, identity_type, identifier, credential)
VALUES (1, 1, 'USERNAME_PASSWORD', 'root',
        'pbkdf2sha256:64000:18:24:n:yUnDokcNRbwILZllmUOItIyo9MnI00QW:6ZcPf+sfzyoygOU8h/GSoirF');

INSERT INTO `lin_group`
VALUES (1, 'root', '超级用户组', 1, '2020-04-16 15:35:34.645', '2021-01-28 20:32:16.813', NULL);
INSERT INTO `lin_group`
VALUES (2, '教师', '教师组', 2, '2020-04-16 15:35:34.648', '2020-09-20 16:28:23.713', NULL);
INSERT INTO `lin_group`
VALUES (3, '学生', '学生组', 2, '2020-06-13 22:17:12.963', '2020-09-20 16:28:35.173', NULL);
INSERT INTO `lin_group`
VALUES (4, '游客', '游客组', 3, '2021-01-28 20:32:38.201', '2021-01-28 20:32:38.201', NULL);

INSERT INTO lin_user_group(id, user_id, group_id)
VALUES (1, 1, 1);

COMMIT;
