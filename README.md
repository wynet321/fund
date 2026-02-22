# Canglong Fund Analysis Platform

## 项目介绍
Canglong Fund Analysis Platform 是一个专业的中国基金数据分析平台，旨在为投资者提供全面、准确的基金数据分析和可视化服务。系统通过自动化采集公开市场数据，结合专业的金融分析模型，帮助用户深入理解基金表现、风险特征和投资机会。

## ✨ 主要功能

### 基金数据管理
- 📊 基金基础信息管理
- 🔍 实时基金数据采集与更新
- 💾 历史数据归档与版本控制

### 统计分析
- 📈 收益率计算（日度、周度、月度、年度）
- 📉 风险评估（波动率、最大回撤、夏普比率等）
- 🔄 相关性分析与投资组合优化
- 🏆 基金排名与筛选

### 数据服务
- 🌐 RESTful API 接口
- 📡 实时数据推送
- 📊 数据导出（CSV, Excel, PDF）
- 🔍 高级搜索与筛选

## 🛠️ 技术栈

### 后端
- **核心框架**: Java 18, Spring Boot 3.2.1, Spring Framework 6.0
- **数据访问**: Spring Data JPA, QueryDSL, JOOQ
- **数据库**: MariaDB 10.4+/MySQL 8.0+
- **缓存**: Redis 6.0+
- **搜索**: Elasticsearch 8.0+
- **消息队列**: RabbitMQ 3.9+
- **任务调度**: Quartz Scheduler
- **API文档**: SpringDoc OpenAPI 3.0

### 开发工具
- **构建工具**: Maven 3.6+
- **代码质量**: Checkstyle, PMD, SpotBugs
- **测试框架**: JUnit 5, Mockito, TestContainers
- **CI/CD**: GitHub Actions, Docker, Kubernetes
- **监控**: Prometheus, Grafana, ELK Stack

## 🚀 快速开始

### 环境要求
- JDK 18+
- Maven 3.6+
- MariaDB 10.4+/MySQL 8.0+
- Redis 6.0+
- Elasticsearch 8.0+
- RabbitMQ 3.9+

### 1. 数据库配置
```sql
-- 创建数据库
CREATE DATABASE canglong 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- 创建专用用户（生产环境建议使用更安全的密码）
CREATE USER 'canglong_user'@'%' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON canglong.* TO 'canglong_user'@'%';
FLUSH PRIVILEGES;
```

### 2. 环境变量配置
创建 `.env` 文件并配置以下环境变量：
```properties
# 数据库配置
SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3306/canglong
SPRING_DATASOURCE_USERNAME=canglong_user
SPRING_DATASOURCE_PASSWORD=your_secure_password

# Redis 配置
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# Elasticsearch 配置
ELASTICSEARCH_HOST=localhost
ELASTICSEARCH_PORT=9200

# 其他配置
SPRING_PROFILES_ACTIVE=dev
```

### 3. 构建与运行
```bash
# 克隆项目
git clone https://github.com/yourusername/canglong-fund.git
cd canglong-fund

# 构建项目
mvn clean package -DskipTests

# 运行应用
java -jar target/fund-0.0.1-SNAPSHOT.war
```

### 4. 使用Docker运行（可选）
```bash
docker-compose up -d
```

## 📚 API 文档

应用启动后，可以通过以下方式访问API文档：
- **Swagger UI**: http://localhost:9000/fund/swagger-ui.html
- **OpenAPI 3.0 JSON**: http://localhost:9000/fund/v3/api-docs

### 主要API端点

#### 基金信息
- `GET /api/fund/{id}` - 获取基金详情
- `GET /api/fund/code/{code}` - 按基金代码查询
- `GET /api/fund/list` - 分页获取基金列表
- `GET /api/fund/types` - 获取所有基金类型
- `GET /api/fund/type/{type}` - 按类型筛选基金

#### 基金数据
- `GET /api/fund/{id}/nav` - 获取基金净值数据
- `GET /api/fund/{id}/returns` - 获取收益率数据
- `GET /api/fund/{id}/risk` - 获取风险评估指标
- `GET /api/fund/{id}/correlation` - 获取相关性分析

#### 搜索与筛选
- `GET /api/fund/search` - 高级搜索
  - 参数: `keyword`, `type`, `minReturn`, `maxRisk`, `sortBy`, `order`
- `GET /api/fund/screener` - 基金筛选器

#### 投资组合
- `POST /api/portfolio` - 创建投资组合
- `GET /api/portfolio/{id}` - 获取投资组合详情
- `POST /api/portfolio/{id}/optimize` - 优化投资组合

## 🛠️ 开发指南

### 项目结构
```
src/
├── main/
│   ├── java/
│   │   └── com/canglong/
│   │       ├── config/         # 配置类
│   │       ├── controller/     # 控制器
│   │       ├── model/          # 数据模型
│   │       ├── repository/     # 数据访问层
│   │       ├── service/        # 业务逻辑
│   │       ├── util/           # 工具类
│   │       └── FundApplication.java
│   └── resources/
│       ├── db/migration/      # Flyway 迁移脚本
│       ├── static/            # 静态资源
│       ├── templates/         # 模板文件
│       ├── application.yml    # 主配置文件
│       └── logback-spring.xml # 日志配置
└── test/                      # 测试代码
```

### 开发规范
1. 代码风格遵循 Google Java Style Guide
2. 提交信息使用 Conventional Commits 规范
3. 新功能需包含单元测试和集成测试
4. API 变更需更新 API 文档

### 测试
```bash
# 运行所有测试
mvn test

# 生成测试覆盖率报告
mvn jacoco:report
```

## 🚨 注意事项

### 安全建议
- 生产环境必须修改默认密码
- 启用 HTTPS
- 配置适当的 CORS 策略
- 实现请求速率限制
- 定期备份数据库

### 性能优化
- 使用 Redis 缓存热点数据
- 数据库查询优化
- 异步处理耗时操作
- 启用 GZIP 压缩

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request。对于重大更改，请先开启 Issue 讨论您想要更改的内容。

### 开发流程
1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 [MIT License](LICENSE)

