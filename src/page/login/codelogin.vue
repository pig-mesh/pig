<template>
  <el-form class="login-form" status-icon :rules="loginRules" ref="loginForm" :model="loginForm" label-width="0">
    <el-form-item prop="mobile">
      <el-input size="small" @keyup.enter.native="handleLogin" v-model="loginForm.mobile" auto-complete="off" placeholder="请输入手机号码">
        <i slot="prefix" class="icon-shouji"></i>
      </el-input>
    </el-form-item>
    <el-form-item prop="code">
      <el-input size="small" @keyup.enter.native="handleLogin" v-model="loginForm.code" auto-complete="off" placeholder="请输入验证码">
        <template slot="append">
          <span @click="handleSend" class="msg-text" :class="[{display:msgKey}]">{{msgText}}</span>
        </template>
        <i slot="prefix" class="icon-yanzhengma" style="margin-top:10px;"></i>
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="small" @click.native.prevent="handleLogin" class="login-submit">登录</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
const MSGINIT = "发送验证码",
  MSGERROR = "验证码发送失败",
  MSGSCUCCESS = "${time}秒后重发",
  MSGTIME = 60;
import { isvalidatemobile } from "@/util/validate";
import { mapGetters } from "vuex";
import request from "@/router/axios";
export default {
  name: "codelogin",
  data() {
    const validatePhone = (rule, value, callback) => {
      if (isvalidatemobile(value)[0]) {
        callback(new Error(isvalidatemobile(value)[1]));
      } else {
        callback();
      }
    };
    const validateCode = (rule, value, callback) => {
      if (value.length != 4) {
        callback(new Error("请输入4位数的验证码"));
      } else {
        callback();
      }
    };
    return {
      msgText: MSGINIT,
      msgTime: MSGTIME,
      msgKey: false,
      loginForm: {
        mobile: "",
        code: ""
      },
      loginRules: {
        mobile: [{ required: true, trigger: "blur", validator: validatePhone }],
        code: [{ required: true, trigger: "blur", validator: validateCode }]
      }
    };
  },
  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["tagWel"])
  },
  props: [],
  methods: {
    handleSend() {
      if (this.msgKey) return;
      if (!this.loginForm.mobile) {
        this.$message.error("请输入手机号码");
      } else if (!/^1[34578]\d{9}$/.test(this.loginForm.mobile)) {
        this.$message.error("手机号格式不正确");
      } else {
        request({
          url: "/admin/smsCode/" + this.loginForm.mobile,
          method: "get"
        }).then(response => {
          if (response.data.data) {
            this.timer();
            this.$message.success("验证码发送成功");
          } else {
            this.$message.error(response.data.msg);
          }
        });
      }
      this.msgText = MSGSCUCCESS.replace("${time}", this.msgTime);
      this.msgKey = true;
      const time = setInterval(() => {
        this.msgTime--;
        this.msgText = MSGSCUCCESS.replace("${time}", this.msgTime);
        if (this.msgTime == 0) {
          this.msgTime = MSGTIME;
          this.msgText = MSGINIT;
          this.msgKey = false;
          clearInterval(time);
        }
      }, 1000);
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.$store
            .dispatch("LoginByPhone", this.loginForm)
            .then(response => {
              this.$store.commit("ADD_TAG", this.tagWel);
              this.$router.push({ path: this.tagWel.value });
            });
        }
      });
    }
  }
};
</script>

<style>
.msg-text {
  display: block;
  width: 60px;
  font-size: 12px;
  text-align: center;
  cursor: pointer;
}
.msg-text.display {
  color: #ccc;
}
</style>
