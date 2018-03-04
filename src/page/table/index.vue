<template>
 <div class="table-container pull-chheight">
  <div class="table-header">
      <el-button type="primary" @click="handleAdd" size="small" v-if="permission.sys_crud_btn_add">新 增</el-button>
      <el-button type="success" @click="handleExport" size="small"  v-if="permission.sys_crud_btn_export">导出excel</el-button>
      <el-button @click="toggleSelection([tableData[1]])" size="small">切换第二选中状态</el-button>
      <el-button @click="toggleSelection()" size="small">取消选择</el-button>
    </div>
  <Crud 
  :tableOption="tableOption"
  :tableData="tableData"
  :tableLoading="tableLoading"
  :before-open="boxhandleOpen"
  :before-close="boxhandleClose" 
  :page="page"
  ref="crud" 
  width="290" 
  @handleSave="handleSave"
  @handleUpdate="handleUpdate"
  @handleDel="handleDel"
  @handleCurrentChange="handleCurrentChange"
  @handleSelectionChange="handleSelectionChange"
  :menu="true">
    <template slot-scope="scope">
        <el-button icon="el-icon-check" size="small" @click="handleGrade(scope.row,scope.$index)">权限</el-button>
    </template>
  </Crud>
  <el-button @click.native="formate" style="margin: 8px 0">格式化</el-button>
    <el-input
    type="textarea"
    :autosize="{ minRows: 2, maxRows: 15}"
    placeholder="请输入内容"
    v-model="formJson">
  </el-input>
  <el-dialog
  title="菜单"
  :visible.sync="grade.box"
  width="40%">
<el-tree
  :data="menuAll"
  :default-checked-keys="grade.check"
  :default-expanded-keys="grade.check"
  show-checkbox
  node-key="id">
</el-tree>
  <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="handleGradeUpdate">更新</el-button>
  </span>
</el-dialog>

 </div>
</template>

<script>
import { mapGetters } from "vuex";
import Crud from "@/components/crud/";
import tableOption from "@/const/tableOption";
export default {
  name: "table",
  data() {
    return {
      tableOption: tableOption, //表格设置属性
      tableData: [], //表格的数据
      tablePage: 1,
      tableLoading: false,
      tabelObj: {},
      formJson: "",
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
    this.formJson = JSON.stringify(tableOption, null, 2);
    this.handleList();
  },
  watch: {},
  mounted() {},
  computed: {
    ...mapGetters(["permission", "menuAll"])
  },
  props: [],
  methods: {
    formate() {
      let p = new Promise((resolve, reject) => {
        resolve(JSON.parse(this.formJson));
      });
      p
        .then(data => {
          this.tableOption = data;
          this.formJson = JSON.stringify(data, null, 2);
          this.$message({
            message: "数据加载成功",
            type: "success"
          });
        })
        .catch(err => {
          this.$message({
            center: true,
            dangerouslyUseHTMLString: true,
            message: `JSON格式错误<br \>\n${err}`,
            type: "error"
          });
        });
    },
    /**
     * @title 权限更新
     *
     **/
    handleGradeUpdate() {
      this.tabelObj.check = [].concat(this.grade.check);
      this.tabelObj = {};
      this.grade.check = [];
      this.grade.box = false;
    },
    /**
     * @title 权限选择
     *
     **/
    handleGradeCheckChange(data, checked, indeterminate) {
      if (checked) {
        this.grade.check.push(data.id);
      } else {
        this.grade.check.splice(this.grade.check.indexOf(data.id), 1);
      }
    },
    /**
     * @title 打开权限
     */
    handleGrade(row, index) {
      this.$store.dispatch("GetMenuAll").then(data => {
        this.grade.box = true;
        this.tabelObj = row;
        this.grade.check = this.tabelObj.check;
      });
    },
    /**
     * @title 导出excel
     *
     **/
    handleExport() {
      import("@/vendor/Export2Excel").then(excel => {
        const tHeader = ["username", "name"];
        const filterVal = ["username", "name"];
        const list = this.tableData;
        const data = this.formatJson(filterVal, list);
        excel.export_json_to_excel(tHeader, data, this.filename);
      });
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v =>
        filterVal.map(j => {
          if (j === "timestamp") {
            return parseTime(v[j]);
          } else {
            return v[j];
          }
        })
      );
    },
    /**
     * @title 页面改变值
     *
     **/
    handleCurrentChange(val) {
      this.tablePage = val;
      this.handleList();
    },
    /**
     * @title 打开新增窗口
     * @detail 调用crud的handleadd方法即可
     *
     **/
    handleAdd() {
      this.$refs.crud.handleAdd();
    },
    /**
     * @title 选中第几行
     * @param row 选中那几行数据
     * @detail 调用crud的toggleSelection方法即可
     *
     **/
    toggleSelection(row) {
      this.$refs.crud.toggleSelection(row);
    },
    /**
     * @title 获取数据
     * @detail 赋值为tableData表格即可
     *
     **/
    handleList() {
      this.tableLoading = true;
      this.$store
        .dispatch("GetTableData", { page: `${this.tablePage}` })
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
     * @title 当前选中的数据
     * @param val 选中的值
     *
     **/
    handleSelectionChange(val) {
      this.$message({
        showClose: true,
        message: JSON.stringify(val),
        type: "success"
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
    },
    /**
     * @title 表单关闭前处理
     * @param done
     *
     **/
    boxhandleClose(done) {
      this.$message({
        showClose: true,
        message: "表单关闭前处理事件",
        type: "success"
      });
      done();
    },
    boxhandleOpen(show) {
      this.$message({
        showClose: true,
        message: "表单打开前处理事件",
        type: "success"
      });
      show();
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
