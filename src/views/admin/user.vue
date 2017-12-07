<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="用户名"
                v-model="listQuery.username">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="search" @click="handleFilter">搜索</el-button>
      <el-button v-if="sys_user_add" class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加
      </el-button>
      <el-button class="filter-item" type="primary" icon="document" @click="handleDownload">导出</el-button>
    </div>

    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit
              highlight-current-row style="width: 100%">

      <el-table-column align="center" label="序号">
        <template scope="scope">
          <span>{{scope.row.userId}}</span>
        </template>
      </el-table-column>

      <el-table-column label="用户名">
        <template scope="scope">
          <span class="link-type" @click="handleUpdate(scope.row)">{{scope.row.title}}</span>
          <span>{{scope.row.username}}</span>
        </template>
      </el-table-column>

      <!--<el-table-column align="center" label="密码" show-overflow-tooltip>-->
        <!--<template scope="scope">-->
          <!--<span>{{scope.row.password}}</span>-->
        <!--</template>-->
      <!--</el-table-column>-->

      <el-table-column align="center" label="角色">
        <template scope="scope">
          <span>{{scope.row.roleList[0].roleName}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间">
        <template scope="scope">
          <span>{{scope.row.createTime | parseTime('{y}-{m}-{d} {h}:{i}')}}</span>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="状态">
        <template scope="scope">
          <el-tag>{{scope.row.delFlag | statusFilter}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template scope="scope">
          <el-button v-if="sys_user_upd" size="small" type="success"
                     @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button v-if="sys_user_del" size="small" type="danger"
                     @click="deletes(scope.row)">删除
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
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输用户名"></el-input>
        </el-form-item>
        <el-form-item v-if="dialogStatus == 'create'" label="密码" placeholder="请输入密码" prop="password">
          <el-input type="password" v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select class="filter-item" v-model="form.role" placeholder="请选择">
            <el-option v-for="item in rolesOptions" :key="item.roleId" :label="item.roleCode" :value="item.roleId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" v-if="dialogStatus == 'update' && sys_user_del " prop="delFlag" >
          <el-select class="filter-item" v-model="form.delFlag" placeholder="请选择">
            <el-option v-for="item in statusOptions" :key="item" :label="item | statusFilter" :value="item"> </el-option>
          </el-select>
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
  import { fetchList, getObj, addObj, putObj, delObj } from '@/api/user'
  import { roleList } from '@/api/role'
  import waves from '@/directive/waves/index.js' // 水波纹指令
  import { parseTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: 'table_user',
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
        form: {
          username: undefined,
          password: undefined,
          delFlag: undefined,
          role: undefined
        },
        rules: {
          username: [
            {
              required: true,
              message: '请输入账户',
              trigger: 'blur'
            },
            {
              min: 3,
              max: 20,
              message: '长度在 3 到 20 个字符',
              trigger: 'blur'
            }
          ],
          password: [
            {
              required: true,
              message: '请输入密码',
              trigger: 'blur'
            },
            {
              min: 5,
              max: 20,
              message: '长度在 5 到 20 个字符',
              trigger: 'blur'
            }
          ]
        },
        statusOptions: ['0', '1'],
        rolesOptions: [],
        dialogFormVisible: false,
        userAdd: false,
        userUpd: false,
        userDel: false,
        dialogStatus: '',
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
      this.sys_user_add = this.permissions['sys_user_add']
      this.sys_user_upd = this.permissions['sys_user_upd']
      this.sys_user_del = this.permissions['sys_user_del']
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
      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },
      handleSizeChange(val) {
        this.listQuery.limit = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.page = val
        this.getList()
      },
      handleCreate() {
        this.resetTemp()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        roleList()
          .then(response => {
            this.rolesOptions = response.data
          })
      },
      handleUpdate(row) {
        getObj(row.userId)
          .then(response => {
            this.form = response.data
            this.form.role = row.roleList[0].roleId
            this.dialogFormVisible = true
            this.dialogStatus = 'update'
          })
        roleList()
          .then(response => {
            this.rolesOptions = response.data
          })
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
        this.$refs[formName].resetFields()
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
      deletes(row) {
        this.$confirm('此操作将永久删除该用户(用户名:' + row.username + '), 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delObj(row.userId).then(() => {
            this.getList()
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
          }).cache(() => {
            this.$notify({
              title: '失败',
              message: '删除失败',
              type: 'error',
              duration: 2000
            })
          })
        })
      },
      resetTemp() {
        this.form = {
          id: undefined,
          username: '',
          password: '',
          role: undefined
        }
      },
      handleDownload() {
        require.ensure([], () => {
          const { export_json_to_excel } = require('vendor/Export2Excel')
          const tHeader = ['用户名', '密码', '状态', '创建时间']
          const filterVal = ['username', 'password', 'delFlag', 'createTime']
          const data = this.formatJson(filterVal, this.list)
          export_json_to_excel(tHeader, data, '用户数据')
        })
      },
      formatJson(filterVal, jsonData) {
        return jsonData.map(v => filterVal.map(j => {
          if (j === 'createTime') {
            return parseTime(v[j])
          } else {
            return v[j]
          }
        }))
      }
    }
  }
</script>
