SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 公告表
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `title`       varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '公告标题',
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '富文本内容',
    `class_id`    int(10) UNSIGNED                                              NOT NULL COMMENT '对应班级',
    `file_id`     int(10) UNSIGNED                                              NULL     DEFAULT NULL COMMENT '附件id',
    `filename`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '文件名',
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `title_del` (`title`, `delete_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;
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
    `is_new`      tinyint(1)                                                    NULL     DEFAULT NULL COMMENT '是否为默认密码/新用户',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username_del` (`username`, `delete_time`) USING BTREE,
    UNIQUE INDEX `email_del` (`email`, `delete_time`) USING BTREE
) ENGINE = InnoDB
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
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 两步验证密钥表
-- ----------------------------
DROP TABLE IF EXISTS `lin_user_mfa`;
CREATE TABLE `lin_user_mfa`
(
    `id`          int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `user_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '用户id',
    `secret`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '两步验证secret',
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_secret_del` (`user_id`, `secret`, `delete_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 选项表
-- ----------------------------
DROP TABLE IF EXISTS `option`;
CREATE TABLE `option`
(
    `id`          int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `title`       varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '选项内容',
    `question_id` int(10) UNSIGNED                                             NOT NULL COMMENT '对应问卷',
    `order`       tinyint(2) UNSIGNED                                          NOT NULL COMMENT '顺序编号',
    `update_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 问题表
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`
(
    `id`               int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `title`            varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题标题',
    `questionnaire_id` int(10) UNSIGNED                                             NOT NULL COMMENT '对应问卷',
    `order`            tinyint(2) UNSIGNED                                          NOT NULL COMMENT '顺序编号',
    `type`             tinyint(2) UNSIGNED                                          NOT NULL COMMENT '问题类型：1-简答 2-选择',
    `limit`            tinyint(2) UNSIGNED                                          NULL     DEFAULT NULL COMMENT '多选限制数量',
    `create_time`      datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time`      datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time`      datetime(3)                                                  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 问题-回答 关系表
-- ----------------------------
DROP TABLE IF EXISTS `question_answer`;
CREATE TABLE `question_answer`
(
    `id`               int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `question_id`      int(10) UNSIGNED                                              NOT NULL COMMENT '问题id',
    `answer`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `option_id`        int(10) UNSIGNED                                              NULL     DEFAULT NULL,
    `create_time`      datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time`      datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time`      datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 问卷表
-- ----------------------------
DROP TABLE IF EXISTS `questionnaire`;
CREATE TABLE `questionnaire`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `title`       varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '问卷标题',
    `info`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '问卷简介',
    `class_id`    int(10) UNSIGNED                                              NOT NULL COMMENT '对应班级',
    `end_time`    datetime(3)                                                   NULL     DEFAULT NULL,
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `title_del` (`title`, `delete_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

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
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 学生-问卷 关系表
-- ----------------------------
DROP TABLE IF EXISTS `student_questionnaire`;
CREATE TABLE `student_questionnaire`
(
    `id`               int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `user_id`          int(10) UNSIGNED                                             NOT NULL COMMENT '学生id',
    `questionnaire_id` int(10) UNSIGNED                                             NOT NULL COMMENT '问卷id',
    `ip`               varchar(39) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`      datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time`      datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time`      datetime(3)                                                  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 学生-签到 关系表
-- ----------------------------
DROP TABLE IF EXISTS `student_sign`;
CREATE TABLE `student_sign`
(
    `id`          int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `user_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '学生id',
    `sign_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '签到项目id',
    `ip`          varchar(39) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `status`      tinyint(1) UNSIGNED                                          NOT NULL DEFAULT 1 COMMENT '签到状态：1-正常签到 2-迟到 3-作废',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id_sign_id` (`user_id`, `sign_id`) USING BTREE COMMENT '联合索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '学生签到表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 学生作业表
-- ----------------------------
DROP TABLE IF EXISTS `student_work`;
CREATE TABLE `student_work`
(
    `id`          int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `user_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '学生id',
    `work_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '作业项目id',
    `ip`          varchar(39) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `file_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '对应的文件id',
    `rate`        tinyint(2) UNSIGNED                                          NULL     DEFAULT NULL COMMENT '教师打分',
    `create_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_work_delete` (`user_id`, `work_id`, `delete_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 教师-班级 关系表
-- ----------------------------
DROP TABLE IF EXISTS `teacher_class`;
CREATE TABLE `teacher_class`
(
    `id`       int(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
    `user_id`  int(10) UNSIGNED    NOT NULL COMMENT '用户id',
    `class_id` int(10) UNSIGNED    NOT NULL COMMENT '班级id',
    `level`    tinyint(1) UNSIGNED NOT NULL COMMENT '教师等级 1-主教师 2-助教',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '教师-班级关系表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 作业项目表
-- ----------------------------
DROP TABLE IF EXISTS `work`;
CREATE TABLE `work`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `name`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '作业名称，例如：搬砖',
    `info`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '作业信息：例如：第一次板砖',
    `class_id`    int(10) UNSIGNED                                              NOT NULL COMMENT '对应班级',
    `file_size`   int(10) UNSIGNED                                              NULL     DEFAULT NULL COMMENT '单文件大小限制，单位为B/byte',
    `type`        tinyint(1) UNSIGNED                                           NOT NULL COMMENT '作业类型：1-课堂 2-回家',
    `end_time`    datetime(3)                                                   NULL     DEFAULT NULL,
    `create_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `delete_time` datetime(3)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '作业项目表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 作业-扩展名关系表
-- ----------------------------
DROP TABLE IF EXISTS `work_extension`;
CREATE TABLE `work_extension`
(
    `id`        int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `work_id`   int(10) UNSIGNED                                             NOT NULL COMMENT '作业项目id',
    `extension` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '扩展名',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `work_extension` (`work_id`, `extension`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '作业-扩展名关系表'
  ROW_FORMAT = Dynamic;

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

INSERT INTO `lin_group`(id, name, info, level)
VALUES (1, 'root', '超级用户组', 1);
INSERT INTO `lin_group`(id, name, info, level)
VALUES (2, '教师', '教师组', 2);
INSERT INTO `lin_group`(id, name, info, level)
VALUES (3, '学生', '学生组', 2);
INSERT INTO `lin_group`(id, name, info, level)
VALUES (4, '游客', '游客组', 3);

INSERT INTO lin_user_group(id, user_id, group_id)
VALUES (1, 1, 1);

-- ----------------------------
-- Records of lin_permission
-- ----------------------------
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (1, '移除班级内学生', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (2, '查询班级内教师', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (3, '新建班级', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (4, '查询名字符合的不在此班级的学生', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (5, '更新一个学期', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (6, '发起作业', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (7, '更新一个班级', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (8, '查询自己拥有的权限', '用户', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (9, '查看单个通知公告', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (10, '交作业', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (11, '新建学期', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (12, '查询一个班级', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (13, '查询一个班级', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (14, '删除一个班级', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (15, '修改公告文件', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (16, '查询一个签到信息', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (17, '删除学生作业', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (18, '修改签到记录', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (19, '下载指定学生作业文件', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (20, '查询不在班级内的教师', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (21, '查看所有通知公告', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (22, '学生查询所有学期', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (23, '添加班级内学生', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (24, '查看所有签到项目', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (25, '教师查询所有学期', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (26, '查询所有学期', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (27, '搜索日志', '日志', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (28, '给学生作业打分', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (29, '发布通知公告', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (30, '查询所有日志', '日志', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (31, '查询单个作业项目下的所有学生', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (32, '查看班级最新签到项目', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (33, '查看所有作业项目', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (34, '查询自己信息', '用户', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (35, '查询特定班级', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (36, '添加班级内教师', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (37, '移除班级内教师', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (38, '查看单个通知公告', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (39, '查询所有此班级学生', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (40, '查看所有通知公告', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (41, '修改通知公告', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (42, '上传共用图片', '文件管理', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (43, '查看班级内签到项目', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (44, '查询所有此班级学生', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (45, '删除通知公告', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (46, '查看班级内作业项目', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (47, '查询所有不在此班级的学生', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (48, '查询作业详情', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (49, '查询所有班级', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (50, '删除作业', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (51, '学生进行签到', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (52, '下载指定作业项目的全部文件', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (53, '查询学生本学期所属班级', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (54, '查询单个签到项目下的所有学生', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (55, '查看自己所属班级', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (56, '查询教师本学期所属班级', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (57, '发起签到', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (58, '查询一个作业信息', '教师', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (59, '下载公告附件', '学生', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (60, '删除一个学期', '管理员', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (61, '查询日志记录的用户', '日志', 1);
INSERT INTO `lin_permission`(id, name, module, mount)
VALUES (62, '下载公告附件', '教师', 1);

-- ----------------------------
-- Records of lin_group_permission
-- ----------------------------
INSERT INTO `lin_group_permission`
VALUES (26, 2, 6);
INSERT INTO `lin_group_permission`
VALUES (28, 2, 8);
INSERT INTO `lin_group_permission`
VALUES (1, 2, 12);
INSERT INTO `lin_group_permission`
VALUES (2, 2, 15);
INSERT INTO `lin_group_permission`
VALUES (3, 2, 16);
INSERT INTO `lin_group_permission`
VALUES (4, 2, 17);
INSERT INTO `lin_group_permission`
VALUES (5, 2, 18);
INSERT INTO `lin_group_permission`
VALUES (6, 2, 19);
INSERT INTO `lin_group_permission`
VALUES (7, 2, 21);
INSERT INTO `lin_group_permission`
VALUES (8, 2, 24);
INSERT INTO `lin_group_permission`
VALUES (9, 2, 25);
INSERT INTO `lin_group_permission`
VALUES (10, 2, 28);
INSERT INTO `lin_group_permission`
VALUES (11, 2, 29);
INSERT INTO `lin_group_permission`
VALUES (12, 2, 31);
INSERT INTO `lin_group_permission`
VALUES (13, 2, 33);
INSERT INTO `lin_group_permission`
VALUES (14, 2, 34);
INSERT INTO `lin_group_permission`
VALUES (15, 2, 38);
INSERT INTO `lin_group_permission`
VALUES (16, 2, 39);
INSERT INTO `lin_group_permission`
VALUES (17, 2, 41);
INSERT INTO `lin_group_permission`
VALUES (18, 2, 42);
INSERT INTO `lin_group_permission`
VALUES (19, 2, 45);
INSERT INTO `lin_group_permission`
VALUES (20, 2, 50);
INSERT INTO `lin_group_permission`
VALUES (21, 2, 52);
INSERT INTO `lin_group_permission`
VALUES (22, 2, 54);
INSERT INTO `lin_group_permission`
VALUES (23, 2, 56);
INSERT INTO `lin_group_permission`
VALUES (24, 2, 57);
INSERT INTO `lin_group_permission`
VALUES (25, 2, 58);
INSERT INTO `lin_group_permission`
VALUES (27, 2, 62);
INSERT INTO `lin_group_permission`
VALUES (42, 3, 8);
INSERT INTO `lin_group_permission`
VALUES (43, 3, 9);
INSERT INTO `lin_group_permission`
VALUES (29, 3, 10);
INSERT INTO `lin_group_permission`
VALUES (30, 3, 22);
INSERT INTO `lin_group_permission`
VALUES (31, 3, 32);
INSERT INTO `lin_group_permission`
VALUES (32, 3, 34);
INSERT INTO `lin_group_permission`
VALUES (33, 3, 35);
INSERT INTO `lin_group_permission`
VALUES (34, 3, 40);
INSERT INTO `lin_group_permission`
VALUES (35, 3, 43);
INSERT INTO `lin_group_permission`
VALUES (36, 3, 46);
INSERT INTO `lin_group_permission`
VALUES (37, 3, 48);
INSERT INTO `lin_group_permission`
VALUES (38, 3, 51);
INSERT INTO `lin_group_permission`
VALUES (39, 3, 53);
INSERT INTO `lin_group_permission`
VALUES (40, 3, 55);
INSERT INTO `lin_group_permission`
VALUES (41, 3, 59);
INSERT INTO `lin_group_permission`
VALUES (45, 4, 8);
INSERT INTO `lin_group_permission`
VALUES (44, 4, 34);

COMMIT;
