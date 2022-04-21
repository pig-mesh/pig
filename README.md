<p align="center">
 <img src="https://img.shields.io/badge/Pig-3.4-success.svg" alt="Build Status">
 <img src="https://img.shields.io/badge/Spring%20Cloud-2021-blue.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-2.6-blue.svg" alt="Downloads">
 <img src="https://img.shields.io/github/license/pig-mesh/pig"/>
</p>

## 系统说明

- 基于 Spring Cloud 2021 、Spring Boot 2.6、 OAuth2 的 RBAC **权限管理系统**
- 基于数据驱动视图的理念封装 element-ui，即使没有 vue 的使用经验也能快速上手
- 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持
- 提供 lambda 、stream api 、webflux 的生产实践

## 文档视频

[ 配套文档 wiki.pig4cloud.com](https://wiki.pig4cloud.com)

[ 配套视频 tv.pig4cloud.com](https://www.bilibili.com/video/BV12t411B7e9)

[PIGX 在线体验 pigx.pig4cloud.com](http://pigx.pig4cloud.com)

[产品白皮书 paper.pig4cloud.com](https://paper.pig4cloud.com)

## 微信群 [禁广告]

![](https://minio.pigx.vip/oss/1648184189.png)

## 快速开始

### 核心依赖

| 依赖                   | 版本         |
| ---------------------- |------------|
| Spring Boot            | 2.6.7      |
| Spring Cloud           | 2021.0.1   |
| Spring Cloud Alibaba   | 2021.0.1.0 |
| Spring Security OAuth2 | 2.3.6      |
| Mybatis Plus           | 3.5.1      |
| hutool                 | 5.7.22     |
| Avue                   | 2.6.18     |

### 模块说明

```lua
pig-ui  -- https://gitee.com/log4j/pig-ui

pig
├── pig-auth -- 授权服务提供[3000]
└── pig-common -- 系统公共模块
     ├── pig-common-bom -- 全局依赖管理控制
     ├── pig-common-core -- 公共工具类核心包
     ├── pig-common-datasource -- 动态数据源包
     ├── pig-common-job -- xxl-job 封装
     ├── pig-common-log -- 日志服务
     ├── pig-common-mybatis -- mybatis 扩展封装
     ├── pig-common-seata -- 分布式事务
     ├── pig-common-security -- 安全工具类
     ├── pig-common-swagger -- 接口文档
     ├── pig-common-feign -- feign 扩展封装
     └── pig-common-test -- oauth2.0 单元测试扩展封装
├── pig-register -- Nacos Server[8848]
├── pig-gateway -- Spring Cloud Gateway网关[9999]
└── pig-upms -- 通用用户权限管理模块
     └── pig-upms-api -- 通用用户权限管理系统公共api模块
     └── pig-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
└── pig-visual
     └── pig-monitor -- 服务监控 [5001]
     ├── pig-codegen -- 图形化代码生成 [5002]
     ├── pig-sentinel-dashboard -- 流量高可用 [5003]
     └── pig-xxl-job-admin -- 分布式定时任务管理台 [5004]
```

### 本地开发 运行

pig 提供了详细的[部署文档 wiki.pig4cloud.com](https://www.yuque.com/pig4cloud/pig/vsdox9)，包括开发环境安装、服务端代码运行、前端代码运行等。

请务必**完全按照**文档部署运行章节 进行操作，减少踩坑弯路！！

### 定制自己微服务

[PIG DIY](https://diy.pig4cloud.com)

[PIG ARCHETYPE](https://archetype.pig4cloud.com)

### Docker 运行

```
# 下载并运行服务端代码
git clone https://gitee.com/log4j/pig.git

cd pig && mvn clean install && docker-compose up -d

# 下载并运行前端UI
git clone https://gitee.com/log4j/pig-ui.git

cd pig-ui && npm install -g cnpm --registry=https://registry.npm.taobao.org


cnpm install && cnpm run build:docker && cd docker && docker-compose up -d
```

## 免费公开课

<table>
  <tr>
    <td><a href="https://www.bilibili.com/video/av45084065" target="_blank"><img src="https://gitee.com/pig4cloud/oss/raw/master/2020-9/20200901133006.png"></a></td>
    <td><a href="https://www.bilibili.com/video/av77344954" target="_blank"><img src="https://gitee.com/pig4cloud/oss/raw/master/2020-9/20200901133059.png"></a></td>
  </tr>
    <tr>
    <td><a href="https://www.bilibili.com/video/BV1J5411476V" target="_blank"><img src="https://gitee.com/pig4cloud/oss/raw/master/2020-9/20200901133114.png"></a></td>
    <td><a href="https://www.bilibili.com/video/BV14p4y197K5" target="_blank"><img src="https://gitee.com/pig4cloud/oss/raw/master/2020-9/20200901133124.png"></a></td>
  </tr>
</table>

## 开源共建

### 开源协议

pig 开源软件遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html)。
允许商业使用，但务必保留类作者、Copyright 信息。

![](https://gitee.com/pig4cloud/oss/raw/master/2020-10-9/1602229452602-image.png)

### 其他说明

1. 欢迎提交 [PR](https://dwz.cn/2KURd5Vf)，注意对应提交对应 `dev` 分支
   代码规范 [spring-javaformat](https://github.com/spring-io/spring-javaformat)

2. 欢迎提交 [issue](https://gitee.com/log4j/pig/issues)，请写清楚遇到问题的原因、开发环境、复显步骤。

3. 联系作者 <a href="mailto:pig4cloud@qq.com">pig4cloud@qq.com</a>

[![Stargazers over time](https://whnb.wang/img/log4j/pig?e=604800)](https://whnb.wang/log4j/pig?e=604800)

