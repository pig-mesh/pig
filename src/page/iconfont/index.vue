<template>
<div class="iconfon-container pull-height">
    <h3 style="color:red;">修改./src/config/env.js中的iconfontVersion地址即可实时调用阿里云在线图标库</h3>
    <ul class="icon-list">
        <li><span><i class="icon-daohanglanmoshi02"></i><span class="icon-name">icon-daohanglanmoshi02</span></span></li>
        <li><span><i class="icon-changyonglogo27"></i><span class="icon-name">icon-changyonglogo27</span></span></li>
        <li><span><i class="icon-biaoge"></i><span class="icon-name">icon-biaoge</span></span></li>
        <li><span><i class="icon-baidu1"></i><span class="icon-name">icon-baidu1</span></span></li>
        <li><span><i class="icon-tubiao"></i><span class="icon-name">icon-tubiao</span></span></li>
        <li><span><i class="icon-souhu"></i><span class="icon-name">icon-souhu</span></span></li>
        <li><span><i class="icon-msnui-360"></i><span class="icon-name">icon-msnui-360</span></span></li>
        <li><span><i class="icon-iframe"></i><span class="icon-name">icon-iframe</span></span></li>
        <li><span><i class="icon-huanyingye"></i><span class="icon-name">icon-huanyingye</span></span></li>
        <li><span><i class="icon-weixin"></i><span class="icon-name">icon-weixin</span></span></li>
        <li><span><i class="icon-qq"></i><span class="icon-name">icon-qq</span></span></li>
        <li><span><i class="icon-tuichu"></i><span class="icon-name">icon-tuichu</span></span></li>
        <li><span><i class="icon-jiaoseguanli"></i><span class="icon-name">icon-jiaoseguanli</span></span></li>
        <li><span><i class="icon-yonghuguanli"></i><span class="icon-name">icon-yonghuguanli</span></span></li>
        <li><span><i class="icon-caidanguanli"></i><span class="icon-name">icon-caidanguanli</span></span></li>
    </ul>
    <img class="iconfon-explace" src="http://oetrwxnhv.bkt.clouddn.com/avue-iconfont.png" alt="">
</div>
</template>

<script>
export default {
  name: "iconfont",
  data() {
    return {};
  },
  created() {},
  mounted() {},
  props: [],
  methods: {},
  components: {}
};
</script>

<style lang="scss" scoped>
.iconfon-container {
  padding: 15px;
}
.iconfon-container > ul.icon-list {
  margin: 15px auto;
  overflow: hidden;
  list-style: none;
  padding: 0;
  border: 1px solid #eaeefb;
  border-radius: 4px;
}

.iconfon-container .content > ul:not(.timeline) {
  margin: 10px 0;
  padding: 0 0 0 20px;
  font-size: 14px;
  color: #5e6d82;
  line-height: 2em;
}
.icon-list li {
  float: left;
  width: 16.66%;
  text-align: center;
  height: 120px;
  line-height: 120px;
  color: #666;
  font-size: 13px;
  transition: color 0.15s linear;
  border-right: 1px solid #eee;
  border-bottom: 1px solid #eee;
  margin-right: -1px;
  margin-bottom: -1px;
}
.icon-list li span {
  display: inline-block;
  line-height: normal;
  vertical-align: middle;
  font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB,
    Microsoft YaHei, SimSun, sans-serif;
  color: #99a9bf;
}
.icon-list li i {
  display: block;
  font-size: 36px !important;
  margin-bottom: 15px;
  color: #606266;
}
.icon-list li .icon-name {
  display: inline-block;
  padding: 0 12px;
  font-size: 12px !important;
  height: 1em;
  color: #606266;
}
.iconfon-explace {
  width: 100%;
}
</style>
