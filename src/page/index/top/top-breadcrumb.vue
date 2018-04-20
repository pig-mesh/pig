<template>
    <el-breadcrumb separator="/" class="tags-breadcrumb-list">
        <template v-for="(item,index) in tagCurrent">
            <el-breadcrumb-item :to="{ path: item.value }">{{item.label}}</el-breadcrumb-item>
        </template>
    </el-breadcrumb>
</template>
<script>
import { mapGetters } from "vuex";
export default {
  name: "breadcrumb",
  data() {
    return {};
  },
  created() {},
  mounted() {},
  watch: {},
  computed: {
    ...mapGetters(["tagCurrent"])
  },
  methods: {}
};
</script>
<style lang="scss" scoped>

</style>


