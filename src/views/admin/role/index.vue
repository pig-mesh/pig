<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit" v-if="roleManager_btn_add">添加
      </el-button>
    </div>

    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row style="width: 99%">

      <el-table-column align="center" label="序号">
        <template slot-scope="scope">
          <span>{{scope.row.roleId}}</span>
        </template>
      </el-table-column>

      <el-table-column label="角色名称">
        <template slot-scope="scope">
          <span>{{scope.row.roleName}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="角色标识">
        <template slot-scope="scope">
          <span>{{scope.row.roleCode}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="角色描述">
        <template slot-scope="scope">
          <span>{{scope.row.roleDesc }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="所属部门">
        <template slot-scope="scope">
          <span>{{scope.row.deptName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{scope.row.createTime | parseTime('{y}-{m}-{d} {h}:{i}')}}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="success" v-if="roleManager_btn_edit" @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button size="mini" type="danger" v-if="roleManager_btn_del" @click="handleDelete(scope.row)">删除
          </el-button>
          <el-button size="mini" type="info" plain @click="handlePermission(scope.row)" v-if="roleManager_btn_perm">权限
          </el-button>
        </template>
      </el-table-column>

    </el-table>

    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色标识" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="角色标识"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="roleDesc">
          <el-input v-model="form.roleDesc" placeholder="描述"></el-input>
        </el-form-item>
        <el-form-item label="所属部门" prop="roleDept">
          <el-input v-model="form.deptName" placeholder="选择部门" @focus="handleDept()" readonly></el-input>
          <el-input type="hidden" v-model="form.roleDeptId"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">修 改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogDeptVisible">
      <el-tree class="filter-tree" :data="treeDeptData" :default-checked-keys="checkedKeys" check-strictly node-key="id" highlight-current ref="deptTree" @node-click="getNodeData" :props="defaultProps" :filter-node-method="filterNode" default-expand-all>
      </el-tree>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogPermissionVisible">
      <el-tree class="filter-tree" :data="treeData" :default-checked-keys="checkedKeys" check-strictly node-key="id" highlight-current :props="defaultProps" show-checkbox ref="menuTree" :filter-node-method="filterNode" default-expand-all>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updatePermession(roleId, roleCode)">更 新</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  permissionUpd,
  fetchRoleTree,
  fetchDeptTree
} from "@/api/role";
import { fetchTree } from "@/api/menu";
import { mapGetters } from "vuex";
import waves from "@/directive/waves/index.js"; // 水波纹指令

export default {
  name: "table_role",
  directives: {
    waves
  },
  data() {
    return {
      treeData: [],
      treeDeptData: [],
      checkedKeys: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20
      },
      form: {
        roleName: undefined,
        roleCode: undefined,
        roleDesc: undefined,
        deptName: undefined,
        roleDeptId: undefined
      },
      roleId: undefined,
      roleCode: undefined,
      rules: {
        roleName: [
          {
            required: true,
            message: "角色名称",
            trigger: "blur"
          },
          {
            min: 3,
            max: 20,
            message: "长度在 3 到 20 个字符",
            trigger: "blur"
          }
        ],
        roleCode: [
          {
            required: true,
            message: "角色标识",
            trigger: "blur"
          },
          {
            min: 3,
            max: 20,
            message: "长度在 3 到 20 个字符",
            trigger: "blur"
          }
        ],
        roleDesc: [
          {
            required: true,
            message: "角色标识",
            trigger: "blur"
          },
          {
            min: 3,
            max: 20,
            message: "长度在 3 到 20 个字符",
            trigger: "blur"
          }
        ]
      },
      statusOptions: ["0", "1"],
      rolesOptions: undefined,
      dialogFormVisible: false,
      dialogDeptVisible: false,
      dialogPermissionVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑",
        create: "创建",
        permission: "分配权限"
      },
      tableKey: 0,
      tableKey: 0,
      roleManager_btn_add: false,
      roleManager_btn_edit: false,
      roleManager_btn_del: false,
      roleManager_btn_perm: false
    };
  },
  created() {
    this.getList();
    this.roleManager_btn_add = this.permissions["sys_role_add"];
    this.roleManager_btn_edit = this.permissions["sys_role_edit"];
    this.roleManager_btn_del = this.permissions["sys_role_del"];
    this.roleManager_btn_perm = this.permissions["sys_role_perm"];
  },
  computed: {
    ...mapGetters(["elements", "permissions"])
  },
  methods: {
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then(response => {
        this.list = response.data.records;
        this.total = response.data.total;
        this.listLoading = false;
      });
    },
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getList();
    },
    handleCreate() {
      this.resetTemp();
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
    },
    handleUpdate(row) {
      getObj(row.roleId).then(response => {
        this.form = response.data;
        this.form.deptName = row.deptName;
        this.form.roleDeptId = row.roleDeptId;
        this.dialogFormVisible = true;
        this.dialogStatus = "update";
      });
    },
    handlePermission(row) {
      fetchRoleTree(row.roleCode)
        .then(response => {
          this.checkedKeys = response.data;
          return fetchTree();
        })
        .then(response => {
          this.treeData = response.data;
          this.dialogStatus = "permission";
          this.dialogPermissionVisible = true;
          this.roleId = row.roleId;
          this.roleCode = row.roleCode;
        });
    },
    handleDept() {
      fetchDeptTree().then(response => {
        this.treeDeptData = response.data;
        this.dialogDeptVisible = true;
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    getNodeData(data) {
      this.dialogDeptVisible = false;
      this.form.roleDeptId = data.id;
      this.form.deptName = data.name;
      console.log(data);
    },
    handleDelete(row) {
      delObj(row.roleId).then(response => {
        this.dialogFormVisible = false;
        this.getList();
        this.$notify({
          title: "成功",
          message: "删除成功",
          type: "success",
          duration: 2000
        });
      });
    },
    create(formName) {
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          addObj(this.form).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$notify({
              title: "成功",
              message: "创建成功",
              type: "success",
              duration: 2000
            });
          });
        } else {
          return false;
        }
      });
    },
    cancel(formName) {
      this.dialogFormVisible = false;
      this.$refs[formName].resetFields();
    },
    update(formName) {
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          this.dialogFormVisible = false;
          putObj(this.form).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$notify({
              title: "成功",
              message: "修改成功",
              type: "success",
              duration: 2000
            });
          });
        } else {
          return false;
        }
      });
    },
    updatePermession(roleId, roleCode) {
      permissionUpd(roleId, this.$refs.menuTree.getCheckedKeys()).then(() => {
        this.dialogPermissionVisible = false;
        fetchTree()
          .then(response => {
            this.treeData = response.data;
            return fetchRoleTree(roleCode);
          })
          .then(response => {
            this.checkedKeys = response.data;
            this.$notify({
              title: "成功",
              message: "修改成功",
              type: "success",
              duration: 2000
            });
          });
      });
    },
    resetTemp() {
      this.form = {
        id: undefined,
        roleName: undefined,
        roleCode: undefined,
        roleDesc: undefined
      };
    }
  }
};
</script>
