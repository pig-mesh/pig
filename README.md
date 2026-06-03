## Pig

Pig 是面向快速部署的开源权限管理与微服务基础脚手架。本仓库保留认证、网关、用户权限、监控、代码生成和定时任务等核心能力，移除了商业版中的多租户、数据权限、动态路由、流程、支付、公众号、报表和移动端服务等扩展模块。

## 模块说明

- `pig-boot`：单体模式启动器，默认端口 `9999`。
- `pig-register`：Nacos Server，默认端口 `8848`。
- `pig-gateway`：Spring Cloud Gateway 网关，默认端口 `9999`。
- `pig-auth`：授权服务，默认端口 `3000`。
- `pig-upms`：通用用户权限管理模块。
- `pig-upms/pig-upms-api`：用户权限公共 API。
- `pig-upms/pig-upms-biz`：用户权限业务服务，默认端口 `4000`。
- `pig-common`：系统公共模块。
- `pig-visual/pig-monitor`：服务监控，默认端口 `5001`。
- `pig-visual/pig-codegen`：图形化代码生成，默认端口 `5002`。
- `pig-visual/pig-quartz`：定时任务管理台，默认端口 `5007`。

## 快速启动

### 基础环境

- JDK 17+
- Maven 3.9+
- Docker 和 Docker Compose

### 使用 Docker Compose 启动

```bash
docker compose up -d pig-mysql pig-redis pig-register
docker compose up -d pig-gateway pig-auth pig-upms pig-monitor pig-quartz pig-codegen
```

### 单体模式

```bash
docker compose -f docker-compose-boot.yml up -d pig-mysql pig-redis
mvn -pl pig-boot -am spring-boot:run
```

### 编译验证

```bash
mvn -T 1C clean install -DskipTests
```

## 配置说明

- 网关路由由 `pig-gateway/src/main/resources/application.yml` 和 Nacos 配置维护，不再依赖动态路由表。
- 默认数据库脚本位于 `db/`，只保留 `pig.sql` 和 `pig_config.sql`；业务表统一初始化到 `pig`，Nacos 配置初始化到 `pig_config`。
- 包名已统一为 `com.pig4cloud.pig`。

## 许可证

本项目采用 Apache License 2.0，详见 `LICENSE`。
