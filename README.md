<h3 align="center">Pig Microservice Architecture</h2> 
<p align="center">
 <img src="https://img.shields.io/circleci/project/vuejs/vue/dev.svg" alt="Build Status">
  <img src="https://img.shields.io/badge/Spring%20Cloud-EdgwareSR4-orange.svg" alt="Coverage Status">
  <img src="https://img.shields.io/badge/Spring%20Boot-1.5.13-blue.svg" alt="Downloads">
  <img src="https://img.shields.io/npm/v/npm.svg" alt="Version">
  <img src="https://img.shields.io/npm/l/vue.svg" alt="License">
</p>

## 简介
- [项目官网](https://pig4cloud.com) 

- [入门视频](https://www.bilibili.com/video/av20229859/)  

- [部署文档](https://www.kancloud.cn/lengleng/pig-guide/550736)  

- [问题反馈](https://gitee.com/log4j/pig/issues)  

- [pig-ui](https://gitee.com/log4j/pig-ui)、[pig-config](https://gitee.com/cqzqxq_lxh/pig-config)

- <a target="_blank" href="https://jq.qq.com/?_wv=1027&k=5zWEvg5">交流群：23754102 （人数有限，需要Spring Cloud 基础，小白必清，谢谢！）</a>   


## pigX
- 全网最新的Cloud 权限系统
- 基于Spring Boot 2.0.3.RELEASE
- 基于Spring Cloud  Finchley.RELEASE
- 网关基于 Spring Cloud Gateway
- 提供Consul 服务注册发现版本pigxc
- 完整的OAuth 2.0 流程，资源服务器控制权限
- 去除了部分对于开发不友好的中间件,快速上手   
[源码获取](https://gitee.com/log4j/pig/wikis/pigx)
### 功能
- 完善登录：账号密码模式、短信验证码模式、社交账号模式均整合Spring security oAuth
- 单点登录：基于Srping security oAuth 提供单点登录接口，方便其他系统对接
- 用户管理：用户是系统操作者，该功能主要完成系统用户配置。
- 机构管理：配置系统组织机构，树结构展现，可随意调整上下级。
- 菜单管理：配置系统菜单，操作权限，按钮权限标识等。
- 角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
- 动态路由：基于zuul实现动态路由，后端可配置化
- 终端管理：动态配置oauth终端，后端可配置化
- 字典管理：对系统中经常使用的一些较为固定的数据进行维护，如：是否等。
- 操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
- 服务限流：多种维度的流量控制（服务、IP、用户等）
- 消息总线：配置动态实时刷新
- 分库分表：shardingdbc分库分表策略
- 数据权限: 使用mybatis对原查询做增强，业务代码不用控制，即可实现。
- 文件系统: 支持FastDFS、七牛云，扩展API几行代码实现上传下载
- 消息中心：短信、邮件模板发送，几行代码实现发送
- 聚合文档：基于zuul实现 swagger各个模块的实现
- 代码生成：前后端代码的生成，支持Vue
- 缓存管理：基于Cache Cloud 保证Redis 的高可用
- 服务监控: Spring Boot Admin
- 分布式任务调度： 基于elastic-job的分布式文件系统，zookeeper做调度中心
- zipkin链路追踪： 数据保存ELK，图形化展示
- pinpoint链路追踪： 数据保存hbase，图形化展示
 ### 模块
``` lua
pig
├── pig-ui -- element-vue-admin实现[9528]
├── pig-auth -- 授权服务提供[3000]
├── pig-common -- 系统公共模块 
├── pig-config -- 配置中心[4001]
├── pig-eureka -- 服务注册与发现[1025]
├── pig-gateway -- ZUUL网关[9999]
├── pig-modules -- 微服务模块
├    ├── pig-daemon-service -- 分布式调度中心[4060]
├    ├── pig-mc-service -- 消息中心[4050]
├    ├── pig-sso-client-demo -- 单点登录客户端示例[4040]
├    └── pig-upms-service -- 权限管理提供[4000]
└── pig-visual  -- 图形化模块 
     ├── pig-monitor -- 服务状态监控、turbine [5001]
     ├── pig-zipkin-elk -- zipkin、ELK监控[5002、5601]
     └── pig-cache-cloud -- 缓存管理、统一监控[5005]
```

### 截图 （点击可大图预览）
<table>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/login.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/1.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/2.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/3.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/4.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/5.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/6.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/7.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/12321.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/WX20180522-182107@2x.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/8.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/9.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/10.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/11.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/12.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/13.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/14.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/15.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oss.pig4cloud.com/pic/201806/16.png"/></td>
        <td><img src="https://oss.pig4cloud.com/pic/201806/17.png"/></td>
    </tr>
</table>
