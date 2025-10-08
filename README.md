# Canglong Fund Investigation

## 项目介绍
这是一个用于生成中国基金各种统计数据的应用程序。该系统通过采集基金数据，计算各种统计指标，并提供API接口供前端或其他系统调用。

## 功能特性
- 基金基本信息管理
- 基金价格数据采集与存储
- 基金收益率计算（月度、年度）
- 基金统计数据分析
- RESTful API接口

## 技术栈
- Java 18
- Spring Boot 3.2.1
- Spring Data JPA
- MariaDB/MySQL
- Flyway（数据库版本管理）
- Lombok
- Maven

## 安装指南

### 环境要求
- JDK 18或更高版本
- Maven 3.6或更高版本
- MariaDB 10.4或更高版本

### 数据库配置
1. 创建数据库：`CREATE DATABASE canglong DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
2. 创建用户并授权：
   ```sql
   CREATE USER 'root'@'localhost' IDENTIFIED BY 'passw0rd';
   GRANT ALL PRIVILEGES ON canglong.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

### 配置环境变量（可选）
为了安全起见，建议设置数据库密码环境变量：
```
set DB_PASSWORD=your_database_password
```

### 构建和运行
1. 克隆项目
2. 进入项目目录：`cd d:\Projects\Java\fund`
3. 构建项目：`mvn clean package`
4. 运行应用：`java -jar target/fund-0.0.1-SNAPSHOT.war`

## API接口
应用启动后，API接口可通过以下地址访问：`http://localhost:9000/fund/api/fund`

主要接口：
- GET `/api/fund/{id}` - 获取指定基金信息
- GET `/api/fund/types` - 获取所有基金类型
- GET `/api/fund/search?keyword={keyword}` - 搜索基金

## 开发说明
- 数据库迁移脚本位于 `src/main/resources/db/migration/`
- 配置文件位于 `src/main/resources/application.yml`
- 日志配置位于 `src/main/resources/logback-spring.xml`

## 注意事项
- 确保数据库服务正常运行
- 首次启动会自动执行数据库迁移
- 默认端口为9000，上下文路径为/fund

