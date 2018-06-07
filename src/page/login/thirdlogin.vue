<!--
  -    Copyright (c) 2018-2025, lengleng All rights reserved.
  -
  - Redistribution and use in source and binary forms, with or without
  - modification, are permitted provided that the following conditions are met:
  -
  - Redistributions of source code must retain the above copyright notice,
  - this list of conditions and the following disclaimer.
  - Redistributions in binary form must reproduce the above copyright
  - notice, this list of conditions and the following disclaimer in the
  - documentation and/or other materials provided with the distribution.
  - Neither the name of the pig4cloud.com developer nor the names of its
  - contributors may be used to endorse or promote products derived from
  - this software without specific prior written permission.
  - Author: lengleng (wangiegie@gmail.com)
  -->

<template>
   <div class="third-container">
     <img :src="qqCode" class="third-icon qq"  />
     <img :src="wechatCode" class="third-icon wechat" />
   </div>
</template>

<script>
import qqCode from "@/assets/img/code/qq-code.jpg";
import wechatCode from "@/assets/img/code/wechat-code.jpg";
export default {
  name: "thirdlogin",
  data() {
    return {
      qqCode: qqCode,
      wechatCode: wechatCode
    };
  },
  created() {},
  mounted() {},
  computed: {},
  props: [],
  methods: {}
};
</script>

<style lang="scss" scoped>
.third-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 250px;
  & > .third-icon {
    padding: 8px;
    width: 130px;
    height: 130px;
    line-height: 100%;
    background-color: #fff;
    border-style: solid;
    border-width: 2px;
    border-radius: 5px;
    text-align: center;
  }
  & > .third-icon.qq {
    border-color: #409eff;
  }
  & > .third-icon.wechat {
    border-color: #67c23a;
  }
}
</style>
