<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-button v-if="sys_log_add" class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加
      </el-button>
    </div>
    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit
              highlight-current-row style="width: 100%">
      <el-table-column align="center" label="编号">
        <template scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="日志类型">
        <template scope="scope">
          <span>{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="日志标题">
        <template scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建者">
        <template scope="scope">
          <span>{{ scope.row.createBy }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间">
        <template scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新时间">
        <template scope="scope">
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作IP地址">
        <template scope="scope">
          <span>{{ scope.row.remoteAddr }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户代理">
        <template scope="scope">
          <span>{{ scope.row.userAgent }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="请求URI">
        <template scope="scope">
          <span>{{ scope.row.requestUri }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作方式">
        <template scope="scope">
          <span>{{ scope.row.method }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作提交的数据">
        <template scope="scope">
          <span>{{ scope.row.params }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="执行时间">
        <template scope="scope">
          <span>{{ scope.row.time }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template scope="scope">
          <el-button v-if="sys_log_upd" size="small" type="success"
                     @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button v-if="sys_log_del" size="mini" type="danger"
                     @click="handleDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page.sync="listQuery.page"
                     :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit"
                     layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="编号" prop="username">
          <el-input v-model="form.id" placeholder="编号"></el-input>
        </el-form-item>
        <el-form-item label="日志类型" prop="username">
          <el-input v-model="form.type" placeholder="日志类型"></el-input>
        </el-form-item>
        <el-form-item label="日志标题" prop="username">
          <el-input v-model="form.title" placeholder="日志标题"></el-input>
        </el-form-item>
        <el-form-item label="创建者" prop="username">
          <el-input v-model="form.createBy" placeholder="创建者"></el-input>
        </el-form-item>
        <el-form-item label="创建时间" prop="username">
          <el-input v-model="form.createTime" placeholder="创建时间"></el-input>
        </el-form-item>
        <el-form-item label="更新时间" prop="username">
          <el-input v-model="form.updateTime" placeholder="更新时间"></el-input>
        </el-form-item>
        <el-form-item label="操作IP地址" prop="username">
          <el-input v-model="form.remoteAddr" placeholder="操作IP地址"></el-input>
        </el-form-item>
        <el-form-item label="用户代理" prop="username">
          <el-input v-model="form.userAgent" placeholder="用户代理"></el-input>
        </el-form-item>
        <el-form-item label="请求URI" prop="username">
          <el-input v-model="form.requestUri" placeholder="请求URI"></el-input>
        </el-form-item>
        <el-form-item label="操作方式" prop="username">
          <el-input v-model="form.method" placeholder="操作方式"></el-input>
        </el-form-item>
        <el-form-item label="操作提交的数据" prop="username">
          <el-input v-model="form.params" placeholder="操作提交的数据"></el-input>
        </el-form-item>
        <el-form-item label="执行时间" prop="username">
          <el-input v-model="form.time" placeholder="执行时间"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { fetchList, addObj, putObj, delObj } from '@/api/log'
  import waves from '@/directive/waves/index.js' // 水波纹指令
  import { mapGetters } from 'vuex'

  export default {
    name: 'table_sys_log',
    directives: {
      waves
    },
    data() {
      return {
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20
        },
        rules: {
        },
        form: {
        },
        dialogFormVisible: false,
        dialogStatus: '',
        sys_log_add: false,
        sys_log_upd: false,
        sys_log_del: false,
        textMap: {
          update: '编辑',
          create: '创建'
        },
        tableKey: 0
      }
    },
    computed: {
      ...mapGetters([
        'permissions'
      ])
    },
    filters: {
      statusFilter(status) {
        const statusMap = {
          0: '有效',
          1: '无效'
        }
        return statusMap[status]
      }
    },
    created() {
      this.getList()
      this.sys_log_add = this.permissions['sys_log_add']
      this.sys_log_upd = this.permissions['sys_log_upd']
      this.sys_log_del = this.permissions['sys_log_del']
    },
    methods: {
      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.listLoading = false
        })
      },
      handleSizeChange(val) {
        this.listQuery.limit = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.page = val
        this.getList()
      },
      handleDelete(row) {
        delObj(row.id)
          .then(response => {
            this.dialogFormVisible = false
            this.getList()
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
          })
      },
      handleCreate() {
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
      },
      create(formName) {
        const set = this.$refs
        set[formName].validate(valid => {
          if (valid) {
            addObj(this.form)
              .then(() => {
                this.dialogFormVisible = false
                this.getList()
                this.$notify({
                  title: '成功',
                  message: '创建成功',
                  type: 'success',
                  duration: 2000
                })
              })
          } else {
            return false
          }
        })
      },
      cancel(formName) {
        this.dialogFormVisible = false
        const set = this.$refs
        set[formName].resetFields()
      },
      update(formName) {
        const set = this.$refs
        set[formName].validate(valid => {
          if (valid) {
            this.dialogFormVisible = false
            this.form.password = undefined
            putObj(this.form).then(() => {
              this.dialogFormVisible = false
              this.getList()
              this.$notify({
                title: '成功',
                message: '修改成功',
                type: 'success',
                duration: 2000
              })
            })
          } else {
            return false
          }
        })
      },
    }
  }
</script>
