<template>
    <div class="errlog-container pull-chheight">
        <h5>
          <el-tag>tip:</el-tag>&nbsp;错误是存储在本地，可以自己回掉方法上传服务器，调用vuex中的CLEAR_ALL_ERR方法清空本地记录
        </h5>
        <h5 v-if="errLog.length==0">
          还没有错误日志了！！！
           你可以放开&nbsp;<el-tag type="danger">./src/page/errlog/index.vue中的errorA标签</el-tag>&nbsp;的组件即可测试<br />
        </h5>
        <!-- <errorA></errorA> -->
        <div v-for="(err,index) in errLog" :key="index" class="errlog-list" v-else>
            <p class="errlog-item"><el-tag type="info">地址</el-tag><span>{{err.url}}</span></p>
            <p  class="errlog-item"><el-tag type="danger">信息</el-tag><span>{{err.message}}</span></p>
            <div class="errlog-info">
                {{err.stack}}
            </div>
        </div>
    </div>
</template>

<script>
import { mapState, mapGetters } from "vuex";
import errorA from "./errorTestA";
export default {
  name: "errlog",
  data() {
    return {};
  },
  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["errLog"])
  },
  props: [],
  methods: {},
  components: { errorA }
};
</script>

<style lang="scss" scoped>
.errlog-container {
  padding: 15px;
}
.errlog-list {
  & > .errlog-item {
    height: 40px;
    line-height: 40px;
    & > span {
      font-size: 14px;
    }
    & > .el-tag {
      margin-right: 10px;
    }
  }
  & > .errlog-info {
    margin-top: 10px;
    margin-bottom: 20px;
    padding: 8px 10px;
    background-color: #fff;
    font-size: 12px;
    border-style: solid;
    border-width: 1px;
    border-radius: 5px;
    border-color: #ccc;
  }
}
</style>
