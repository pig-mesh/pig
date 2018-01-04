import request from '@/utils/request'

export function login(username, password, randomStr, code) {
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

export function getInfo(token) {
  return request({
    url: '/admin/user/info',
    method: 'get'
  })
}

export function logout(accesstoken, refreshToken) {
  return request({
    url: '/auth/removeToken',
    method: 'post',
    params: { accesstoken, refreshToken }
  })
}
