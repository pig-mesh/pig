<template>
  <div class="pull-auto">
    <el-button type="primary" @click="handleAdd" size="small">新 增</el-button>
    <br /><br />
    <avue-crud ref="crud" :page="page" :data="tableData" :table-loading="tableLoading" :option="tableOption" @current-change="currentChange" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel">
      <template slot-scope="scope" slot="menu">
        <el-button type="primary" icon="el-icon-check" size="small" plain @click="handleEdit(scope.row,scope.index)">编 辑</el-button>
        <el-button type="danger" icon="el-icon-delete" size="small" plain @click="handleDel(scope.row,scope.index)">删 除</el-button>
      </template>
    </avue-crud>
  </div>
</template>

<script>
import { getList, addObj, putObj, delObj } from "./api.js";
import tableOption from "./option.js";
export default {
  name: "{{name}}",
  data() {
    return {
      tableOption: tableOption,
      tableData: [],
      page: {
        total: 0, //总页数
        currentPage: 1, //当前页数
        pageSize: 20 //每页显示多少条
      },
      tableLoading: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleAdd: function() {
      this.$refs.crud.rowAdd();
    },
    handleEdit(row, index) {
      this.$refs.crud.rowEdit(row, index);
    },
    handleDel(row, index) {
      this.$refs.crud.rowDel(row, index);
    },
    /**
     * @title 首次加载获取列表
     * @param val 当前第几页
     *
     **/
    getList() {
      this.tableLoading = true;
      getList({
        page: this.page.currentPage,
        limit: this.page.pageSize
      }).then(response => {
        this.tableData = response.data.records;
        this.page.total = response.data.total;
        this.tableLoading = false;
      });
    },
    /**
     * @title 页码回调
     * @param val 当前第几页
     *
     **/
    currentChange(val) {
      this.page.currentPage = val;
      this.getList();
    },
    /**
     * @title 数据删除回调
     * @param row 为当前的数据
     * @param index 为当前的数据第几行
     *
     **/
    rowDel: function(row, index) {
      var _this = this;
      this.$confirm("是否确认删除ID为" + row["{{id}}"], "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delObj(row["{{id}}"]);
        })
        .then(data => {
          _this.tableData.splice(index, 1);
          _this.$message({
            showClose: true,
            message: "删除成功",
            type: "success"
          });
        })
        .catch(function(err) {});
    },

    /**
     * @title 数据更新回调
     * @param row 为当前的数据
     * @param index 为当前的数据第几行
     * @param done 为表单关闭函数
     *
     **/
    rowUpdate: function(row, index, done) {
      putObj(row).then(data => {
        this.tableData.splice(index, 1, Object.assign({}, row));
        this.$message({
          showClose: true,
          message: "修改成功",
          type: "success"
        });
        done();
      });
    },
    /**
     * @title 数据添加回调
     * @param row 为当前的数据
     * @param done 为表单关闭函数
     *
     **/
    rowSave: function(row, done) {
      addObj(row).then(data => {
        this.tableData.push(Object.assign({}, row));
        this.$message({
          showClose: true,
          message: "添加成功",
          type: "success"
        });
        done();
      });
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

