<template>
  <el-menu unique-opened :default-active="nowTagValue" class="el-menu-vertical-demo" background-color="#495060" text-color="#c9cbd0" active-text-color="#fff" :collapse="isCollapse">
    <sidebar-item :menu="menu" :show="!isCollapse"></sidebar-item>
  </el-menu>
</template>

<script>
import MENU from "@/mock/menu";
import { mapGetters } from "vuex";
import { setUrlPath } from "@/util/util";
import SidebarItem from "./sidebarItem";

export default {
  data() {
    return {};
  },
  created() {
    this.$store.dispatch("GetMenu").then(data => {});
  },
  computed: {
    ...mapGetters(["menu", "tag", "isCollapse"]),
    nowTagValue: function() {
      return setUrlPath(this.$route);
    }
  },
  mounted() {},
  methods: {},
  components: { SidebarItem }
};
</script>
<style lang="scss" scoped>

</style>

