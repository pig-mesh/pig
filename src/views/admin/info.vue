<template>
  <div class="app-container calendar-list-container">
    <el-row>
      <el-col :span="12">
        <div class="grid-content bg-purple">
          <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-width="100px" class="demo-ruleForm">
            <el-form-item label="用户名" prop="username">
              <el-input type="text" :value="name" disabled></el-input>
            </el-form-item>
            <el-form-item label="原密码" prop="pass">
              <el-input type="password" v-model="ruleForm2.password" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="pass">
              <el-input type="password" v-model="ruleForm2.newpassword1" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
              <el-input type="password" v-model="ruleForm2.newpassword2" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="头像">
              <my-upload field="file"
                         @crop-upload-success="cropUploadSuccess"
                         v-model="show"
                         :width="300"
                         :height="300"
                         url="/admin/user/upload"
                         :headers="headers"
                         img-format="png"></my-upload>
              <img :src="avatar">
              <el-button type="primary" @click="toggleShow" size="mini">选择<i class="el-icon-upload el-icon--right"></i></el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm2')">提交</el-button>
              <el-button @click="resetForm('ruleForm2')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>

  </div>
</template>


<script>
import { mapGetters } from 'vuex'
import myUpload from 'vue-image-crop-upload'
import { getToken } from '@/utils/auth'
import ElFormItem from '../../../node_modules/element-ui/packages/form/src/form-item.vue'
import fetch from '@/utils/fetch'

export default {
  components: {
    ElFormItem,
    'my-upload': myUpload },
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm2.newpassword1 !== '') {
          this.$refs.ruleForm2.validateField('newpassword1')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm2.newpassword2) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      fileList: [],
      show: false,
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      ruleForm2: {
        password: '',
        newpassword1: '',
        newpassword2: '',
        avatar: ''
      },
      rules2: {
        newpassword1: [
          { validator: validatePass, trigger: 'blur' }
        ],
        newpassword2: [
          { validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'name', 'avatar'
    ])
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.ruleForm2.avatar = this.avatar
          fetch({
            url: '/admin/user/editInfo',
            method: 'put',
            data: this.ruleForm2
          }).then((response) => {
            if (response) {
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.$router.push({ path: '/' })
            } else {
              this.$notify({
                title: '失败',
                message: response,
                type: 'fail',
                duration: 2000
              })
            }
          }).catch(() => {
            this.$notify({
              title: '失败',
              message: '修改失败',
              type: 'fail',
              duration: 2000
            })
          })
        } else {
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    toggleShow() {
      this.show = !this.show
    },
    /**
     * upload success
     *
     * [param] jsonData   服务器返回数据，已进行json转码
     * [param] field
     */
    cropUploadSuccess(jsonData, field) {
      console.log('-------- upload success --------')
      this.$store.commit('SET_AVATAR', jsonData.filename)
    }
  }
}
</script>
