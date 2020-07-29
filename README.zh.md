
**中文** | [English](https://github.com/pigxcloud/pig/blob/master/README.md)

<p align="center">
 <img src="https://img.shields.io/badge/Pig-2.8-success.svg" alt="Build Status">
 <img src="https://img.shields.io/badge/Avue-2.6-green.svg" alt="Build Status">
 <img src="https://img.shields.io/badge/Spring%20Cloud-Hoxto.SR6-blue.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-2.3.RELEASE-blue.svg" alt="Downloads">
</p>
   
- 基于 Spring Cloud Hoxton 、Spring Boot 2.3、 OAuth2 的RBAC权限管理系统  
- 基于数据驱动视图的理念封装 element-ui，即使没有 vue 的使用经验也能快速上手  
- 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持  
- 提供 lambda 、stream api 、webflux 的生产实践   


<a href="http://pig4cloud.com/doc/pig" target="_blank">部署文档</a> | <a target="_blank" href="https://avuejs.com"> 前端解决方案</a> | <a target="_blank" href="http://pigx.pig4cloud.com"> PigX在线体验</a> | <a target="_blank" href="https://paper.pig4cloud.com/"> PigX白皮书</a> | <a target="_blank" href="https://start.pig4cloud.com"> pig4cloud initializr</a>
    

#### 快速构架微服务应用  

```xml
<!-- pig-gen archetype -->
<dependency>
    <groupId>com.pig4cloud.archetype</groupId>
    <artifactId>pig-gen</artifactId>
    <version>last.version</version>
</dependency>
```

<img src="https://images.gitee.com/uploads/images/2019/1026/004238_9a73e1e7_393021.gif"/>  
   
#### 核心依赖 


依赖 | 版本
---|---
Spring Boot |  2.3.2.RELEASE  
Spring Cloud | Hoxton.SR6   
Spring Cloud Alibaba | 2.2.1.RELEASE
Spring Security OAuth2 | 2.3.6
Mybatis Plus | 3.3.2
hutool | 5.3.10
Avue | 2.6.14
   


#### 模块说明
```lua
pig-ui  -- https://gitee.com/log4j/pig-ui

pig
├── pig-auth -- 授权服务提供[3000]
└── pig-common -- 系统公共模块 
     ├── pig-common-core -- 公共工具类核心包
     ├── pig-common-datasource -- 动态数据源包
     ├── pig-common-log -- 日志服务
     ├── pig-common-mybatis -- mybatis 扩展封装
     ├── pig-common-security -- 安全工具类
     ├── pig-common-swagger -- 接口文档
     └── pig-common-sentinel -- sentinel 扩展封装
├── pig-register -- Nacos Server[8848]
├── pig-gateway -- Spring Cloud Gateway网关[9999]
└── pig-upms -- 通用用户权限管理模块
     └── pig-upms-api -- 通用用户权限管理系统公共api模块
     └── pig-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
└── pig-visual
     └── pig-monitor -- 服务监控 [5001]
     ├── pig-codegen -- 图形化代码生成 [5002]
     └── pig-sentinel-dashboard -- 流量高可用 [5003]		 
```
#### 开源共建

1. 欢迎提交 [pull request](https://dwz.cn/2KURd5Vf)，注意对应提交对应 `dev` 分支

2. 欢迎提交 [issue](https://gitee.com/log4j/pig/issues)，请写清楚遇到问题的原因、开发环境、复显步骤。

3. 不接受`功能请求`的 [issue](https://gitee.com/log4j/pig/issues)，功能请求可能会被直接关闭。  

4. mail: <a href="mailto:pig4cloud@qq.com">pig4cloud@qq.com</a>     

#### 免费公开课

<table>
  <tr>
    <td><a href="https://www.bilibili.com/video/av45084065" target="_blank"><img src="https://images.gitee.com/uploads/images/2020/0318/215612_b3d0375d_1824312.jpeg"></a></td>
    <td><a href="https://www.bilibili.com/video/av77344954" target="_blank"><img src="https://images.gitee.com/uploads/images/2020/0318/215612_f6d298c4_1824312.jpeg"></a></td>
  </tr>
    <tr>
    <td><a href="https://www.bilibili.com/video/BV1J5411476V" target="_blank"><img src="http://pigx.vip/20200504210206_YDYTOA_k3s.jpeg"></a></td>
    <td><a href="https://www.bilibili.com/video/BV14p4y197K5" target="_blank"><img src="http://pigx.vip/20200504210257_yXZBSx_zerotier.jpeg"></a></td>
  </tr>
</table>

#### 交流群
![输入图片说明](https://images.gitee.com/uploads/images/2020/0519/142843_6032c52e_410595.png "qr.png")


#### Star 历史

[![Stargazers over time](https://starchart.cc/pigxcloud/pig.svg)](https://starchart.cc/pigxcloud/pig.svg)
