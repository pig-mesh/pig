<template>
 <div class="table-container pull-height">
    <div class="table-header">
      <el-button type="primary" @click="handleAdd" size="small" v-if="permission.sys_crud_btn_add">新 增</el-button>
    </div>
  <Crud 
  :tableOption="tableOption"
  :tableData="tableData"
  :tableLoading="tableLoading"
  :page="page"
  ref="crud" 
  width="290" 
  @handleSave="handleSave"
  @handleUpdate="handleUpdate"
  @handleDel="handleDel"
  menu>

  </Crud>

 </div>
</template>

<script>
import { mapGetters } from "vuex";
import Crud from "@/components/crud/";
import { userOption } from "@/const/adminTabelOption.js";
export default {
  name: "user",
  data() {
    return {
      tableOption: {}, //表格设置属性
      tableData: [], //表格的数据
      tablePage: 1,
      tableLoading: false,
      tabelObj: {},
      page: {
        total: 0, //总页数
        currentPage: 1, //当前页数
        pageSize: 10 //每页显示多少条
      },
      grade: {
        box: false,
        check: []
      }
    };
  },
  created() {
    //初始化数据格式
    this.tableOption = userOption;
    this.handleList();
  },
  watch: {},
  mounted() {},
  computed: {
    ...mapGetters(["permission", "menuAll"])
  },
  props: [],
  methods: {
    /**
     * @title 打开新增窗口
     * @detail 调用crud的handleadd方法即可
     *
     **/
    handleAdd() {
      this.$refs.crud.handleAdd();
    },
    /**
     * @title 获取数据
     * @detail 赋值为tableData表格即可
     *
     **/
    handleList() {
      this.tableLoading = true;
      this.$store
        .dispatch("GetUserData", { page: `${this.tablePage}` })
        .then(data => {
          setTimeout(() => {
            this.tableData = data.tableData;
            this.page = {
              total: data.total,
              pageSize: data.pageSize
            };
            this.tableLoading = false;
          }, 1000);
        });
    },
    /**
     * @title 数据添加
     * @param row 为当前的数据
     * @param done 为表单关闭函数
     *
     **/
    handleSave(row, done) {
      this.tableData.push(row);
      this.$message({
        showClose: true,
        message: "添加成功",
        type: "success"
      });
      done();
    },
    /**
     * @title 数据删除
     * @param row 为当前的数据
     * @param index 为当前更新数据的行数
     *
     **/
    handleDel(row, index) {
      this.$confirm(`是否确认删除序号为${row.name}`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.tableData.splice(index, 1);
          this.$message({
            showClose: true,
            message: "删除成功",
            type: "success"
          });
        })
        .catch(err => {});
    },
    /**
     * @title 数据更新
     * @param row 为当前的数据
     * @param index 为当前更新数据的行数
     * @param done 为表单关闭函数
     *
     **/
    handleUpdate(row, index, done) {
      this.tableData.splice(index, 1, row);
      this.$message({
        showClose: true,
        message: "修改成功",
        type: "success"
      });
      done();
    }
  },
  components: {
    Crud
  }
};
</script>

<style lang="scss" scoped>
.table-container {
  padding: 8px 10px;
}
.table-header {
  margin-bottom: 10px;
  & > .el-button {
    padding: 12px 25px;
  }
}
</style>
