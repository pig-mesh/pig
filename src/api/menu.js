import fetch from 'utils/fetch'

export function fetchTree(query) {
  return fetch({
    url: '/admin/menu/tree',
    method: 'get',
    params: query
  })
}

export function fetchAll() {
  return fetch({
    url: '/admin/menu/navMenu',
    method: 'get'
  })
}

export function fetchUserTree(type) {
  return fetch({
    url: '/admin/menu/userTree/' + type,
    method: 'get'
  })
}

export function addObj(obj) {
  return fetch({
    url: '/admin/menu/',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return fetch({
    url: '/admin/menu/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/admin/menu/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return fetch({
    url: '/admin/menu/',
    method: 'put',
    data: obj
  })
}
