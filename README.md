2017年11月1日   1.0-ALPHA
Future 整合ele-admin前后端分离


## 权限设计
基于spring security oauth2.0 + jwt的认证、鉴权的网关设计
![image](http://obq1lvsd9.bkt.clouddn.com/pigpermission.png)

如图
1. 用户发送获取token 的请求（密码模式）
2. 网关将请求转发到认证服务器（auth-server）
3. 认证服务器通过调用用户模块，判断下用户上送的信息是否正确
4. 用户发送资源请求到网关
5. 网关根据token去资源服务器获取用户的（user-info）
6. 根据用户信息调用用户模块（getUrls），进行权限判断

### 特点

- 业务模块不涉及oauth2.0，认证鉴权全部在网关模块
- 业务模块可以直接根据网关转发过来的请求解析请求头中jwt信息，进行当前用户身份确认
## 如何使用：
### 开发环境
- spring Boot 1.5.7
- spring Cloud Dalston.SR4
- maven 3.3+
- Redis 
- MySQL 5.7
### 配置修改

- fock https://github.com/jieblog/pig-config.git 自行修改各个模块的 mysql、redis配置。
- 修改配置中心的git地址

```
spring:
  application:
    name: pig-config-server
  cloud:
    config:
      server:
        git:
          uri: 配置fork 的git厂库
```

### 开始使用

### 通过网关访问auth-server 获取access-token

```
// cGlnOnBpZw==  是Base64(clientId:secret) 默认在auth模块的配置里面pig:pig
 curl -H "Authorization:Basic cGlnOnBpZw==" -d "grant_type=password&scope=server&username=admin&password=admin" http://localhost:1000/auth/oauth/token
 
   
 {"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MDk1NzA0NjMsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiZWMwZmJhMjYtMGJkZS00YjY2LThhZTQtZGRmYTNiMzkxZGM5IiwiY2xpZW50X2lkIjoicGlnIiwic2NvcGUiOlsic2VydmVyIl19.ZoSU_4NhdolnV6ZsNaSXITC_pewUDiaqZPLoESu9f9s","token_type":"bearer","refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MDk1NzA0NjMsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiZWMwZmJhMjYtMGJkZS00YjY2LThhZTQtZGRmYTNiMzkxZGM5IiwiY2xpZW50X2lkIjoicGlnIiwic2NvcGUiOlsic2VydmVyIl19.ZoSU_4NhdolnV6ZsNaSXITC_pewUDiaqZPLoESu9f9s","expires_in":3600,"scope":"server"}

```
### 通过access-token 访问受保护的资源
```
curl -H "Authorization:Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MDk1NzA0NjMsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiZWMwZmJhMjYtMGJkZS00YjY2LThhZTQtZGRmYTNiMzkxZGM5IiwiY2xpZW50X2lkIjoicGlnIiwic2NvcGUiOlsic2VydmVyIl19.ZoSU_4NhdolnV6ZsNaSXITC_pewUDiaqZPLoESu9f9s" http://localhost:1000/admin/user

```