<template>
  <span>
    <i class="icon-bofangqi-suoping" @click="handleLock"></i>
    <el-dialog title="设置锁屏密码" :visible.sync="box" width="30%">
      <el-form :model="form" ref="form" label-width="80px">
        <el-form-item label="锁屏密码" prop="passwd" :rules="[{ required: true, message: '锁屏密码不能为空'}]">
          <el-input v-model="form.passwd" placeholder="请输入锁屏密码"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSetLock">确 定</el-button>
      </span>
    </el-dialog>
  </span>
</template>

<script>
import { fullscreenToggel } from "@/util/util";
import { validatenull } from "@/util/validate";
import { mapGetters } from "vuex";
export default {
  name: "top-lock",
  data() {
    return {
      box: false,
      form: {
        passwd: ""
      }
    };
  },
  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["lockPasswd"])
  },
  props: [],
  methods: {
    handleSetLock() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$store.commit("SET_LOCK_PASSWD", this.form.passwd);
          this.handleLock();
        }
      });
    },
    handleLock() {
      if (validatenull(this.lockPasswd)) {
        this.box = true;
        return;
      }
      this.$store.commit("SET_LOCK");
      setTimeout(() => {
        this.$router.push({ path: "/lock" });
      }, 100);
    }
  },
  components: {}
};
</script>

<style lang="scss" scoped>

</style>
