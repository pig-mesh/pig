<template>
  <div class="pull-auto top-menu">
    <el-menu :default-active="activeIndex" mode="horizontal">
      <template v-for="(item,index) in items">
        <el-menu-item :index="item.parentId+''" @click.native="openMenu(item)" :key="index">{{item.label}}</el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script>
import { resolveUrlPath } from "@/util/util";
import { mapState, mapGetters } from "vuex";
export default {
  name: "top-menu",
  data() {
    return {
      activeIndex: "0",
      items: [
        {
          label: "首页",
          href: "/wel/index",
          parentId: 0
        },
        {
          label: "文档",
          href: "https://www.kancloud.cn/lengleng/pig-guide/550709",
          parentId: 1
        },
        {
          label: "crud实例",
          href: "/crud/index",
          parentId: 2
        }
      ]
    };
  },
  created() {},
  computed: {
    ...mapGetters(["tagCurrent", "menu"])
  },
  methods: {
    openMenu(item) {
      this.$store.dispatch("GetMenu", item.parentId).then(data => {
        let itemActive,
          childItemActive = 0;
        if (item.href) {
          itemActive = item;
        } else {
          if (this.menu[childItemActive].length == 0) {
            itemActive = this.menu[childItemActive];
          } else {
            itemActive = this.menu[childItemActive].children[childItemActive];
          }
        }
        this.$router.push({
          path: resolveUrlPath(itemActive.href, itemActive.label)
        });
      });
    }
  }
};
</script>

<style scoped="scoped" lang="scss">
.top-menu {
  margin-top: -4px;
  box-sizing: border-box;
}
</style>