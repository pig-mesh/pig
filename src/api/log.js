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
