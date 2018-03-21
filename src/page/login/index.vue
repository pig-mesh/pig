<template>
  <div class="login-container pull-height" @keyup.enter.native="handleLogin">
    <div class="login-info text-white animated fadeInLeft">
      <h2 class="login-info-title">Pig 微服务快速开发框架</h2>
      <ul class="login-info-list">
        <li class="login-info-item">
          <i class="el-icon-check"></i> 是一个基于Spring Cloud、oAuth2.0开发基于Vue前后分离的开发平台</li>
        <li class="login-info-item">
          <i class="el-icon-check"></i> 是一个基于vue+vuex+vue-router快速后台管理系统，采用token交互验证方式。</li>
        <li class="login-info-item">
          <i class="el-icon-check"></i> 最大程度上帮助企业节省时间成本和费用开支。 </li>
        <li class="login-info-item">
          <i class="el-icon-check"></i> QQ群：23754102 </li>
      </ul>
    </div>
    <div class="login-border pull-height">
      <div class="login-main animated fadeIn">
        <h2>欢迎使用</h2>
        <p>Pig 微服务快速开发框架</p>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="用户密码" name="user">
            <userLogin></userLogin>
          </el-tab-pane>
          <el-tab-pane label="短信验证码" name="code">
            <codeLogin></codeLogin>
          </el-tab-pane>
          <el-tab-pane label="第三方授权登录" name="third">
            <thirdLogin></thirdLogin>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

  </div>
</template>
<script>
import userLogin from "./userlogin";
import codeLogin from "./codelogin";
import thirdLogin from "./thirdlogin";
export default {
  name: "login",
  data() {
    return {
      activeName: "user"
    };
  },
  created() {},
  mounted() {},
  computed: {},
  props: [],
  methods: {
    handleClick() {}
  },
  components: {
    userLogin,
    codeLogin,
    thirdLogin
  }
};
</script>

<style lang="scss">
.login-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(0, 0, 0, 0.6);
  position: relative;
}
.login-container::before {
  z-index: -999;
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-image: url("../../assets/img/login.jpg");
  background-size: cover;
}
.login-info {
  padding-left: 60px;
}
.login-info-title {
  line-height: 90px;
}
.login-info-item {
  font-size: 14px;
}
.login-border {
  display: flex;
  align-items: center;
  background-color: #fff;
}
.login-main {
  padding: 140px 60px;
  width: 450px;
  border-radius: 3px;
  box-sizing: border-box;
  background-color: #fff;
  & > .el-tabs {
    margin-top: 30px;
  }
}
.login-main > h3 {
  margin-bottom: 20px;
}
.login-main > p {
  color: #76838f;
}
.login-submit {
  width: 100%;
}
.login-form {
  margin: 20px 0;
}
.login-code {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 0 0 0 10px;
}
.login-code-img {
  width: 100px;
  height: 43px;
  background-color: #eee;
  border: 1px solid #f0f0f0;
  color: #333;
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 2px;
  text-indent: 2px;
  text-align: center;
}
.login-code-icon {
}
</style>
