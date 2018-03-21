<template>
  <div class="pull-auto">
    <el-row :span="24">
      <template v-for="item,index in data">
        <el-col :span="span">
          <div class="item">
            <img :src="item.src" class="item-img" />
            <div class="item-text" :style="{color:colorText,backgroundColor:bgText}">
              <h3>{{item.name}}</h3>
              <p>{{item.text}}</p>
            </div>
          </div>
        </el-col>
      </template>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "cardData",
  data() {
    return {
      span: this.option.span || 6,
      data: this.option.data || [],
      colorText: this.option.colorText || "#fff",
      bgText: this.option.bgText || "#2e323f",
      borderColor: this.option.borderColor || "#2e323f"
    };
  },
  props: {
    option: {
      type: Object,
      default: {}
    }
  },
  created() {},
  mounted() {},
  watch: {},
  computed: {},
  methods: {}
};
</script>

<style lang="scss" scoped>
$height: 340px;
.item {
  position: relative;
  margin: 0 auto;
  margin-bottom: 50px;
  width: 230px;
  height: $height;
  overflow: hidden;
  border-radius: 5px;
  border-color: #fff;
  border-width: 1px;
  border-style: solid;
  &:hover .item-text {
    top: 0;
  }
}
.item-img {
  width: 100%;
  background: red;
  border-radius: 5px;
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}
.item-text {
  position: absolute;
  top: 150px;
  padding: 20px 15px;
  width: 100%;
  height: $height;
  overflow: auto;
  box-sizing: border-box;
  border-radius: 5px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
  opacity: 0.9;
  transition: top 0.4s;
  & > p {
    font-size: 12px;
    line-height: 25px;
    text-indent: 2em;
  }
}
</style>
