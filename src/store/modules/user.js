/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

import {
  getToken,
  setToken,
  removeToken
} from '@/util/auth'
import {
  setStore,
  getStore
} from '@/util/store'
import {
  validatenull
} from '@/util/validate'
import {
  loginByUsername,
  mobileLogin,
  getUserInfo,
  logout
} from '@/api/login'
import {
  GetMenu
} from '@/api/menu'
import { encryption } from '@/util/util'
const user = {
  state: {
    userInfo: getStore({
      name: 'userInfo'
    }) || {},
    permissions: getStore({
      name: 'permissions'
    }) || {},
    roles: getStore({
      name: 'roles'
    }) || [],
    menu: getStore({
      name: 'menu'
    }) || [],
    isInitMenu: getStore({
      name: 'isInitMenu'
    }) || false,
    access_token: getStore({
      name: 'access_token'
    }) || '',
    refresh_token: getStore({
      name: 'refresh_token'
    }) || ''
  },
  actions: {
    // 根据用户名登录
    LoginByUsername({
      commit,
      state,
      dispatch
    }, userInfo) {
      return new Promise((resolve, reject) => {
        const user = encryption({
          data: userInfo,
          key: '1234567887654321',
          param: ['password']
        })

        loginByUsername(user.username, user.password, user.code, user.randomStr).then(response => {
          const data = response.data
          setToken(data.access_token)
          commit('SET_ACCESS_TOKEN', data.access_token)
          commit('SET_REFRESH_TOKEN', data.refresh_token)
          commit('CLEAR_LOCK')
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 根据手机号登录
    LoginByPhone({
      commit,
      state,
      dispatch
    }, userInfo) {
      const mobile = userInfo.mobile.trim()
      return new Promise((resolve, reject) => {
        mobileLogin(mobile, userInfo.code).then(response => {
          const data = response.data
          setToken(data.access_token)
          commit('SET_ACCESS_TOKEN', data.access_token)
          commit('SET_REFRESH_TOKEN', data.refresh_token)
          commit('CLEAR_LOCK')
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    GetTableData({
      commit,
      state,
      dispatch
    }, page) {
      return new Promise((resolve, reject) => {
        // 未定义
        // getTableData(page).then(res => {
        //   const data = res.data
        //   resolve(data)
        // })
      })
    },
    GetUserInfo({
      commit,
      state,
      dispatch
    }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
          const data = response.data.data
          commit('SET_ROLES', data.roles)
          commit('SET_USER_INFO', data.sysUser)
          commit('SET_PERMISSIONS', data.permissions)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut({
      commit,
      state
    }) {
      return new Promise((resolve, reject) => {
        logout(state.access_token, state.refresh_token).then(() => {
          // 清除菜单
          commit('SET_MENU', [])
          // 清除权限
          commit('SET_PERMISSIONS', [])
          // 清除用户信息
          commit('SET_USER_INFO', {})
          commit('SET_ACCESS_TOKEN', '')
          commit('SET_REFRESH_TOKEN', '')
          commit('SET_ROLES', [])
          commit('DEL_ALL_TAG')
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 注销session
    FedLogOut({
      commit
    }) {
      return new Promise(resolve => {
        // 清除菜单
        commit('SET_MENU', [])
        // 清除权限
        commit('SET_PERMISSIONS', [])
        // 清除用户信息
        commit('SET_USER_INFO', {})
        commit('SET_ACCESS_TOKEN', '')
        commit('SET_REFRESH_TOKEN', '')
        commit('SET_ROLES', [])
        commit('DEL_ALL_TAG')
        removeToken()
        resolve()
      })
    },
    // 获取系统菜单
    GetMenu({
      commit
    }) {
      return new Promise(resolve => {
        GetMenu().then((res) => {
          const data = res.data
          data.forEach(ele => {
            ele.children.forEach(child => {
              if (!validatenull(child.component)) child.path = `${ele.path}/${child.path}`
            });
          });
          commit('SET_MENU', data)
          resolve(data)
        })
      })
    }
  },
  mutations: {
    SET_ACCESS_TOKEN: (state, access_token) => {
      state.access_token = access_token
      setStore({
        name: 'access_token',
        content: state.access_token,
        type: 'session'
      })
    },
    SET_MENU: (state, menu) => {
      state.menu = menu
      setStore({
        name: 'menu',
        content: state.menu,
        type: 'session'
      })
    },
    SET_USER_INFO: (state, userInfo) => {
      state.userInfo = userInfo
      setStore({
        name: 'userInfo',
        content: state.userInfo,
        type: 'session'
      })
    },
    SET_REFRESH_TOKEN: (state, rfToken) => {
      state.refresh_token = rfToken
      setStore({
        name: 'refresh_token',
        content: state.refresh_token,
        type: 'session'
      })
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
      setStore({
        name: 'roles',
        content: state.roles,
        type: 'session'
      })
    },
    SET_PERMISSIONS: (state, permissions) => {
      const list = {}
      for (let i = 0; i < permissions.length; i++) {
        list[permissions[i]] = true
      }
      state.permissions = list
      setStore({
        name: 'permissions',
        content: state.permissions,
        type: 'session'
      })
    }
  }
}
export default user
