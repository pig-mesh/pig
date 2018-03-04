import { baseUrl, khglUrl, dicUrl } from '@/config/env'
import request from '@/router/axios'
import { userInfo, tableData } from '@/mock/user'
import { menu, menuAll } from '@/mock/menu'
export const loginByUsername = (username, password, code, randomStr) => {
  var grant_type = 'password'
  var scope = 'server'
  return request({
    url: '/auth/oauth/token',
    headers: {
      'Authorization': 'Basic cGlnOnBpZw=='
    },
    method: 'post',
    params: { username, password, randomStr, code, grant_type, scope }
  })
}

export const getUserInfo = () => {
    return new Promise((resolve, reject) => {
        resolve({ data: userInfo });
    })
}
export const getMenu = () => {
    return new Promise((resolve, reject) => {
        resolve({ data: menu });
    })
}
export const getMenuAll = () => {
    return new Promise((resolve, reject) => {
        resolve({ data: menu });
    })
}

export const getTableData = (page) => {
    return new Promise((resolve, reject) => {
        resolve({ data: tableData });
    })
}
export const logout = () => {
    return new Promise((resolve, reject) => {
        resolve();
    })
}



