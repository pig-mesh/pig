import request from '@/router/axios'

export function fetchList(query) {
  return request({
    url: '/admin/dict/dictPage',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/admin/user/',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/admin/user/' + id,
    method: 'get'
  })
}

export function delObj(row) {
  return request({
    url: '/admin/dict/' + row.id + '/' + row.type,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/admin/user/',
    method: 'put',
    data: obj
  })
}

export function remote(type) {
  return request({
    url: '/admin/dict/type/' + type,
    method: 'get'
  })
}
