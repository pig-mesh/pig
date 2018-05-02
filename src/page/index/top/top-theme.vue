<template>
  <el-color-picker size="small" class="theme-picker" popper-class="theme-picker-dropdown" v-model="themeVal"></el-color-picker>
</template>

<script>
import theme from "@/mixins/theme";

export default {
  name: "topTheme",
  mixins: [theme()],
  data() {
    return {
      chalk: ""
    };
  }
};
</script>

<style>
.theme-picker .el-color-picker__trigger {
  vertical-align: middle;
}

.theme-picker-dropdown .el-color-dropdown__link-btn {
  display: none;
}
</style>
