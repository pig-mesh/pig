<template>
  <el-collapse v-model="activeName" accordion>
    <el-collapse-item title="HOME" name="1">
      <div>Pig 是基于spring cloud 的权限管理系统</div>
      <div>业务模块不涉及oauth2.0，认证鉴权全部在网关模块，力求代码简洁</div>
      <div>业务模块可以直接根据网关转发过来的请求解析请求头中jwt信息，进行当前用户身份确认</div>
      <div>wiki:<a href="https://gitee.com/log4j/pig/wikis/Home" target="_blank">https://gitee.com/log4j/pig/wikis/Home</a></div>
    </el-collapse-item>
    <el-collapse-item title="开发环境" name="2">
      <div>wiki:<a href="https://gitee.com/log4j/pig/wikis/开发环境" target="_blank">https://gitee.com/log4j/pig/wikis/开发环境</a></div>
    </el-collapse-item>
    <el-collapse-item title="配置修改" name="3">
      <div>wiki:<a href="https://gitee.com/log4j/pig/wikis/配置修改" target="_blank">https://gitee.com/log4j/pig/wikis/配置修改</a></div>
    </el-collapse-item>
    <el-collapse-item title="启动体验" name="4">
      <div>wiki:<a href="https://gitee.com/log4j/pig/wikis/启动体验" target="_blank">https://gitee.com/log4j/pig/wikis/启动体验</a></div>
    </el-collapse-item>
    <el-collapse-item title="接口调用使用" name="5">
      <div>wiki:<a href="https://gitee.com/log4j/pig/wikis/接口调用使用" target="_blank">https://gitee.com/log4j/pig/wikis/接口调用使用</a></div>
    </el-collapse-item>
    <el-collapse-item title="页面访问使用" name="6">
      <div>wiki:<a href="https://gitee.com/log4j/pig/wikis/页面访问使用" target="_blank">https://gitee.com/log4j/pig/wikis/页面访问使用</a></div>
      <dict type="lengleng"></dict>
    </el-collapse-item>

  </el-collapse>
</template>


<script>
  import Dict from '@/components/Dict'
  export default {
    components: {
      Dict
    },
    data() {
      return {
        activeName: '1'
      }
    }
  }
</script>
