import fetch from '@/utils/fetch'

export function fetchList(query) {
  console.log(query)
  return fetch({
    url: '/admin/user/userPage',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return fetch({
    url: '/admin/user/',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return fetch({
    url: '/admin/user/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/admin/user/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return fetch({
    url: '/admin/user/',
    method: 'put',
    data: obj
  })
}

