### avue1.2.1发布
欢迎加入QQ交流群，互相学习   
前端avue交流群：606410437  
后台微服务群：23754102   
服务端解决方案：[https://gitee.com/log4j/pig](https://gitee.com/log4j/pig)

#### 技术文档
- [avue技术文档](https://www.kancloud.cn/smallwei/avue/)

#### 源码地址
- [码云地址:https://gitee.com/smallweigit/avue](https://gitee.com/smallweigit/avue)
- [github地址：https://github.com/nmxiaowei/avue](https://github.com/nmxiaowei/avue)

#### 更新日志
- [更新日志](./UPDATE.md)

#### 在线demo
- [crud和form组件(html-demo)](http://sandbox.runjs.cn/show/xjjyj1cj)
- [演示地址1:http://avue.2bugs.cn](http://avue.2bugs.cn)
- [演示地址2:http://122.4.247.156:7777](http://122.4.247.156:7777)——jenkins+docker+git钩子自动部署

#### 使用方式
1.html引入
```
axios、vue、element-ui相关依赖也要引入
[avue-cdn:](https://gitee.com/smallweigit/avue/raw/master/lib/avue.js)
<script src="https://cdn.bootcss.com/axios/0.18.0/axios.js"></script>
<script type="text/javascript" src="https://cdn.bootcss.com/vue/2.5.17-beta.0/vue.js"></script>
<script type="text/javascript" src="https://cdn.bootcss.com/element-ui/2.3.4/index.js"></script>
<script type="text/javascript" src="https://gitee.com/smallweigit/avue/raw/master/lib/avue.js"></script>
```
2.npm引入
```
npm install avue-cli

import Avue form 'avue-cli/packages/index.js';

```

#### 简介
`avue` 是一个类似easyui那种去写vue的方案支持SSR(服务端渲染)和SPA(单例页面),全部基于json可配置化去开发界面，节约开发成本和提高开发效率，基本构成由 [Vue.js](https://github.com/vuejs/vue) 和 [element](https://github.com/ElemeFE/element)。它使用了最新的前端技术栈，权限验证，第三方网站嵌套等功能，很多功能还在开发，敬请期待<br /><br />
`1.vuex本地持久化存储,封装h5的sessionStorage和localStorage`<br /><br />
`2.加入了本地离线的包引入方法去引入vue，vue-router等第三方包` [详细介绍](https://my.oschina.net/sunshineS/blog/1583563)<br /><br />
`3.支持SSR服务端渲染(express)`[vue-server-renderer](https://ssr.vuejs.org/zh/)<br /><br />
`4.支持阿里巴巴图标库在线调用，自动同步图标` [阿里巴巴图标库](http://www.iconfont.cn/)<br /><br />
`5.支持iframe嵌套第三方网站`[详细介绍](https://my.oschina.net/sunshineS/blog/1615716)<br /><br />
`6.支持js动态可配CRUD和FORM,节约大量开发成本，配置字典接口自动匹配字典`<br /><br />
`7.支持多种登录方式,本地验证码校验和服务端验证码校验`<br /><br />
`8.全局错误日志记录`<br /><br />
`9.scss模块化开发`<br /><br />
`10.crud组件快速生成器`<br /><br />
`11.增加系统管理模板(用户管理,角色管理,菜单管理——基于本框架的crud组件自动生成)`<br /><br />
`12.打包后docker一键部署脚本基于nginx镜像（具体的可以修改./src/docker/Dockerfile）`<br /><br />
`13.支持路由改变单例页面title`<br /><br />
`14.crud组件快速生成器`<br /><br />


#### 页面展示
**登录**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-login.png">
</p>

**权限测试页面**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-roles-test.png">
</p>

**数据展示**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-datashow.png">
</p>

**错误页面**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-errpage.png">
</p>

**错误日志记录**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-errlog.png">
</p>

**CRUD**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-crud.png">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-grade.png">
</p>

**FORM**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-form.png">
</p>

**用户管理**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-user.png">
</p>

**角色管理**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-role.png">
</p>

**菜单设置**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-menu.png">
</p>

**阿里巴巴图标库(在线调用)**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-ali.png">
</p>

**登录页面SSR渲染**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/login-ssr.png">
</p>

**主页**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-index.png">
</p>

**第三方网站**
<p align="center">
  <img width="900" src="https://gitee.com/smallweigit/avue/raw/master/static/cdn/images/avue-iframe.png">
</p>

#### 功能结构
```
- 全局错误日志记录
- vuex持久化存储
- 主题色切换
- 锁屏
- SSR渲染页面
- 数据展示
- 登录/注销
 - 用户名登录
 - 验证码登录
 - 第三方登录(开发中)
- 权限验证
- 第三方网站嵌套
- CRUD(增删改查)
- FORM(动态生成)
- 阿里巴巴图标库(在线调用)
- 系统管理
 - 用户管理
 - 角色管理
 - 菜单管理
- 高级路由
 - 动态路由
 - 参数路由
- 更多功能开在开发
```

##### 按钮的显隐控制
返回的vuex对象中额permission数组包括按钮的权限 例如: ['sys_crud_add', 'sys_crud_export'], crud的增加按钮和导出按钮

##### 全局错误日志记录
放开./src/page/errlog/index.vue中的errorA的组件即可测试他是存储在本地，可以自己回掉方法上传服务器，调用CLEAR_ALL_ERR方法清空本地

##### vuex持久化存demo请看
详细demo请看./src/store/modules/tgs.js实例
```bash
...
state:{
  ...
  tag: getStore({ name: 'tag' }) || tagObj
},
...
 mutations: {
  ...
  setStore({ name: 'tagList', content: state.tagList, type: 'session' })
  ...
 }
```
##### 数据加密工具类——在./src/util/util.js中encryption
支持Base64和Aes加密
```bash
const data ={
  username:'admin',
  password:'123456'
}
const userInfo = encryption({
    data: data,//加密的数据
    key:'123',//aes加密时的类型，不是aes加密不用传
    type: 'Base64',//要加密的类型 Base64 || Aes
    param: ['useranme', 'password'] //要加密的字段
});
```
##### CRUD和FORM使用说明————根据配置json文件自动生成CRUD和FORM，并且配置字典接口，自动匹配字典
详细demo请看./src/page/table/index.vue和./src/page/form/index.vue实例实例
```bash

支持vue的solt卡槽分发添加dom内容<br />
<template slot-scope="scope" slot="username">
  <el-tag>{{scope.row.username}}</el-tag>
</template>
<template slot-scope="scope" slot="menu">
  <el-button icon="el-icon-check" size="small" @click="handleGrade(scope.row,scope.$index)">权限</el-button>
</template>
slot的名字为字段的名字，当为menu时为菜单添加dom内容<br />
js自动配置crud<br />
{
  border: true,//表格是否显示边框 default:false
  index: true,///表格是否显示序号 default:false
  selection: true,//表格是否显示可选select default:false
  menu:true,//表格是否显示操作栏, default:true
  menuWidth:250,//操作菜单的宽度
  editBtn:false,//是否显示编辑按钮 default:true
  delBtn:false,//是否显示删除按钮 default:true
  page:false,//是否显示分页 default:true
  height:'500'//表格的高度 default:auto
  dic:['GRADE','SEX'],//传入需要获取字典的变量，看vuex中的getDic方法
  column: [
    {
      label: "用户名",//表格的标题
      prop: "username",//表格的key
      width: "150",//表格的宽度
      fixed: true,//是否冻结列
      hide:true,//是否隐藏
      span:12,//表单格栅显示的列
      type:'select', //select | radio | checkbox | date defaulttext
      visdiplay:true,//表单不显示
      solt:true,//支持自定义dom default:false,
      overHidden: true,//超出省略号显示
      dicData: 'GRADE', //传入需要引用的字典
      ],//type的数据字典,当type为：select | radio | checkbox 加载
      dataDetail: row => {
        return row.username+user.name;//是否对列表数据处理
      },
      rules: [{ required: true, message: "请输入用户名", trigger: "blur" }] //表单校验规则
    }
}
```

#### 开发
```bash
# 克隆项目
git clone https://gitee.com/smallweigit/avue.git

# 进入项目
cd avue

# 安装依赖
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

#### 调试与发布
```bash
# 构建测试环境
npm run dev

# 构建生成环境
npm run build

# 构建SSR渲染页面
npm run start

```


#### 其它
```bash
# 代码检测
npm run lint

# 单元测试
npm run karma

# 构建SSR客户端代码
npm run build:client

# 构建SSR服务端端代码
npm run build:server
```



#### License
[MIT](https://gitee.com/smallweigit/avue/blob/master/LICENSE)

Copyright (c) 2017-present Smallwei QQ:1634566606
