import request from '@/router/axios'

export function fetchList(query) {
  return request({
    url: '/admin/route/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/admin/route/',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/admin/route/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/admin/route/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/admin/route',
    method: 'put',
    data: obj
  })
}

export function applyObj() {
  return request({
    url: '/admin/route/apply',
    method: 'get'
  })
}
