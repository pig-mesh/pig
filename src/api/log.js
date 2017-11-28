import fetch from '@/utils/fetch'

export function fetchList(query) {
  return fetch({
    url: '/admin/log/logPage',
    method: 'get',
    params: query
  })
}

export function delObj(id) {
  return fetch({
    url: '/admin/log/' + id,
    method: 'delete'
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

export function putObj(obj) {
  return fetch({
    url: '/admin/user/',
    method: 'put',
    data: obj
  })
}
