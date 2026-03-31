# Sky Take Out - 外卖系统

## 📋 项目简介

Sky Take Out 是一个基于 Spring Boot 的现代化外卖系统后端项目。该系统采用微服务模块化架构，提供完整的外卖业务流程支持，包括用户管理、商品管理、订单处理、支付结算等核心功能。

## 🏗️ 项目架构

本项目采用 Maven 多模块结构，主要由以下三个模块组成：

### 模块划分

| 模块 | 描述 |
|------|------|
| **sky-common** | 公共模块，包含工具类、常量、异常处理等通用代码 |
| **sky-pojo** | 数据模型模块，定义实体类(Entity)、数据传输对象(DTO)、视图对象(VO) |
| **sky-server** | 服务核心模块，包含业务逻辑、控制器、数据访问层等 |

## 🚀 技术栈

### 后端框架
- **Spring Boot**: 2.7.3
- **Spring Framework**: 基于 Spring Boot 自动配置
- **MyBatis**: 2.2.0（ORM 框架）
- **Druid**: 1.2.1（数据库连接池）

### 数据访问
- **MySQL**: 关系型数据库
- **Redis**: 2.7.3（缓存和 Session 管理）

### 中间件和工具
- **JWT**: 0.9.1（身份认证和授权）
- **PageHelper**: 1.3.0（分页插件）
- **Aliyun OSS**: 3.10.2（对象存储服务）
- **Knife4j/Swagger**: 3.0.2（API 文档生成）
- **AspectJ**: 1.9.4（面向切面编程）
- **Lombok**: 1.18.30（代码生成）
- **FastJSON**: 1.2.76（JSON 处理）
- **Apache POI**: 3.16（Excel 文件处理）
- **WeChat Pay SDK**: 0.4.8（微信支付集成）

### 开发工具
- **Java**: 17
- **Maven**: 项目管理和构建
- **Docker**: 容器化部署

## 📁 项目结构

```
sky-take-out/
├── sky-common/                 # 公共模块
│   ├── src/main/java/
│   │   └── com/sky/
│   │       ├── constant/       # 常量定义
│   │       ├── context/        # 上下文工具
│   │       ├── enumeration/    # 枚举类
│   │       ├── exception/      # 自定义异常
│   │       ├── json/           # JSON 相关工具
│   │       ├── properties/     # 配置属性
│   │       ├── result/         # 返回结果封装
│   │       └── utils/          # 工具类
│   └── pom.xml
│
├── sky-pojo/                   # 数据模型模块
│   ├── src/main/java/
│   │   └── com/sky/
│   │       ├── dto/            # 数据传输对象
│   │       ├── entity/         # 实体类
│   │       └── vo/             # 视图对象
│   └── pom.xml
│
├── sky-server/                 # 核心服务模块
│   ├── src/main/java/
│   │   └── com/sky/
│   │       ├── annotation/     # 自定义注解
│   │       ├── aspect/         # AOP 切面
│   │       ├── config/         # Spring 配置类
│   │       ├── controller/     # REST 控制器
│   │       │   ├── admin/      # 管理员接口
│   │       │   ├── user/       # 用户接口
│   │       │   └── notify/     # 通知接口
│   │       ├── handler/        # 异常处理
│   │       ├── interceptor/    # 拦截器
│   │       ├── mapper/         # 数据访问层
│   │       ├── service/        # 业务逻辑层
│   │       ├── task/           # 定时任务
│   │       ├── webSocket/      # WebSocket 实现
│   │       └── SkyApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 主配置文件
│   │   ├── application-dev.yml # 开发环境配置
│   │   ├── mapper/             # MyBatis XML 映射文件
│   │   └── template/           # 报表模板
│   ├── pom.xml
│   ├── Dockerfile
│   └── docker-compose.yml
│
├── pom.xml                     # 父 POM 文件
├── .gitignore
└── README.md                   # 项目说明文档

```

## 🛠️ 主要功能模块

### 用户相关
- 用户注册和登录
- 用户身份验证（JWT Token）
- 用户个人信息管理
- 地址簿管理

### 商品管理
- 菜品分类管理
- 菜品信息管理
- 菜品口味管理
- 套餐商品管理
- 商品展示列表

### 订单处理
- 购物车管理
- 订单创建和提交
- 订单状态管理
- 订单详情查询
- 订单支付处理

### 管理员功能
- 员工管理
- 权限管理
- 数据统计和报表
- 运营数据分析
- Excel 报表导出

### 支付功能
- 微信支付集成
- 支付回调处理

### 通知服务
- WebSocket 实时通知
- 订单提醒

## 🔧 项目配置

### 数据库配置
项目使用 MySQL 数据库，通过 Druid 连接池管理数据库连接。配置示例：

```yaml
spring:
  datasource:
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/sky_take_out
      username: root
      password: password
```

### Redis 配置
用于缓存和会话管理：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 
```

### JWT 配置
项目使用 JWT 进行身份验证，支持管理员和用户两种令牌：

```yaml
sky:
  jwt:
    admin-secret-key: itcast
    admin-ttl: 7200000000
    admin-token-name: token
    user-secret-key: itheima
    user-token-name: authentication
    user-ttl: 7200000000
```

## 📦 安装和运行

### 前置要求
- Java 17+
- Maven 3.6+
- MySQL 5.7+
- Redis 5.0+

### 构建项目

```bash
# 克隆仓库
git clone <repository-url>
cd sky-take-out

# 使用 Maven 编译和构建
mvn clean install
```

### 运行应用

```bash
# 方式一：直接运行
cd sky-server
mvn spring-boot:run

# 方式二：运行生成的 JAR 文件
java -jar sky-server/target/sky-server-1.0-SNAPSHOT.jar
```

### Docker 部署

项目包含 Dockerfile 和 docker-compose.yml 配置：

```bash
# 构建 Docker 镜像
docker build -t sky-take-out:1.0 .

# 使用 docker-compose 启动所有服务
docker-compose up -d
```

## 📚 API 文档

本项目集成了 Knife4j（基于 Swagger 3.0）来自动生成 API 文档。

启动应用后，可以通过以下地址访问 API 文档：

```
http://localhost:8080/doc.html
```

## 🔐 安全特性

- **JWT Token 认证**: 基于 JWT 的无状态身份验证
- **AOP 权限控制**: 使用 AspectJ 实现方法级权限控制
- **异常处理**: 统一的异常处理和错误响应
- **数据库连接池**: 通过 Druid 提供监控和安全管理

## 🗄️ 数据库设计

项目使用 MyBatis 作为 ORM 框架，提供的 Mapper 文件包括：

- `AddressBookMapper.xml` - 地址簿映射
- `CategoryMapper.xml` - 分类映射
- `DishMapper.xml` - 菜品映射
- `DishFlavorsMapper.xml` - 菜品口味映射
- `EmployeeMapper.xml` - 员工映射
- `SetmealMapper.xml` - 套餐映射
- `SetMealDishMapper.xml` - 套餐菜品映射
- `ShoppingCartMapper.xml` - 购物车映射
- `OrdersMapper.xml` - 订单映射
- `OrderDetailMapper.xml` - 订单详情映射
- `UserMapper.xml` - 用户映射

## 📝 日志配置

项目配置了分级日志记录，在 `application.yml` 中定义：

```yaml
logging:
  level:
    com.sky.mapper: debug
    com.sky.service: info
    com.sky.controller: info
```

## 🐛 常见问题

### 问题 1: 连接数据库失败
- 确保 MySQL 服务正在运行
- 检查数据库连接配置（主机、端口、用户名、密码）
- 确保数据库已创建

### 问题 2: Redis 连接失败
- 确保 Redis 服务正在运行
- 检查 Redis 连接配置（主机、端口、密码）

### 问题 3: 端口被占用
- 默认服务器端口为 8080，可在 `application.yml` 中修改
- 或使用其他端口运行：`java -jar app.jar --server.port=9090`

## 📄 许可证

本项目采用 MIT 许可证。

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

## 📧 联系方式

如有任何问题或建议，请通过以下方式联系：

- 提交 GitHub Issue
- 发送邮件至项目维护者

---

**最后更新**: 2026年3月31日  
**项目版本**: 1.0-SNAPSHOT  
**开发语言**: Java 17

