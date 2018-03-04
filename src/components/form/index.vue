<template>
 <div class="from-container pull-auto">
    <el-form  ref="form" :model="form" label-width="80px" :rules="formRules">
      <el-row :gutter="20" :span="24">
        <template v-for="(column,index) in formOption.column">
          <el-col :span="column.span||12">
            <el-form-item :label="column.label" :prop="column.prop" v-if="!column.visdiplay">
              <template v-if="column.type == 'select'">
                  <el-select v-model="form[column.prop]" :placeholder="'请选择'+column.label" :disabled="column.disabled">
                  <el-option
                    v-for="(item,index) in DIC[column.dicData]"
                    :key="index"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
              <template v-if="column.type == 'radio'">
                    <el-radio  v-for="(item,index) in DIC[column.dicData]" v-model="form[column.prop]" :disabled="column.disabled" :label="item.value" :key="index">{{item.label}}</el-radio>
              </template>
              <template v-if="column.type == 'date'">
                    <el-date-picker v-model="form[column.prop]" type="date" :placeholder="'请输入'+column.label" :disabled="column.disabled"> </el-date-picker>
              </template>
              <template v-if="column.type == 'checkbox'">
                <el-checkbox-group  v-model="form[column.prop]">
                    <el-checkbox  v-for="(item,index) in DIC[column.dicData]" :label="item.value" :key="index" :disabled="column.disabled">{{item.label}}</el-checkbox>
                </el-checkbox-group>
              </template>
              <template v-if="!column.type">
                  <el-input v-model="form[column.prop]" :placeholder="'请输入'+column.label" :disabled="column.disabled"></el-input>  
              </template>
            </el-form-item>
          </el-col>
         </template>
          <el-col :span="24">
          <el-form-item>
              <el-button type="primary" @click="handleSubmit">{{formSubmitText}}</el-button>
          </el-form-item>
          </el-col>
      </el-row>
      </el-form>
 </div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  name: "from",
  data() {
    return {
      form: {},
      formRules: {},
      DIC: {}
    };
  },
  created() {
    //规则初始化
    this.rulesInit();
    //初始化dic字典
    this.dicInit();
    //初始化form表单
    this.formInit();
  },
  watch: {
    formOption: function(n, o) {
      this.rulesInit();
    }
  },
  mounted() {},
  computed: {},
  props: {
    formOption: {
      type: Object,
      required: true,
      default: {}
    },
    formSubmitText: {
      type: String,
      default: "提交"
    }
  },
  methods: {
    ...mapActions(["GetDic"]),
    rulesInit() {
      this.formRules = {};
      this.formOption.column.forEach(ele => {
        if (ele.rules) this.formRules[ele.prop] = ele.rules;
      });
    },
    dicInit() {
      this.GetDic(this.formOption.dic).then(data => {
        this.DIC = data;
      });
    },
    formInit() {
      const list = this.formOption.column;
      let form = {};
      list.forEach(ele => {
        if (ele.type == "checkbox" || ele.type == "radio") {
          form[ele.prop] = [];
        } else {
          form[ele.prop] = "";
        }
      });
      this.form = Object.assign({}, form);
    },
    handleSubmit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$emit("handleSubmit", this.form);
        } else {
          this.$emit("handleSubmit");
        }
      });
    }
  },
  components: {}
};
</script>

<style lang="scss" scoped>
.from-container {
  padding: 8px 10px;
}
</style>
