<template>
  <div class="menu-wrapper">
    <template v-for="item in menu">
      <el-menu-item v-if="item.children.length===0 " :index="item.component" @click="open(item)" :key="item.label">
        <i :class="item.icon"></i>
        <span slot="title">{{item.label}}</span>
      </el-menu-item>
      <el-submenu v-else :index="item.label" :key="item.name">
        <template slot="title">
          <i :class="item.icon"></i>
          <span slot="title" :class="{display:!show}">{{item.label}}</span>
        </template>
        <template v-for="child in item.children">
          <el-menu-item v-if="child.children.length==0" :index="child.component" @click="open(child)">
            <i :class="child.icon"></i>
            <span slot="title">{{child.label}}</span>
          </el-menu-item>
          <sidebar-item v-else :menu="[child]" :show="show"></sidebar-item>
        </template>
      </el-submenu>
    </template>
  </div>
</template>
<script>
import { resolveUrlPath } from "@/util/util";
export default {
  name: "SidebarItem",
  data() {
    return {};
  },
  props: {
    menu: {
      type: Array
    },
    show: {
      type: Boolean
    }
  },
  created() {},
  mounted() {},
  methods: {
    open(item) {
      this.$router.push({ path: resolveUrlPath(item.href) });
      this.$store.commit("ADD_TAG", {
        label: item.label,
        value: item.href
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.display {
  display: none;
}
</style>

