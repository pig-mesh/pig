import fetch from '@/utils/fetch'

export function roleList() {
  return fetch({
    url: '/admin/role/roleList',
    method: 'get'
  })
}

export function fetchList(query) {
  return fetch({
    url: '/admin/role/rolePage',
    method: 'get',
    params: query
  })
}

export function getObj(id) {
  return fetch({
    url: '/admin/role/' + id,
    method: 'get'
  })
}

export function addObj(obj) {
  return fetch({
    url: '/admin/role/',
    method: 'post',
    data: obj
  })
}

export function putObj(obj) {
  return fetch({
    url: '/admin/role/',
    method: 'put',
    data: obj
  })
}

export function delObj(id) {
  return fetch({
    url: '/admin/role/' + id,
    method: 'delete'
  })
}
