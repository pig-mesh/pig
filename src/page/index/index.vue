<template>
	<div class="pull-height animated" :class="{'zoomOutUp': isLock}">
    <Top></Top>
		<div class="index">
      <Sidebar class="left pull-chheight"></Sidebar>
			<div class="right">
          <Tags ref="nav" class="nav"></Tags>
          <router-view class="main"></router-view>  
			</div>
		</div>
	</div>
</template>

<script>
import { mapGetters } from "vuex";
import Tags from "./tags";
import Top from "./top";
import Sidebar from "./sidebar/";
export default {
  name: "index",
  data() {
    return {};
  },
  created() {},
  mounted() {},
  computed: mapGetters(["isLock"]),
  props: [],
  methods: {},
  components: {
    Top,
    Tags,
    Sidebar
  }
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
  .left {
  }
  .right {
    padding-top: 102px;
    position: relative;
    flex: 1;
    overflow: auto;
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