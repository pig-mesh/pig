<template>
	<div class="pull-chheight role-container">
        <el-card class="box-card">
            <p>当前用户的权限值是有权限时(admin) 时，才可以看到菜单有这个页面。</p>
            <el-switch
                v-model="roleSwitch"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="admin"
                active-text="有权限(admin)"
                inactive-value="user"
                inactive-text="无权限(user)" @change="handlechange">
            </el-switch>
        </el-card>
        <el-card class="box-card" style="margin-top:20px;">
            <p>当前用户的权限值是有权限时(admin) 时。才能看到全部按钮</p>
            <el-button v-if="permission.sys_role_btn1">默认按钮</el-button>
            <el-button type="primary" v-if="permission.sys_role_btn2">主要按钮</el-button>
            <el-button type="success" v-if="permission.sys_role_btn3">成功按钮</el-button>
            <el-button type="info" v-if="permission.sys_role_btn4">信息按钮</el-button>
            <el-button type="warning" v-if="permission.sys_role_btn5">警告按钮</el-button>
            <el-button type="danger" v-if="permission.sys_role_btn6">危险按钮</el-button>
    </el-card>
         
	</div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "role",
  data() {
    return {
      roleSwitch: ""
    };
  },
  created() {
    this.roleSwitch = this.roles[0];
  },
  computed: {
    ...mapGetters(["roles", "permission"])
  },
  methods: {
    handlechange(val) {
      this.$store.commit("SET_ROLES", [val]);
      if (val == "user") {
        this.$store.commit("SET_PERMISSION", [
          "sys_role_btn1",
          "sys_role_btn2"
        ]);
      } else if (val == "admin") {
        this.$store.dispatch("GetUserInfo");
      }

      this.$store.dispatch("GetMenu");
    }
  }
};
</script>

<style scoped="scoped" lang="scss">

</style>