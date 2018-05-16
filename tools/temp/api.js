import request from '@/router/axios'

const path = '{{path}}';
export function getList(query) {
  return request({
    url: `${path}/page`,
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: `${path}`,
    method: 'post',
    data: obj
  })
}

export function delObj(id) {
  return request({
    url: `${path}/${id}`,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: `${path}`,
    method: 'put',
    data: obj
  })
}
