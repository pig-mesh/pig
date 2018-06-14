<!--
  -    Copyright (c) 2018-2025, lengleng All rights reserved.
  -
  - Redistribution and use in source and binary forms, with or without
  - modification, are permitted provided that the following conditions are met:
  -
  - Redistributions of source code must retain the above copyright notice,
  - this list of conditions and the following disclaimer.
  - Redistributions in binary form must reproduce the above copyright
  - notice, this list of conditions and the following disclaimer in the
  - documentation and/or other materials provided with the distribution.
  - Neither the name of the pig4cloud.com developer nor the names of its
  - contributors may be used to endorse or promote products derived from
  - this software without specific prior written permission.
  - Author: lengleng (wangiegie@gmail.com)
  -->

<template>
  <div class="app-container calendar-list-container">
    <el-row>
      <el-col :span="12">
        <div class="grid-content bg-purple">
          <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-width="100px" class="demo-ruleForm">
            <el-form-item label="用户名" prop="username">
              <el-input type="text" :value="userInfo.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="原密码" prop="password">
              <el-input type="password" v-model="ruleForm2.password" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="newpassword1">
              <el-input type="password" v-model="ruleForm2.newpassword1" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="newpassword2">
              <el-input type="password" v-model="ruleForm2.newpassword2" auto-complete="off" ></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="ruleForm2.phone" placeholder="验证码登录使用"></el-input>
            </el-form-item>
            <el-form-item label="头像">
              <my-upload field="file" @crop-upload-success="cropUploadSuccess" v-model="show" :width="300" :height="300" url="/admin/user/upload" :headers="headers" img-format="png"></my-upload>
              <img :src="ruleForm2.avatar">
              <el-button type="primary" @click="toggleShow" size="mini">选择
                <i class="el-icon-upload el-icon--right"></i>
              </el-button>
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
import { mapState } from "vuex";
import myUpload from "vue-image-crop-upload";
import { getToken } from "@/util/auth";
import ElFormItem from "element-ui/packages/form/src/form-item.vue";
import request from "@/router/axios";

export default {
  components: {
    ElFormItem,
    'my-upload': myUpload
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (this.ruleForm2.password !== '') {
        if (value === '') {
          callback(new Error('请输入密码'))
        } else if (value.length < 6) {
          callback(new Error('密码不能小于6位'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (this.ruleForm2.password !== '') {
        if (value === '') {
          callback(new Error('请再次输入密码'))
        } else if (value !== this.ruleForm2.newpassword1) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
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
        avatar: '',
        phone: ''
      },
      rules2: {
        newpassword1: [{ validator: validatePass, trigger: 'blur' }],
        newpassword2: [{ validator: validatePass2, trigger: 'blur' }]
      }
    }
  },
  created() {
    this.ruleForm2.avatar = this.userInfo.avatar
    this.ruleForm2.phone = this.userInfo.phone
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          request({
            url: '/admin/user/editInfo',
            method: 'put',
            data: this.ruleForm2
          })
            .then(response => {
              if (response.data.data) {
                this.userInfo.phone = this.ruleForm2.phone
                this.userInfo.avatar = this.ruleForm2.avatar
                this.$notify({
                  title: '成功',
                  message: '修改成功',
                  type: 'success',
                  duration: 2000
                });
                // 修改密码之后强制重新登录
                if (this.ruleForm2.newpassword1 !== '') {
                  this.$store.dispatch('LogOut').then(() => {
                    location.reload() // 为了重新实例化vue-router对象 避免bug
                  });
                } else {
                  this.$router.push({ path: '/' })
                }
              } else {
                this.$notify({
                  title: '失败',
                  message: response.data.msg,
                  type: 'error',
                  duration: 2000
                })
              }
            })
            .catch(() => {
              this.$notify({
                title: '失败',
                message: '修改失败',
                type: 'error',
                duration: 2000
              });
            });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      console.log(formName)
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
      this.$store.commit('SET_AVATAR', jsonData.filename);
    }
  }
};
</script>
