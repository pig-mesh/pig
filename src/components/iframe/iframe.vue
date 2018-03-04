<template>
	<iframe v-if="$route.query.src" :src='$route.query.src' class="iframe" ref="iframe"  v-loading.fullscreen.lock="fullscreenLoading"></iframe>  
    <iframe v-else :src="urlPath" class="iframe" ref="iframe"  v-loading.fullscreen.lock="fullscreenLoading"></iframe>  
</template>

<script>
import { mapState, mapGetters } from "vuex";
export default {
  name: "myiframe",
  data() {
    return {
      fullscreenLoading: false,
      urlPath: this.getUrlPath() //iframe src 路径
    };
  },
  created() {},
  mounted() {
    this.load();
    this.resize();
  },
  props: ["routerPath"],
  watch: {
    $route: function() {
      this.load();
    },
    routerPath: function(val) {
      // 监听routerPath变化，改变src路径
      this.urlPath = this.getUrlPath();
    }
  },
  components: {
    ...mapGetters(["tagList"]),
    tagListNum: function() {
      return this.tagList.length != 0;
    }
  },
  methods: {
    // 显示等待框
    show() {
      this.fullscreenLoading = true;
    },
    // 隐藏等待狂
    hide() {
      this.fullscreenLoading = false;
    },
    // 加载浏览器窗口变化自适应
    resize() {
      window.onresize = () => {
        this.iframeInit();
      };
    },
    // 加载组件
    load() {
      this.show();
      this.$route.query.src = this.$route.query.src
        ? this.$route.query.src.replace("$", "#")
        : "";
      //超时3s自动隐藏等待狂，加强用户体验
      let time = 3;
      const timeFunc = setInterval(() => {
        time--;
        if (time == 0) {
          this.hide();
          clearInterval(timeFunc);
        }
      }, 1000);
      this.iframeInit();
    },
    //iframe窗口初始化
    iframeInit() {
      const iframe = this.$refs.iframe;
      const clientHeight = document.documentElement.clientHeight - 120;
      iframe.style.height = `${clientHeight}px`;
      if (iframe.attachEvent) {
        iframe.attachEvent("onload", () => {
          this.hide();
        });
      } else {
        iframe.onload = () => {
          this.hide();
        };
      }
    },
    getUrlPath: function() {
      //获取 iframe src 路径
      let url = window.location.href;
      url = url.replace("/myiframe", "");
      return url;
    }
  }
};
</script>

<style lang="scss">
.iframe {
  width: 100%;
  height: 100%;
  border: 0;
  overflow: hidden;
  box-sizing: border-box;
}
</style>