# pig-ui https证书配置和使用教程
csdn文档：https://blog.csdn.net/if_deram/article/details/118443403
**须知:** centos部署参考https://www.yuque.com/pig4cloud/pig/ggffp7 ,此文档只提供https=》前端和nginx部署

> 必须拥有域名,并且域名备案解析到服务器,教程以阿里云为主,腾讯云等云服务配置大同小异

## 1. 登录阿里云

点击链接登录阿里云 https://account.aliyun.com/login/login.htm  ,输入用户名密码登录

## 2. 购买ssl免费证书

### 2.1 首先搜索框中输入ssl,点击搜索或回车

**注：如若域名和服务器不在同一运营商，须在云服务器所在运营商进行购买配置ssl证书**

![image-20210630172121982](https://www.xingyijiankang.com/pig/img/image-20210630172121982.png)

### 2.2 弹出ssl证书相关内容,点击立即购买证书->选购证书

![image-20210630172232250](https://www.xingyijiankang.com/pig/img/image-20210630172232250.png)

### 2.3 选择商品属性并点击立即购买

![image-20210630174600004](https://www.xingyijiankang.com/pig/img/image-20210630174600004.png)

## 3. ssl证书申请

### 3.1 进去证书管理控制台,可以回到首页搜索ssl,选择管理控制台

![image-20210630174945686](https://www.xingyijiankang.com/pig/img/image-20210630174945686.png)

### 3.2 点击左侧SSL证书,右侧的证书申请

> **注意:免费证书可以为你域名提供https的服务,但是不能为你的2级域名,3级域名提供,只限于你的1级域名使用**

![image-20210630175518787](https://www.xingyijiankang.com/pig/img/image-20210630175518787.png)

### 3.3 点击创建证书并且填写认证申请信息

**大概几分钟就可以申请通过**

* 点击申请

  ![image-20210630180048714](https://www.xingyijiankang.com/pig/img/image-20210630180048714.png)

* 填写信息
  **注： 域名验证方式：如果您绑定的域名在本账号下，可以使用自动验证**
  
  >  推荐选择

  **域名验证方式** ->手工DNS验证       **CSR生成方式** ->系统生成
  
  * (推荐)填写基本信息：**域名验证方式** ->自动DNS验证，**CSR生成方式** ->系统生成
 
    ![image-20210630180207905](https://www.xingyijiankang.com/pig/img/image-20210630180207905.png)
  
  * （不推荐）填写基本信息：**域名验证方式** ->自动DNS验证， **CSR生成方式** ->手动填写
    
    > 注意：手动填写CSR的证书不支持部署到阿里云产品。
    > 在制作CSR文件时请务必保存好您的私钥文件。私钥和SSL证书一一对应，一旦丢失了私钥，您的数字证书也将不可使用
    
    **手动生成证书的密钥和CSR文件,
    window下默认提供Keytool工具生成CSR文件**
    
    创建一个文件夹，在该文件夹下执行如下命令
 
    1. 执行以下命令，生成keystore证书文件
    
        ```shell
          keytool -genkey -alias [$Alias] -keyalg RSA -keysize 2048 -storepass [123456] -keystore [$Keytool_Path] 
        ```

        > 说明
        >
        >  -alias: 证书别名，[$Alias]可自定义
        >
        >  -keyalg: 使用的RSA算法，不可以修改
        >
        >  -keysize: 密钥长度为2048bit，不可以修改
        >
        >  -storepass: 密钥库口令，自定义
        >
        >  -keystore: 密钥库文件名
        >
        >  -Keytool_Path: 证书文件保存路径，默认当前路径
        
    2. 根据系统返回的提示，输入生成CSR文件所需的信息。以下是关于提示的说明：
    
            您的名字与姓氏是什么?(first and last name)：申请证书的域名。
            您的组织单位名称是什么?(name of your organizational unit)：部门名称。
            您的组织名称是什么?(name of your organization)：公司名称。
            您所在的城市或区域名称是什么?(name of your City or Locality)：城市名称。
            您所在的城市或区域名称是什么?(name of your State or Province)：州名或省份名称。
            该单位的双字母国家/地区代码是什么?(two-letter country code for this unit)：两位字符的ISO国家代码。
            
    3. 确认输入内容是否正确，输入Y表示正确
    
          ![image-20210630180207905](https://www.xingyijiankang.com/pig/img/image_20210703163909.png)
           
    4. 执行以下命令，生成CSR文件
    
            ```shell
                keytool -certreq -sigalg SHA256withRSA -alias [$Alias] -storepass [123456] -keystore [$Keytool_Path] -file [$Keytool_CSR]
            ```
         
       >说明：
       >
       >   -sigalg: 摘要算法
       >
       >   [$Keytool_CSR]：CSR文件存放路径
       >
       >   -alias、-storepass、-keystore:与keystore证书文件一致
       
        ![image-20210630180207905](https://www.xingyijiankang.com/pig/img/image_20210703164115.png)
        
    5. 打开CSR文件，复制进`CSR文件内容`，点击确定
    
        如果生成keystore证书填写域名和当前认证域名不一致，或修改加密位数或算法报如下错误，解决方案：`检查域名是否一直，输入文件是否按照要求`
        
          ![image-20210630180207905](https://www.xingyijiankang.com/pig/img/image_20210703163922.png)
          
  * 域名验证,提交审核

    ![image-20210701093145300](https://www.xingyijiankang.com/pig/img/image-20210701093145300.png)

## 4. 证书下载

等待状态变为**以签发** ,你就可以点击下载,只需要下载nginx证书

**下载内容为:** 此证书信息和密钥

* 点击下载

  ![image-20210630201101990](https://www.xingyijiankang.com/pig/img/image-20210630201101990.png)

* 下载nginx

![image-20210630201206960](https://www.xingyijiankang.com/pig/img/image-20210630201206960.png)

* 开放安全组，默认没有开始443安全组，需开启

  1. 点击**`实例`** ，在选择你的服务器

  ![image-20210702093422557](https://www.xingyijiankang.com/pig/img/image-20210702093422557.png)

  2. 点击**`安全组`** ，在点击**`内网入方向全部规则`** 查看是否开启443端口，如果没有开启，请选择**`安全组`** 列表**`加入安全组`** 并**`配置规则`**

  ![image-20210702093622581](https://www.xingyijiankang.com/pig/img/image-20210702093622581.png)

  ​	![image-20210702093929827](https://www.xingyijiankang.com/pig/img/image-20210702093929827.png)

  ![image-20210702094036002](https://www.xingyijiankang.com/pig/img/image-20210702094036002.png)
  

## 5. 拷贝证书文件

进入服务器nginx下conf目录创建cert并把ssl证书 *.pem 和 私钥 *.key拷贝到这个目录

> 注:文件可以放在服务器任意目录下

```shell
#当前我nginx安装在/usr/local目录下,一般软件都建议安装在此目录下
mkdir /usr/local/nginx/conf/cert -p
```



##  6. (可选)nginx部署ssl模块

**注：** 通过tar包安装nginx需部署ssl模块

### 6.1 查看nginx是否有ssl模块,如果没有则安装

* (可选)nginx全局配置下执行-V命令查看是否用ssl模块

* (可选)在nginx安装目录下的**sbin目录** 下查看

```shell
#进入nginx安装目录下的sbin
cd /usr/local/nginx/sbin
#执行./nginx -V命令
./nginx -V
#以下是返回值
# configure arguments: --prefix=/usr/local/nginx --pid-path=/var/run/nginx/nginx.pid --lock-path=/var/lock/nginx.lock --error-log-path=/var/log/nginx/error.log --http-log-path=/var/log/nginx/access.log --with-http_gzip_static_module --http-client-body-temp-path=/var/temp/nginx/client --http-proxy-temp-path=/var/temp/nginx/proxy --http-fastcgi-temp-path=/var/temp/nginx/fastcgi --http-uwsgi-temp-path=/var/temp/nginx/uwsgi --http-scgi-temp-path=/var/temp/nginx/scgi
```

* (可选)进入到nginx解压的文件夹objs文件夹下验证是否拥有ssl,演示解压文件在/home/software目录下

```shell
#进入software目录
cd /home/software
#进入nginx解压目录下objs目录,nginx模块都会存放在objs文件夹下
cd nginx/objs
#进入ngx_modules.c文件搜索ssl验证是否存在
vi ngx_modules.c
#输入/ssl查找
/ssl
```

* 未查找到,返回到nginx解压目录,新增ssl模块

```shell
#进入到nginx解压目录
cd cd /home/software/nginx
#执行命令,--prefix为你nginx安装目录
./configure \
--prefix=/usr/local/nginx \
--pid-path=/var/run/nginx/nginx.pid \
--lock-path=/var/lock/nginx.lock \
--error-log-path=/var/log/nginx/error.log \
--http-log-path=/var/log/nginx/access.log \
--with-http_gzip_static_module \
--http-client-body-temp-path=/var/temp/nginx/client \
--http-proxy-temp-path=/var/temp/nginx/proxy \
--http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
--http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
--http-scgi-temp-path=/var/temp/nginx/scgi \
--with-http_ssl_module
```

* 执行编译并且安装

```shell
make
make install
```

### 6.2 检测是否安装成功(2种方案可选)

1. 通过查看解压**nginx/objs目录下ngx_modules.c** 是否安装ssl模块

   ```shell
   #进入software目录
   cd /home/software
   #进入objs目录,nginx模块都会存放在objs文件夹下
   cd nginx/objs
   #进入ngx_modules.c文件搜索ssl验证是否存在
   vi ngx_modules.c
   #输入/ssl查找模块
   /ssl
   ```

2. 通过nginx安装目录sbin下执行**./nginx -V** 查看

   ```shell
   #进入安装目录sbin文件夹下
   cd /usr/local/nginx/sbin
   #执行./nginx -V命令
   ./nginx -V
   #以下是返回值,已经拥有with-http_ssl_module模块
   # configure arguments: --prefix=/usr/local/nginx --pid-path=/var/run/nginx/nginx.pid --lock-path=/var/lock/nginx.lock --error-log-path=/var/log/nginx/error.log --http-log-path=/var/log/nginx/access.log --with-http_gzip_static_module --http-client-body-temp-path=/var/temp/nginx/client --http-proxy-temp-path=/var/temp/nginx/proxy --http-fastcgi-temp-path=/var/temp/nginx/fastcgi --http-uwsgi-temp-path=/var/temp/nginx/uwsgi --http-scgi-temp-path=/var/temp/nginx/scgi --with-http_ssl_module
   ```

## 7. 前端部署

* 通过tar安装nginx方式

```shell
#进入nginx安装目录下conf文件
cd /usr/local/nginx/conf/
#修改nginx配置文件
vi nginx.conf

user  root;
worker_processes  auto;

events {
    worker_connections  1024;
}


http {
    server {
        #如果您使用Nginx 1.15.0及以上版本，请使用listen 443 ssl代替listen 443和ssl on
        listen 443 ssl;
        #listen 443;
        # 开启ssl
        #ssl on;
        #替换你自己的域名
        server_name www.domian.com domian.com;
        # 配置ssl证书,替换你自己下载的nginx证书,可以是相对路径或绝对路径
        ssl_certificate cert/5887362_www.yourpem.com.pem;
        # 配置证书秘钥,系统生成=》替换你自己下载的nginx密钥,手动填写=》需要你通过openssl生成密钥上传，可以是相对路径或绝对路径
        ssl_certificate_key cert/5887362_www.yourkey.com.key;
        # ssl会话cache
        ssl_session_cache shared:SSL:1m;
        # ssl会话超时时间
        ssl_session_timeout 5m;
        # 配置加密套件，写法遵循 openssl 标准
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
        #表示使用的TLS协议的类型
        ssl_prefer_server_ciphers on;
        root /data/pig-ui;
        index index.html;
        location / {
            root /data/pig-ui;
            index index.html;
        }
        # 注意维护新增微服务，gateway 路由前缀
            location ~* ^/(code|auth|admin|gen) {
                proxy_pass http://127.0.0.1:9999;
                proxy_set_header   Host              $host;
                proxy_set_header   X-Forwarded-Proto $scheme;
                add_header Cache-Control no-store;
                proxy_connect_timeout 15s;
                proxy_send_timeout 15s;
                proxy_read_timeout 15s;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto http;
        }
        server{
            listen 80;
            #换成你的域名
            server_name  www.domian.com domian.com;
            #重定向到https
            return 301 https://$server_name$request_uri;
        }
    }
}

#启动nginx，进入nginx安装目录下的sbin
cd /usr/local/nginx/sbin
#启动nginx
./nginx
```



* 通过yum命令安装nginx方式

```shell
mkdir -p /data/pig-ui && cp -r dist/* /data/pig-ui

cd /etc/nginx/conf.d && rm -f default.conf

vim pigx.conf

server {
    #如果您使用Nginx 1.15.0及以上版本，请使用listen 443 ssl代替listen 443和ssl on
	listen 443 ssl;
	#listen 443;
	# 开启ssl
	#ssl on;
	#替换你自己的域名
	server_name www.domian.com domian.com;
	# 配置ssl证书,替换你自己下载的nginx证书,可以是相对路径或绝对路径
	ssl_certificate cert/5887362_www.yourpem.com.pem;
	# 配置证书秘钥,系统生成=》替换你自己下载的nginx密钥,手动填写=》需要你通过openssl生成密钥上传，
	ssl_certificate_key cert/5887362_www.yourkey.com.key;
	# ssl会话cache
	ssl_session_cache shared:SSL:1m;
	# ssl会话超时时间
	ssl_session_timeout 5m;
	# 配置加密套件，写法遵循 openssl 标准
	ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
	ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
	#表示使用的TLS协议的类型
	ssl_prefer_server_ciphers on;
	root /data/pig-ui;
	index index.html;
	location / {
        root /data/pig-ui;
		index index.html;
    }
	# 注意维护新增微服务，gateway 路由前缀
    location ~* ^/(code|auth|admin|gen) {
       	proxy_pass http://127.0.0.1:9999;
       	proxy_set_header   Host              $host;
		proxy_set_header   X-Forwarded-Proto $scheme;
		add_header Cache-Control no-store;
		proxy_connect_timeout 15s;
       	proxy_send_timeout 15s;
       	proxy_read_timeout 15s;
       	proxy_set_header X-Real-IP $remote_addr;
       	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       	proxy_set_header X-Forwarded-Proto http;
    }
}
server {
    #监听80端口
	listen 80;
	#替换自己的域名
    server_name  www.domain.com domain.com;
    #开启https，http失效需重定向到https
	return 301 https://$server_name$request_uri;
}

#启动nginx
nginx
```



## 8.  验证是否成功

```shell
#浏览器输入地址验证domain换成你的ip地址
https://www.domian.com
```



# 可参考文档

阿里云申请配置https参考步骤

https://help.aliyun.com/video_detail/212905.html?spm=a2c4g.11186623.2.2.20fa1a26hDPFQM

华为云申请免费ssl步骤

https://support.huaweicloud.com/qs-ccm/ccm_07_0012.html

腾讯云申请免费ssl步骤

1. 申请

   https://cloud.tencent.com/document/product/400/6814

2. 验证域名

   https://support.huaweicloud.com/ccm_faq/ccm_01_0268.html#ccm_01_0268__fig1141255248

