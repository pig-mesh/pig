
[中文](https://github.com/pigxcloud/pig/blob/master/README.zh.md) | **English**


<p align="center">
 <img src="https://img.shields.io/badge/Pig-2.9-success.svg" alt="Build Status">
 <img src="https://img.shields.io/badge/Avue-2.6-green.svg" alt="Build Status">
 <img src="https://img.shields.io/badge/Spring%20Cloud-Hoxto.SR8-blue.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-2.3.RELEASE-blue.svg" alt="Downloads">
</p>
   
- based on Spring Cloud Hoxton 、Spring Boot 2.3、 OAuth2  RBAC web framework
- Idea based on data-driven view,  if you don't use vue, you can get started quickly 
- provide support for common containers like Docker、Kubernetes、Rancher2 
- provide lambda 、stream api 、webflux production practice   


<a href="https://wiki.pig4cloud.com" target="_blank">Documentation</a> | <a target="_blank" href="http://pigx.pig4cloud.com"> PigX Online</a> | <a target="_blank" href="https://paper.pig4cloud.com/"> PigX Paper</a> | <a target="_blank" href="https://start.pig4cloud.com"> pig4cloud initializr</a>
    

#### Quickly structure microservice applications  

```xml
<!-- pig-gen archetype -->
<dependency>
    <groupId>com.pig4cloud.archetype</groupId>
    <artifactId>pig-gen</artifactId>
    <version>last.version</version>
</dependency>
```

<img src="https://images.gitee.com/uploads/images/2019/1026/004238_9a73e1e7_393021.gif"/>  
   
#### Core dependencies 


dependencies | version
---|---
Spring Boot |  2.3.4.RELEASE  
Spring Cloud | Hoxton.SR8  
Spring Cloud Alibaba | 2.2.3.RELEASE
Spring Security OAuth2 | 2.3.6
Mybatis Plus | 3.4.0
hutool | 5.4.3
Avue | 2.6.15
   


#### Module description

```lua
pig-ui  -- https://github.com/pigxcloud/pig-ui

pig
├── pig-auth -- oauth-server[3000]
└── pig-common 
     ├── pig-common-core -- tool core package
     ├── pig-common-datasource -- dynamic data source package
     ├── pig-common-log -- Log service package
     ├── pig-common-job -- xxl-job expand
     ├── pig-common-mybatis -- mybatis expand
     ├── pig-common-security -- security tools
     ├── pig-common-swagger -- api documentation
     └── pig-common-sentinel -- sentinel auto fallbak
├── pig-register -- nacos server[8848]
├── pig-gateway -- spring cloud gateway[9999]
└── pig-upms
     └── pig-upms-api -- user management system api
     └── pig-upms-biz -- user management system biz[4000]
└── pig-visual
     └── pig-monitor -- spring boot admin[5001]
     ├── pig-codegen -- graphical code generation[5002]
     ├── pig-sentinel-dashboard -- sentinel dashboard [5003]
     └── pig-xxl-job-admin -- xxl-job-admin [5004]	 
```
#### Open source co-construction

1. Welcome to submit [pull request](https://dwz.cn/2KURd5Vf)，note correspondence `dev` branch

2. Welcome to submit [issue](https://gitee.com/log4j/pig/issues)，Please clearly write down the cause of the problem, the development environment, and the steps to reproduce.

3. mail: <a href="mailto:pig4cloud@qq.com">pig4cloud@qq.com</a>     

#### License

pig is Open Source software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).

#### Free Class

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

#### Chat Group

![](https://gitee.com/pig4cloud/oss/raw/master/2020-9/20200901133142.png)

#### Star history

[![Stargazers over time](https://starchart.cc/pigxcloud/pig.svg)](https://starchart.cc/pigxcloud/pig.svg)
