import Vue from 'vue'
import Router from 'vue-router'
const _import = require('./_import_' + process.env.NODE_ENV)
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading

Vue.use(Router)

/* layout */
import Layout from '../views/layout/Layout'

/**
* icon : the icon show in the sidebar
* hidden : if `hidden:true` will not show in the sidebar
* redirect : if `redirect:noredirect` will no redirct in the levelbar
* noDropdown : if `noDropdown:true` will has no submenu
* meta : { role: ['admin'] }  will control the page role
**/
export const constantRouterMap = [
    { path: '/login', component: _import('login/index'), hidden: true },
    { path: '/authredirect', component: _import('login/authredirect'), hidden: true },
    { path: '/404', component: _import('errorPage/404'), hidden: true },
    { path: '/401', component: _import('errorPage/401'), hidden: true },
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    name: '首页',
    hidden: true,
    children: [{ path: 'index', component: _import('index') },
      { path: '/upload', name: '修改信息', component: _import('admin/info'), hidden: true },
    ]
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    menuId: 1,
    path: '/admin',
    component: Layout,
    icon: 'table',
    name: '系统管理',
    children: [
      { menuId: 2, path: 'user', component: _import('admin/user'), name: '用户管理' },
      { menuId: 3, path: 'menu', component: _import('admin/menu'), name: '菜单管理' },
      { menuId: 4, path: 'role', component: _import('admin/role'), name: '角色管理' },
      { menuId: 6, path: 'dict', component: _import('admin/dict'), name: '字典管理' },
      { menuId: 5, path: 'log', component: _import('admin/log'), name: '日志管理' },
    ]
  }
]
