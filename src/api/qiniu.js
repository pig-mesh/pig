import fetch from '@/utils/fetch'

export function getToken() {
  return fetch({
    url: '/zuul/admin/user/upload', // 假地址 自行替换
    method: 'post'
  })
}
