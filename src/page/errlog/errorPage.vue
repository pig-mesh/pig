<template>
    <div class="errpage-container pull-chheight">
       <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>404错误页面</span>
        </div>
       <div class="error-box">
            <errorPage404 class="error-page"></errorPage404>
            <span class="error-text">/404当访问的页面不存在时会跳转到404页面，您可以在浏览器地址栏中修改url为一个不存在的路径，体验一下效果</span>
       </div>
        </el-card>
        <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>403错误页面</span>
        </div>
        <div class="error-box">
            <errorPage403  class="error-page"></errorPage403>
             <span class="error-text">/403在当前登录用户不具有执行当前操作的权限时跳转到该页面，您可以在ajax请求方法中判断返回的状态码为403时跳转到该页面</span>
        </div>
        </el-card>
        <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>500错误页面</span>
        </div>
        <div class="error-box">
            <errorPage500 class="error-page"></errorPage500>
             <span class="error-text">/500当请求之后出现服务端错误时跳转到该页面，您可以在ajax请求方法中判断返回的状态码为500时跳转到该页面</span>
        </div>
        </el-card>
    </div>
</template>

<script>
import errorPage404 from "@/components/errorPage/404.vue";
import errorPage403 from "@/components/errorPage/403.vue";
import errorPage500 from "@/components/errorPage/500.vue";
export default {
  name: "errPage",
  data() {
    return {};
  },
  created() {},
  mounted() {},
  computed: {},
  props: [],
  methods: {},
  components: { errorPage404, errorPage403, errorPage500 }
};
</script>

<style lang="scss" scoped>
.error-page {
  position: absolute;
  top: 90px;
  left: 150px;
  transform: scale(0.4);
}
.error-text {
  position: absolute;
  top: 20px;
  left: 340px;
}
.error-box {
  position: relative;
  height: 200px;
}
</style>
