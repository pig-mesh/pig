import fetch from '@/utils/fetch'

export function roleList() {
  return fetch({
    url: '/admin/role/roleList',
    method: 'get'
  })
}
