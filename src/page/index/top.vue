<template>
<div class="header">
		<div class="header-button is-left">
		  <h3 style="letter-spacing: 1px;">Pig 微服务快速开发框架</h3>
		</div>
		<h1 class="header-title"></h1>
		<div class="header-button is-right">
        <el-tooltip class="item" effect="dark" content="锁屏" placement="bottom">
           <span  class="header-item">
              <i class="icon-bofangqi-suoping" @click="handleLock"></i>
           </span>
        </el-tooltip>
         <el-tooltip class="item" effect="dark" :content="isFullScren?'退出全屏':'全屏'" placement="bottom">
           <span  class="header-item">
               <i :class="isFullScren?'icon-tuichuquanping':'icon-quanping'" @click="handleScreen"></i>
           </span>
         </el-tooltip>
         <el-dropdown>
          <span class="el-dropdown-link">
            {{userInfo.username}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="logout">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-tooltip class="item" effect="dark" content="用户头像" placement="bottom">
             <img class="header-userImg" :src="userInfo.avatar">
         </el-tooltip>
		</div>
	</div>
</template>

<script>
import { mapState, mapGetters } from "vuex";
import { fullscreenToggel } from "@/util/util";
export default {
  name: "top",
  data() {
    return {};
  },
  filters: {},
  created() {},
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    }),
    ...mapGetters(["isFullScren"])
  },
  methods: {
    handleLock() {
      this.$store.commit("SET_LOCK");
      setTimeout(() => {
        this.$router.push({ path: "/lock" });
      }, 100);
    },
    handleScreen() {
      this.$store.commit("SET_FULLSCREN");
      fullscreenToggel();
    },
    logout() {
      this.$confirm("是否退出系统, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$store.dispatch("LogOut").then(() => {
          this.$router.push({ path: "/login" });
        });
      });
    }
  }
};
</script>

<style lang="scss" scoped>

</style>

