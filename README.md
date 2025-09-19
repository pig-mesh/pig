<p align="center">
 <img src="https://img.shields.io/badge/Pig-3.8-success.svg" alt="Build Status">
 <img src="https://img.shields.io/badge/Spring%20Cloud-2023-blue.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-3.3-blue.svg" alt="Downloads">
 <img src="https://img.shields.io/badge/Vue-3.4-blue.svg" alt="Downloads">
 <img src="https://img.shields.io/github/license/pig-mesh/pig"/>
</p>

## 系统说明

- 基于 Spring Cloud 、Spring Boot、 OAuth2 的 RBAC **企业快速开发平台**， 同时支持微服务架构和单体架构
- 提供对 Spring Authorization Server 生产级实践，支持多种安全授权模式
- 提供对常见容器化方案支持 Kubernetes、Rancher2 、Kubesphere、EDAS、SAE 支持

#### 分支说明

- dev: java17/21 + springboot 3.5 + springcloud 2025
- master: java17/21 + springboot 3.5 + springcloud 2025
- java8_eof: java8 + springboot 2.7 + springcloud 2021

#### 文档视频

- 🔥 [ 配套文档 wiki.pig4cloud.com](https://wiki.pig4cloud.com)

#### 其他产品

- 👉🏻 [PIGX 在线体验](http://home.pig4cloud.com:38081)

- 👉🏻 [自研BPMN工作流引擎](http://home.pig4cloud.com:38082)

- 👉🏻 [大模型 RAG 知识库](http://home.pig4cloud.com:38083)

## 微信群 [禁广告]

<img src='https://foruda.gitee.com/images/1731647278591226450/68ad7b55_441246.png' alt='1717136749'/>

## 快速开始

### 核心依赖

| 依赖                   | 版本         |
|----------------------|------------|
| Spring Boot          | 3.5.6      |
| Spring Cloud         | 2025.0.0   |
| Spring Cloud Alibaba | 2023.0.3.3 |
| sa-token             | 1.44.0     |
| Mybatis Plus         | 3.5.14     |
| Vue                  | 3.4        |
| Element Plus         | 2.7        |

### 模块说明

```lua
pig-ui  -- https://gitee.com/log4j/pig-ui

pig
├── pig-boot -- 单体模式启动器[9999]
├── pig-auth -- 授权服务提供[3000]
└── pig-common -- 系统公共模块
     ├── pig-common-bom -- 全局依赖管理控制
     ├── pig-common-core -- 公共工具类核心包
     ├── pig-common-datasource -- 动态数据源包
     ├── pig-common-log -- 日志服务
     ├── pig-common-oss -- 文件上传工具类
     ├── pig-common-mybatis -- mybatis 扩展封装
     ├── pig-common-seata -- 分布式事务
     ├── pig-common-security -- 安全工具类
     ├── pig-common-swagger -- 接口文档
     ├── pig-common-feign -- feign 扩展封装
     └── pig-common-xss -- xss 安全封装
├── pig-register -- Nacos Server[8848]
├── pig-gateway -- Spring Cloud Gateway网关[9999]
└── pig-upms -- 通用用户权限管理模块
     └── pig-upms-api -- 通用用户权限管理系统公共api模块
     └── pig-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
└── pig-visual
     └── pig-monitor -- 服务监控 [5001]
     ├── pig-codegen -- 图形化代码生成 [5002]
     └── pig-quartz -- 定时任务管理台 [5007]
```

### 本地开发 运行

pig 提供了详细的[部署文档 wiki.pig4cloud.com](https://www.yuque.com/pig4cloud/pig/vsdox9)，包括开发环境安装、服务端代码运行、前端代码运行等。

请务必**完全按照**文档部署运行章节 进行操作，减少踩坑弯路！！

### Docker 运行

```
# 下载并运行服务端代码
git clone https://gitee.com/log4j/pig.git -b jdk17

cd pig && mvn clean install && docker compose up -d

# 下载并运行前端UI
git clone https://gitee.com/log4j/pig-ui.git

cd pig-ui && npm install  --registry=https://registry.npmmirror.com

npm install && npm run build:docker && cd docker && docker compose up -d
```

## 免费公开课

<table>
  <tr>
    <td><a href="https://www.bilibili.com/video/av45084065" target="_blank"><img src="https://foruda.gitee.com/images/1731647304254897555/88a9c2fa_441246.jpeg"></a></td>
    <td><a href="https://www.bilibili.com/video/av77344954" target="_blank"><img src="https://foruda.gitee.com/images/1731647324953921510/39689640_441246.jpeg"></a></td>
  </tr>
    <tr>
    <td><a href="https://www.bilibili.com/video/BV1J5411476V" target="_blank"><img src="https://foruda.gitee.com/images/1731647357502030768/7f31f392_441246.jpeg"></a></td>
    <td><a href="https://www.bilibili.com/video/BV14p4y197K5" target="_blank"><img src="https://foruda.gitee.com/images/1731647375444479120/2b8fd494_441246.jpeg"></a></td>
  </tr>
</table>

## 开源共建

### 开源协议

pig 开源软件遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html)。
允许商业使用，但务必保留类作者、Copyright 信息。

![](https://foruda.gitee.com/images/1731647419204307063/91217172_441246.jpeg)

### 其他说明

1. 欢迎提交 [PR](https://dwz.cn/2KURd5Vf)，注意对应提交对应 `dev` 分支
   代码规范 [spring-javaformat](https://github.com/spring-io/spring-javaformat)

   <details>
    <summary>代码规范说明</summary>

    1. 由于 <a href="https://github.com/spring-io/spring-javaformat" target="_blank">spring-javaformat</a>
       强制所有代码按照指定格式排版，未按此要求提交的代码将不能通过合并（打包）
    2. 如果使用 IntelliJ IDEA
       开发，请安装自动格式化软件 <a href="https://repo1.maven.org/maven2/io/spring/javaformat/spring-javaformat-intellij-idea-plugin/" target="_blank">
       spring-javaformat-intellij-idea-plugin</a>
    3. 其他开发工具，请参考 <a href="https://github.com/spring-io/spring-javaformat" target="_blank">
       spring-javaformat</a>
       说明，或`提交代码前`在项目根目录运行下列命令（需要开发者电脑支持`mvn`命令）进行代码格式化
       ```
       mvn spring-javaformat:apply
       ```
   </details>

2. 欢迎提交 [issue](https://gitee.com/log4j/pig/issues)，请写清楚遇到问题的原因、开发环境、复显步骤。
