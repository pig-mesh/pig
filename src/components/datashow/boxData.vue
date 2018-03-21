<template>
  <div class="pull-auto">
    <el-row :span="24">
      <template v-for="item in data">
        <el-col :span="span">
          <div class="item" :style="{background:item.color}">
            <div class="item-header">
              <p>{{item.title}}</p>
              <span>{{item.subtitle}}</span>
            </div>
            <div class="item-body">
              <h2>{{item.count}}</h2>
            </div>
            <div class="item-footer">
              <span>{{item.allcount}}</span>
              <p>{{item.text}}</p>
            </div>
            <p class="item-tip">{{item.key}}</p>
          </div>
        </el-col>
      </template>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "datashow",
  data() {
    return {
      span: this.option.span || 6,
      data: this.option.data || []
    };
  },
  props: {
    option: {
      type: Object,
      default: {}
    }
  }
};
</script>

<style lang="scss" scoped>
.item {
  position: relative;
  margin: 15px;
  padding: 12px;
  height: 144px;
  border-radius: 4px;
  box-sizing: border-box;
  overflow: hidden;
  color: #fff;
}
.item-header {
  position: relative;
  & > p {
    margin: 0px;
    font-size: 14px;
  }
  & > span {
    position: absolute;
    right: 0px;
    top: 0px;
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 12px;
    background: rgba(255, 255, 255, 0.3);
  }
}
.item-body {
  & > h2 {
    margin: 0;
    font-size: 32px;
    line-height: 60px;
  }
}
.item-footer {
  line-height: 16px;
  & > span {
    font-size: 10px;
  }
  & > p {
    margin: 0px;
    font-size: 12px;
  }
}
.item-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  width: 80px;
  height: 80px;
  bottom: 10px;
  right: 10px;
  border: 2px solid #fff;
  border-radius: 100%;
  font-size: 48px;
  transform: rotate(-40deg);
  opacity: 0.1;
}
</style>
