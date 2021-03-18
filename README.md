## 预览

### Lin-CMS 线上 demo

[http://face.cms.talelin.com/](http://face.cms.talelin.com/)

### Lin-CMS 文档地址

[https://doc.cms.talelin.com/](https://doc.cms.talelin.com/)

## 依赖
* JDK 1.8+，已在 JAVA8、9、11 上测试通过。
* MySQL 5.6+，确保你有可用的数据库环境。
* Maven 3.6+，依赖、打包需要它。
* DejaVu 字体

## 快速上手

DejaVu 字体在 Linux 上多为系统自带，Windows 上一般没有，这个字体的用途是渲染验证码中的文字，如果
jvm 没有找到字体，则会自动回落到系统字体，这可能导致验证码渲染异常。该字体系列为自由版权，
可以在官网 [https://dejavu-fonts.github.io/](https://dejavu-fonts.github.io/) 上下载。

`resource` 目录下的 `schema.sql` 为数据库初始化配置的sql

数据库连接配置在 `application-dev.yml` 中，或者复制一个副本重命名为 `application-prod.yml` 并修改，运行时加上参数 `--spring.profiles.active=prod`

```bash
# clone the project
git clone https://github.com/gadfly3173/chakki-spring.git

# build
mvn clean package -U
```

推荐的 nginx 配置：
```nginx
server {
listen 80;
listen 443;
    server_name  chakki.gadfly.vip;
    root /home/chakki;
    gzip  on;
    gzip_types text/plain application/x-javascript application/javascript text/css application/xml text/javascript application/x-httpd-php image/jpeg image/gif image/png;
    gzip_static on;
    client_max_body_size 200m;

    location / {
    }

    location ^~ /api/ {
        proxy_pass http://127.0.0.1:19560;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header Host $http_host;
        proxy_set_header X-Forwarded-Host $server_name;
    }

    # jar 运行时会在运行 jar 的路径下生成 assets 文件夹，assets 中存放用户上传的文件
    location ^~ /assets/ {
        alias /home/chakki/backend/assets/;
    }
}
```

## 简介

本项目为 Spring Boot 后端，前端端请移步 [chakki-vue](https://github.com/gadfly3173/chakki-vue)

### 什么是Lin CMS

Lin-CMS 是林间有风团队经过大量项目实践所提炼出的一套**内容管理系统框
架**。Lin-CMS 可以有效的帮助开发者提高 CMS 的开发效率。

本项目是 Lin CMS 后端的 java spring-boot 实现，需要前端？请访
问[前端仓库](https://github.com/TaleLin/lin-cms-vue)。

### Lin CMS 特点

Lin CMS 的构筑思想是有其自身特点的。下面我们阐述一些 Lin 的主要特点。

#### Lin CMS 是一个前后端分离的 CMS 解决方案

Lin 既提供后台的支撑，也有一套对应的前端系统，你也可以选择不同的后端实现，
如 koa 和 flask。

#### 框架本身已内置了 CMS 常用的功能

Lin 已经内置了 CMS 中最为常见的需求：用户管理、权限管理、日志系统等。开发者只需
要集中精力开发自己的 CMS 业务即可

#### Lin CMS 本身也是一套开发规范

Lin CMS 除了内置常见的功能外，还提供了一套开发规范与工具类。
Lin CMS 只需要开发者关注自己的业务开发，它已经内置了很多机制帮助开发者快速开发自己的业务。

#### 扩展灵活

Lin CMS 支持 `extension` 来便捷地增强你的业务。

#### 前端支持

Lin CMS 也有自己的前端实现，强强联合为你助力。

#### 完善的文档

Lin CMS 提供大量的文档来帮助开发者使用

## 注意事项

- 目前正处于测试，勿使用在开发环境中！！！

- Lin CMS 需要一定的基础，至少你得有一定的 java 基础和数据库基础，并且比较熟悉spring-boot和mybatis，
当然如果你是个 java 程序员，这些肯定都不在话下。


- Lin CMS 基于 spring boot ，因此也采取了 spring boot 的 starter （启动器）机制，我们也有自己的
starter，见 [lin-cms-java-core](https://github.com/TaleLin/lin-cms-java-core.git)。 



