<template>
    <div class="error500">
        <div class="error500-body-con">
            <el-card class="box-card">
                <div class="error500-body-con-title">
                    5<span class="error500-0-span"><i class="icon-debug"></i></span><span class="error500-0-span"><i class="icon-debug"></i></span>
                </div>
                <p class="error500-body-con-message">Oops! the server is wrong</p>
                <div class="error500-btn-con">
                    <el-button @click="goHome" size="large" style="width: 200px;" type="text">返回首页</el-button>
                    <el-button @click="backPage" size="large" style="width: 200px;margin-left: 40px;" type="primary">返回上一页</el-button>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
export default {
  name: "Error500",
  methods: {
    backPage() {
      this.$router.go(-1);
    },
    goHome() {
      this.$router.push({
        name: "/"
      });
    }
  }
};
</script>
<style lang="scss" scoped>
@keyframes error500animation {
  0% {
    transform: rotateZ(0deg);
  }
  20% {
    transform: rotateZ(-10deg);
  }
  40% {
    transform: rotateZ(5deg);
  }
  60% {
    transform: rotateZ(-5deg);
  }
  80% {
    transform: rotateZ(10deg);
  }
  100% {
    transform: rotateZ(0deg);
  }
}
.error500 {
  &-body-con {
    width: 700px;
    height: 500px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    &-title {
      text-align: center;
      font-size: 240px;
      font-weight: 700;
      color: #2d8cf0;
      height: 260px;
      line-height: 260px;
      margin-top: 40px;
      .error500-0-span {
        display: inline-block;
        position: relative;
        width: 170px;
        height: 170px;
        border-radius: 50%;
        border: 20px solid #ed3f14;
        color: #ed3f14;
        margin-right: 10px;
        i {
          display: inline-block;
          font-size: 120px !important;
          position: absolute;
          bottom: -43px;
          left: 20px;
          transform-origin: center bottom;
          animation: error500animation 3s ease 0s infinite alternate;
        }
      }
    }
    &-message {
      display: block;
      text-align: center;
      font-size: 30px;
      font-weight: 500;
      letter-spacing: 4px;
      color: #dddde2;
    }
  }
  &-btn-con {
    text-align: center;
    padding: 20px 0;
    margin-bottom: 40px;
  }
}
</style>

