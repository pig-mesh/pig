import request from '@/router/axios'

export function roleList() {
  return request({
    url: '/admin/role/roleList',
    method: 'get'
  })
}

export function fetchList(query) {
  return request({
    url: '/admin/role/rolePage',
    method: 'get',
    params: query
  })
}

export function deptRoleList(deptId) {
  return request({
    url: '/admin/role/roleList/' + deptId,
    method: 'get'
  })
}

export function getObj(id) {
  return request({
    url: '/admin/role/' + id,
    method: 'get'
  })
}

export function addObj(obj) {
  return request({
    url: '/admin/role/',
    method: 'post',
    data: obj
  })
}

export function putObj(obj) {
  return request({
    url: '/admin/role/',
    method: 'put',
    data: obj
  })
}

export function delObj(id) {
  return request({
    url: '/admin/role/' + id,
    method: 'delete'
  })
}

export function permissionUpd(roleId, menuIds) {
  return request({
    url: '/admin/role/roleMenuUpd',
    method: 'put',
    params: {
      roleId: roleId,
      menuIds: menuIds
    }
  })
}

export function fetchRoleTree(roleName) {
  return request({
    url: '/admin/menu/roleTree/' + roleName,
    method: 'get'
  })
}

export function fetchDeptTree(query) {
  return request({
    url: '/admin/dept/tree',
    method: 'get',
    params: query
  })
}
