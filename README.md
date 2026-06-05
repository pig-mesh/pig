<p align="center">
 <img src="https://img.shields.io/badge/Pig-4.0-success.svg" alt="Pig">
 <img src="https://img.shields.io/badge/Spring%20Cloud-2025.1-blue.svg" alt="Spring Cloud">
 <img src="https://img.shields.io/badge/Spring%20Boot-4.0-blue.svg" alt="Spring Boot">
 <img src="https://img.shields.io/badge/Vue-3.5-blue.svg" alt="Vue">
 <img src="https://img.shields.io/github/license/pig-mesh/pig" alt="License">
 <img src="https://gitcode.com/pig-mesh/pig/star/badge.svg" alt="Stars">
</p>

## 系统说明

- Pig 是基于 Spring Cloud、Spring Boot、OAuth2 的 RBAC 企业级快速开发平台，同时支持微服务架构和单体架构。
- 认证中心基于 Spring Authorization Server 落地生产级 OAuth2 实践，支持授权码、密码、刷新令牌等常见登录与授权场景。
- 当前开源版本保留认证、网关、用户权限、监控、代码生成和定时任务等核心能力，移除了商业版中的多租户、数据权限、动态路由、流程、支付、公众号、报表和移动端服务等扩展模块。
- 提供 Docker Compose 本地编排，支持快速启动 MySQL、Redis、Nacos 和业务服务。

## 使用文档

PIG 提供了完整的部署与开发文档：[wiki.pig4cloud.com](https://wiki.pig4cloud.com)，涵盖开发环境配置、服务端启动、前端运行、微服务部署和单体部署等关键步骤。

### 其他产品

- [PIGX 在线体验](http://home.pig4cloud.com:38081)
- [自研 BPMN 工作流引擎](http://home.pig4cloud.com:38082)
- [大模型 RAG 知识库](http://home.pig4cloud.com:38083)

## 快速开始

### 基础环境

- JDK 17+
- Maven 3.9+
- Docker 和 Docker Compose
- Node.js 16+（运行 `pig-ui` 前端时需要）

### 微服务模式

在项目根目录执行完整编译，再构建并启动本地服务栈：

```bash
mvn clean install -T 4 -Pcloud
docker compose build && docker compose up
```

服务启动后，默认通过网关端口 `9999` 访问后端接口，Nacos 控制台端口为 `8848`。

### 单体模式

单体模式通过 `boot` profile 启用 `pig-boot` 模块：

```bash
mvn clean install -T 4 -Pboot
docker compose -f docker-compose-boot.yml build && docker compose -f docker-compose-boot.yml up
```

单体服务默认监听 `9999` 端口。

## 核心依赖

| 依赖 | 版本 |
| --- | --- |
| Pig | 4.0.0 |
| JDK | 17+ |
| Spring Boot | 4.0.6 |
| Spring Cloud | 2025.1.1 |
| Spring Cloud Alibaba | 2025.1.0.0 |
| Spring Security OAuth2 Authorization Server | 7.0.5 |
| MyBatis Plus | 3.5.16 |
| Nacos Client | 3.1.2 |
| Druid | 1.2.28 |
| Vue | 3.5.34 |
| Element Plus | 2.13.7 |
| Vite | 5.4.21 |

## 模块说明

```lua
pig-ui -- https://github.com/pig-mesh/pig-ui

pig
├── pig-register -- Nacos Server [8848/9848/18080]
├── pig-gateway -- Spring Cloud Gateway 网关 [9999]
├── pig-auth -- 授权服务 [3000]
├── pig-upms -- 通用用户权限管理模块
│   ├── pig-upms-api -- 通用用户权限管理公共 API
│   └── pig-upms-biz -- 通用用户权限业务服务 [4000]
├── pig-common -- 系统公共模块
│   ├── pig-common-bom -- 全局依赖版本管理
│   ├── pig-common-core -- 公共工具类核心包
│   ├── pig-common-data -- MyBatis Plus 与缓存扩展
│   ├── pig-common-datasource -- 动态数据源封装
│   ├── pig-common-log -- 日志服务
│   ├── pig-common-oss -- 文件上传工具类
│   ├── pig-common-security -- 安全工具类
│   ├── pig-common-sentinel -- Sentinel 与异常处理封装
│   ├── pig-common-swagger -- 接口文档封装
│   ├── pig-common-feign -- OpenFeign 扩展封装
│   ├── pig-common-excel -- Excel 导入导出封装
│   └── pig-common-xss -- XSS 安全封装
├── pig-visual -- 可视化支撑服务
│   ├── pig-monitor -- 服务监控 [5001]
│   ├── pig-codegen -- 图形化代码生成 [5002]
│   └── pig-quartz -- 定时任务管理台 [5007]
└── pig-boot -- 单体模式启动器 [9999]，通过 `-Pboot` 启用
```

## 配置说明

- 微服务模式使用 `cloud` profile，默认激活 `dev` 环境配置。
- 单体模式使用 `boot` profile，`pig-boot` 模块只在该 profile 下参与构建。
- 网关路由由 `pig-gateway/src/main/resources/application.yml` 和 Nacos 配置维护，不再依赖动态路由表。
- 默认数据库脚本位于 `db/`，业务表初始化到 `pig`，Nacos 配置初始化到 `pig_config`。
- 包名已统一为 `com.pig4cloud.pig`。

## 开源共建

### 开源协议

Pig 开源软件遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html)，允许商业使用，但务必保留类作者、Copyright 信息。

### 其他说明

1. 欢迎提交 [PR](https://github.com/pig-mesh/pig/pulls)，请基于当前开发分支提交。
2. 欢迎提交 [Issue](https://github.com/pig-mesh/pig/issues)，请写清楚问题现象、开发环境和复现步骤。
3. 代码格式遵循 Spring Java Format，提交前可在项目根目录运行：

```bash
mvn spring-javaformat:apply
```
