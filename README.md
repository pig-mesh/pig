 <p align="center">
  <img src="https://img.shields.io/badge/Avue-1.5.0-green.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/Spring%20Cloud-Finchley.SR2-blue.svg" alt="Coverage Status">
   <img src="https://img.shields.io/badge/Spring%20Boot-2.0.8.RELEASE-blue.svg" alt="Downloads">
 </p>  
 
**Pig Microservice Architecture**   
   
- 基于 Spring Cloud Finchley 、Spring Security OAuth2 的RBAC权限管理系统  
- 基于数据驱动视图的理念封装 Element-ui，即使没有 vue 的使用经验也能快速上手  
- 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持  
- 提供 lambda 、stream api 、webflux 的生产实践   


<a href="https://pig4cloud.com/#/doc/pig" target="_blank">部署文档</a> | <a target="_blank" href="https://avue.top"> 前端解决方案</a> | <a target="_blank" href="https://gitee.com/log4j/pig/releases/v1.3.2"> 1.0  版本</a> | <a target="_blank" href="http://pigx.pig4cloud.com"> PigX在线体验</a>
    


   
![](http://a.pigx.top/20190201162417.png?imageView2/2/w/650)   

#### 核心依赖 


依赖 | 版本
---|---
Spring Boot |  2.0.8.RELEASE  
Spring Cloud | Finchley.SR2   
Spring Security OAuth2 | 2.3.3
Mybatis Plus | 3.0.6
hutool | 4.3.3
Avue | 1.5.0
   


#### 模块说明
```lua
pig
├── pig-ui -- 前端工程[8080]
├── pig-auth -- 授权服务提供[3000]
└── pig-common -- 系统公共模块 
     ├── pig-common-core -- 公共工具类核心包
     ├── pig-common-log -- 日志服务
     └── pig-common-security -- 安全工具类
├── pig-config -- 配置中心[8888]
├── pig-eureka -- 服务注册与发现[8761]
├── pig-gateway -- Spring Cloud Gateway网关[9999]
└── pig-upms -- 通用用户权限管理模块
     └── pigx-upms-api -- 通用用户权限管理系统公共api模块
     └── pigx-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
└── pigx-visual  -- 图形化模块 
     ├── pigx-monitor -- Spring Boot Admin监控 [5001]
     └── pigx-codegen -- 图形化代码生成[5003]
	 
```
#### 提交反馈

1. 欢迎提交 issue，请写清楚遇到问题的原因，开发环境，复显步骤。

2. 不接受`功能请求`的 issue，功能请求可能会被直接关闭。  

3. <a href="mailto:wangiegie@gmail.com">wangiegie@gmail.com</a>  
4. <a target="_blank" href="https://jq.qq.com/?_wv=1027&k=5zWEvg5">交流群 23754102</a>   

#### 开源协议
![](http://a.pigx.top/20190201155120.png)


#### 鸣谢 

avue [@smallwei](https://avue.top)    
mica-auto [@dreamlu](https://dreamlu.net)   
bladex [@smallc](http://bladex.vip)  
mybatis-plus [@青苗](http://mp.baomidou.com)     
hutool [@路小磊](https://dreamlu.net)   

