<template>
  <div class="pull-height animated" :class="{'zoomOutUp': isLock}">
    <top></top>
    <div class="index">
      <sidebar class="left pull-chheight"></sidebar>
      <div class="right">
        <tags ref="nav" class="nav"></tags>
        <keep-alive>
            <router-view v-if="!$route.meta.keepAlive" class="main pull-chheight"></router-view>
        </keep-alive>
        <router-view v-if="$route.meta.keepAlive" class="main pull-chheight"></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import tags from "./tags";
import top from "./top/";
import sidebar from "./sidebar/";
export default {
  components: {
    top,
    tags,
    sidebar
  },
  name: "index",
  data() {
    return {};
  },
  created() {},
  mounted() {},
  computed: mapGetters(["isLock"]),
  props: [],
  methods: {}
};
</script>

<style lang="scss" scoped>
.index {
  display: flex;
  height: calc(100% - 60px);
  padding: 0 0 20px 0;
  position: relative;
  min-height: 100%;
  background: #fff;
  overflow: hidden;
  .left:not(.el-menu--collapse) {
    width: 200px;
    overflow-y: auto;
  }
  .right {
    padding-top: 42px;
    position: relative;
    flex: 1;
    box-sizing: border-box;
  }
  .main {
    overflow-y: scroll;
  }
  .nav {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 999;
  }
}
</style>