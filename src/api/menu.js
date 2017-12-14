import request from '@/utils/request'

export function fetchTree(query) {
  return request({
    url: '/admin/menu/tree',
    method: 'get',
    params: query
  })
}

export function fetchAll() {
  return request({
    url: '/admin/menu/navMenu',
    method: 'get'
  })
}

export function fetchUserTree(type) {
  return request({
    url: '/admin/menu/userTree/' + type,
    method: 'get'
  })
}

export function addObj(obj) {
  return request({
    url: '/admin/menu/',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/admin/menu/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/admin/menu/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/admin/menu/',
    method: 'put',
    data: obj
  })
}
