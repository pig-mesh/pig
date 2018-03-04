<template>
    <div class="lock-container pull-height">
      <div class="lock-form animated bounceInDown">
        <div class="animated"  :class="{'shake':passwdError,'bounceOut':pass}">
          <h3 class="text-white">{{userInfo.username}}——<small>默认密码:avue</small></h3> 
           <el-input placeholder="请输入登录密码" type="password" class="input-with-select animated" v-model="passwd">
             <el-button slot="append" icon="icon-bofangqi-suoping" @click="handleLogin" ></el-button>
           </el-input>
        </div>
          
      </div>
    </div>
</template>
<script>
import { mapGetters, mapState } from "vuex";
export default {
  name: "lock",
  data() {
    return {
      passwd: "",
      passwdError: false,
      pass: false
    };
  },
  created() {},
  mounted() {},
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    }),
    ...mapGetters(["tag"])
  },
  props: [],
  methods: {
    handleLogin() {
      if (this.passwd != "avue") {
        this.passwd = "";
        this.$message({
          message: "解锁密码错误,默认为avue",
          type: "error"
        });
        this.passwdError = true;
        setTimeout(() => {
          this.passwdError = false;
        }, 1000);
        return;
      }
      this.pass = true;
      setTimeout(() => {
        this.$store.commit("CLEAR_LOCK");
        this.$router.push({ path: this.tag.value || "/" });
      }, 1000);
    }
  },
  components: {}
};
</script>

<style lang="scss">
.lock-container {
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.6);
  position: relative;
}
.lock-container::before {
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
.lock-form {
  width: 300px;
}
</style>