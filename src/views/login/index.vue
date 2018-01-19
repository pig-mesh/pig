<template>
  <div class="login-container">
    <el-form autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left"
             label-width="0px"
             class="card-box login-form">
      <h3 class="title">系统登录</h3>
      <el-tabs v-model="activeName">
        <el-tab-pane label="账号登录" name="first">
          <el-form-item prop="username">
            <span class="svg-container svg-container_login">
              <svg-icon icon-class="user"/>
            </span>
            <el-input name="username" type="text" v-model="loginForm.username" autoComplete="on" placeholder="用户名"/>
          </el-form-item>
          <el-form-item prop="password">
            <span class="svg-container">
              <svg-icon icon-class="password"></svg-icon>
            </span>
              <el-input name="password" :type="pwdType" @keyup.enter.native="handleLogin" v-model="loginForm.password"
                        autoComplete="on"
                      placeholder="密码"></el-input>
            <span class="show-pwd" @click="showPwd"><svg-icon icon-class="eye"/></span>
          </el-form-item>

          <input name="randomStr" type="hidden" v-model="loginForm.randomStr"/>
          <el-form-item>
            <el-col :span="2">
          <span class="svg-container">
            <svg-icon icon-class="code"/>
          </span>
            </el-col>
            <el-col :span="11">
              <el-input name="code" type="text" v-model="loginForm.code" autoComplete="on" placeholder="验证码"/>
            </el-col>
            <el-col :span="10" align="right">
              <img :src="src" style="padding-bottom: 1px" @click="refreshCode"/>
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleLogin">
              登陆
            </el-button>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="短信登录" name="second">
          <el-form-item prop="mobile">
            <span class="svg-container svg-container_login">
              <svg-icon icon-class="mobile"/>
            </span>
            <el-input name="mobile" type="text" v-model="loginForm.mobile" autoComplete="on" placeholder="手机号"/>
          </el-form-item>
          <el-form-item>
            <el-col :span="2">
              <span class="svg-container">
                <svg-icon icon-class="code"/>
              </span>
            </el-col>
            <el-col :span="11">
              <el-input name="smsCode" type="text" v-model="loginForm.smsCode" autoComplete="on" placeholder="验证码"/>
            </el-col>
            <el-col :span="10" align="right">
              <a @click="getMobileCode">{{text}}</a>
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleMobileLogin">
              登陆
            </el-button>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="社交登录" name="three">
            <a href="http://dsb.frps.shop/social/qq" style="color: white;">QQ登录</a>
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </div>
</template>

<script>
  // import { isvalidUsername } from '@/utils/validate'
  import request from '@/utils/request'

  export default {
    name: 'login',
    data() {
      const validatePass = (rule, value, callback) => {
        if (!value || value.length < 6) {
          callback(new Error('密码不能小于6位'))
        } else {
          callback()
        }
      }
      return {
        time: 60,
        timeFlag: false,
        activeName: 'first',
        src: '',
        loginForm: {
          username: null,
          password: null,
          code: '',
          randomStr: Math.ceil(Math.random() * 100000) + '_' + Date.now(),
          mobile: null,
          smsCode: ''
        },
        loginRules: {
          username: [{required: true, trigger: 'blur'}],
          password: [{required: true, trigger: 'blur', validator: validatePass}],
          code: [{required: true, trigger: 'blur'}],
        },
        loading: false,
        pwdType: 'password'
      }
    },
    methods: {
      refreshCode: function () {
        this.loginForm.randomStr = Math.ceil(Math.random() * 100000) + Date.now()
        this.src = '/admin/code/' + this.loginForm.randomStr
      },
      showPwd() {
        if (this.pwdType === 'password') {
          this.pwdType = ''
        } else {
          this.pwdType = 'password'
        }
      },
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true
            this.$store.dispatch('Login', this.loginForm).then(() => {
              this.loading = false
              this.$router.push({path: '/'})
            }).catch(() => {
              this.loading = false
              this.refreshCode()
            })
          } else {
            console.log('想搞事情？？')
            return false
          }
        })
      },
      handleMobileLogin() {
        this.loading = true
        if (!this.loginForm.smsCode || this.loginForm.smsCode.length !== 4) {
          this.$message.error('验证码不合法')
        }
        this.$store.dispatch('MobileLogin', this.loginForm).then(() => {
          this.loading = false
          this.$router.push({path: '/'})
        }).catch(() => {
          this.loading = false
        })
      },
      getMobileCode: function () {
        if (!this.loginForm.mobile) {
          this.$message.error('请输入手机号码')
        } else if (!(/^1[34578]\d{9}$/.test(this.loginForm.mobile))) {
          this.$message.error('手机号格式不正确')
        } else {
          request({
            url: '/admin/smsCode/' + this.loginForm.mobile,
            method: 'get'
          }).then(response => {
            if (response.data) {
              this.timer()
              this.$message.success('验证码发送成功')
            } else {
              this.$message.error('验证码发送失败')
            }
          })
        }
      },
      timer: function () {
        if (this.time > 0) {
          this.timeFlag = true
          this.time--
          setTimeout(this.timer, 1000)
        } else {
          this.timeFlag = false
        }
      }
    },
    computed: {
      text: function () {
        if (this.timeFlag === false) {
          return '获取验证码'
        } else {
          return this.time > 0 ? this.time + 's' : '重新获取'
        }
      }
    },
    created() {
      this.src = '/admin/code/' + this.loginForm.randomStr
      var params = this.$route.query
      var access_token = params.access_token
      var refresh_token = params.refresh_token
      console.log(access_token)
      console.log(refresh_token)
      if (access_token !== undefined && refresh_token !== undefined) {
        console.log('执行到1')
        this.$store.dispatch('SocialLogin', params).then(() => {
          console.log('执行到2')
          this.loading = false
          this.$router.push({path: '/'})
        }).catch(() => {
          this.loading = false
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "src/styles/mixin.scss";

  $bg: #2d3a4b;
  $dark_gray: #889aa4;
  $light_gray: #eee;

  .login-container {
    @include relative;
    height: 100vh;
    .el-tabs__item {
      color: #fff;
    }
    .el-tabs__item.is-active {
      color: #409EFF;
    }
    background-color: $bg;
    input:-webkit-autofill {
      -webkit-box-shadow: 0 0 0px 1000px #293444 inset !important;
      -webkit-text-fill-color: #fff !important;
    }
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
    }
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;
    }
    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;
    }
    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
      &_login {
        font-size: 20px;
      }
    }
    .title {
      font-size: 26px;
      font-weight: 400;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
    .login-form {
      position: absolute;
      left: 0;
      right: 0;
      width: 400px;
      padding: 35px 35px 15px 35px;
      margin: 120px auto;
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
    }
    .thirdparty-button {
      position: absolute;
      right: 35px;
      bottom: 28px;
    }
  }
</style>
