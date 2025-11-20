# DTS-SHOP 特色农产品电商系统

> 基于 Spring Boot + Vue.js + 微信小程序的全栈电商解决方案

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.0.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-2.6.10-brightgreen.svg)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-5.7%2F8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📖 项目简介

DTS-SHOP 是一个完整的电商系统，包含：
- 🛒 **微信小程序** - 用户端购物平台
- 💻 **管理后台** - Vue.js + Element UI 管理界面
- 🔧 **后端API** - Spring Boot RESTful 接口

### ✨ 主要功能

**用户端（小程序）：**
- 商品浏览、搜索、分类
- 购物车、下单、支付
- 订单管理、物流查询
- 优惠券、团购活动
- 用户中心、地址管理
- 收藏、足迹、评论

**管理端：**
- 商品管理（CRUD、上下架）
- 订单管理（发货、退款）
- 用户管理
- 统计分析（销售数据、图表展示）
- 品牌、分类、专题管理
- 优惠券、团购管理
- 佣金系统

## 🏗️ 技术栈

### 后端
- **框架：** Spring Boot 2.0.4
- **ORM：** MyBatis
- **安全：** Apache Shiro
- **数据库：** MySQL 5.7/8.0
- **连接池：** Druid
- **构建工具：** Maven 3.8+

### 前端（管理后台）
- **框架：** Vue 2.6.10
- **UI组件：** Element UI 2.15.6
- **构建工具：** webpack
- **状态管理：** Vuex
- **路由：** Vue Router

### 小程序
- **框架：** 微信原生小程序
- **支付：** 微信支付（支持模拟支付）

## 📦 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Node.js 14.x - 20.x
- MySQL 5.7 或 8.0
- 微信开发者工具

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/你的用户名/dts-shop.git
cd dts-shop
```

2. **创建数据库**
```bash
mysql -u root -p
CREATE DATABASE shop CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
mysql -u root -p shop < shop.sql
```

3. **修改配置**

编辑 `dts-core/src/main/resources/application-dev.yml`：
```yaml
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/shop...
      username: root
      password: 你的密码  # 修改这里
```

4. **编译项目**
```bash
# Windows
build.bat

# Linux/Mac
mvn clean install -DskipTests
```

5. **启动服务**

**Windows:**
```bash
# 一键启动所有服务
start-all.bat

# 或分别启动
start-admin-api.bat  # 管理后台API (8083)
start-wx-api.bat     # 小程序API (8080)
start-admin-ui.bat   # 管理前端 (9527)
```

**Linux/Mac:**
```bash
# 启动管理后台API
java -jar dts-admin-api/target/dts-admin-api-0.0.1-SNAPSHOT-exec.jar --spring.profiles.active=dev &

# 启动小程序API
java -jar dts-wx-api/target/dts-wx-api-0.0.1-SNAPSHOT-exec.jar --spring.profiles.active=dev &

# 启动管理前端
cd admin-ui && npm install && npm run dev
```

6. **启动小程序**
- 打开微信开发者工具
- 导入项目：选择 `wx-mini-program` 目录
- AppID：`wxa0a35dc9af3f7794`（测试号）
- 勾选"不校验合法域名"
- 点击"编译"

### 访问地址

- 管理后台：http://localhost:9527
  - 账号：`admin123`
  - 密码：`admin123`
- 小程序API：http://localhost:8080
- 管理后台API：http://localhost:8083

## 📁 项目结构

```
dts-shop/
├── dts-admin-api/      # 管理后台API模块
├── dts-wx-api/         # 微信小程序API模块
├── dts-core/           # 核心模块（工具类、配置）
├── dts-dao/            # 数据访问层
├── admin-ui/           # 管理后台前端（Vue）
├── wx-mini-program/    # 微信小程序
├── shop.sql            # 数据库脚本
├── build.bat           # 编译脚本
├── start-all.bat       # 一键启动脚本
└── 新电脑部署指南.md   # 详细部署文档
```

## 📸 项目截图

### 管理后台
- 仪表盘：销售统计、数据图表
- 商品管理：列表、创建、编辑
- 订单管理：订单列表、详情、发货

### 小程序
- 首页：轮播图、商品推荐
- 分类：商品分类浏览
- 购物车：商品管理、结算
- 个人中心：订单、收藏、足迹

## 🔧 配置说明

### 数据库配置
位置：`dts-core/src/main/resources/application-dev.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/shop
    username: root
    password: 123456
```

### 文件存储配置
```yaml
dts:
  storage:
    active: local  # 支持：local, aliyun, tencent, qiniu
    local:
      storagePath: D:\\my-storage
```

### 微信配置
```yaml
dts:
  wx:
    app-id: your-app-id
    app-secret: your-app-secret
    mock-pay: true  # 开发时使用模拟支付
```

## 🚀 部署上线

详细部署文档请查看：[部署指南.md](部署指南.md)

### 生产环境配置
1. 修改 `application-prod.yml`
2. 配置真实的数据库地址
3. 配置文件存储服务
4. 配置微信支付参数
5. 使用 Nginx 反向代理

## ⚠️ 注意事项

1. **安全警告：**
   - 默认数据库密码为 `123456`，生产环境请修改
   - `application-dev.yml` 包含敏感信息，不要提交到公开仓库

2. **端口占用：**
   - 管理后台API：8083
   - 小程序API：8080
   - 管理前端：9527

3. **编译问题：**
   - 确保使用 JDK 1.8
   - Maven 需要配置国内镜像

## 📝 开发文档

- [API文档](docs/API.md)
- [数据库设计](docs/DATABASE.md)
- [部署指南](新电脑部署指南.md)

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 License

[MIT License](LICENSE)

## 👨‍💻 作者

- GitHub: [@your-username](https://github.com/helloboy829)

## 🙏 鸣谢

感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element UI](https://element.eleme.io/)
- [MyBatis](https://mybatis.org/)

---

⭐ 如果这个项目对你有帮助，请给一个 Star！
