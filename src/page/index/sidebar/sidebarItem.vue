<template>
  <div class="menu-wrapper">
    <template v-for="(item,index) in menu">
      <el-menu-item v-if="item.children.length===0 " :index="filterPath(item.path,index)" @click="open(item)" :key="item.label">
        <i :class="item.icon"></i>
        <span slot="title">{{item.label}}</span>
      </el-menu-item>
      <el-submenu v-else :index="filterPath(item.name,index)" :key="item.name">
        <template slot="title">
          <i :class="item.icon"></i>
          <span slot="title" :class="{display:!show}">{{item.label}}</span>
        </template>
        <template v-for="(child,cindex) in item.children">
          <el-menu-item :index="filterPath(child.path,cindex)" @click="open(child)" v-if="child.children.length==0" :key="cindex">
            <i :class="child.icon"></i>
            <span slot="title">{{child.label}}</span>
          </el-menu-item>
          <sidebar-item v-else :menu="[child]" :show="show" :key="cindex"></sidebar-item>
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
    filterPath(path, index) {
      return path == null ? index + "" : path;
    },
    open(item) {
      this.$router.push({
        path: resolveUrlPath(item.path, item.name),
        query: item.query
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.display,
.display + .el-submenu__icon-arrow {
  display: none;
}
</style>

