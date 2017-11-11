import fetch from '@/utils/fetch'

export function loginByUsername(username, password) {
  var grant_type = 'password'
  var scope = 'server'

  return fetch({
    url: '/auth/oauth/token',
    headers: {
      'Authorization': 'Basic cGlnOnBpZw=='
    },
    method: 'post',
    params: { username, password, grant_type, scope }
  })
}

export function logout(accesstoken, refreshToken) {
  return fetch({
    url: '/auth/removeToken',
    method: 'post',
    params: { accesstoken, refreshToken }
  })
}

export function getUserInfo(token) {
  return fetch({
    url: '/admin/user/info',
    method: 'get'
  })
}

