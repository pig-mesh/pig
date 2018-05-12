import Vue from 'vue'
import VueRouter from 'vue-router'
import { routerMode } from '@/config/env'
import store from '@/store'
import { formatRoutes } from '@/util/util'
import Layout from '@/page/index/'
const _import = require('./_import')

Vue.use(VueRouter)

export default new VueRouter({
  // mode: 'history',
  strict: process.env.NODE_ENV !== 'production',
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      if (from.meta.keepAlive) {
        from.meta.savedPosition = document.body.scrollTop
      }
      return {
        x: 0,
        y: to.meta.savedPosition || 0
      }
    }
  },
  routes: [
    {
      path: '/',
      name: '主页',
      redirect: '/wel'
    }, {
      path: '/wel',
      component: Layout,
      redirect: '/wel/index',
      children: [{
        path: 'index',
        name: '首页',
        component: _import('wel')
      }]
    }, {
      path: '*',
      redirect: '/404',
      hidden: true
    }, {
      path: '/login',
      name: '登录页',
      component: _import('login/index')
    }, {
      path: '/lock',
      name: '锁屏页',
      component: _import('lock/index')
    }, {
      path: '/crud',
      component: Layout,
      redirect: '/crud/index',
      children: [{
        path: 'index',
        name: 'crud实例',
        component: _import('crud/index', 'views')
      }]
    }, {
      path: '/info',
      component: Layout,
      redirect: '/info/index',
      children: [{
        path: 'index',
        name: '修改信息',
        component: _import('admin/user/info', 'views')
      }]
    }, {
      path: '/404',
      component: _import('error-page/404', 'components'),
      name: '404'
    }, {
      path: '/403',
      component: _import('error-page/403', 'components'),
      name: '403'
    }, {
      path: '/500',
      component: _import('error-page/500', 'components'),
      name: '500'
    }, {
      path: '/myiframe',
      component: Layout,
      redirect: '/myiframe',
      meta: {
        keepAlive: true
      },
      children: [{
        path: ':routerPath',
        name: 'iframe',
        component: _import('iframe/main', 'components'),
        props: true
      }]
    }
  // 防止F5刷新页面，app重新加载，动态新增路由消失
  ].concat(...formatRoutes(store.state.user.menu))
})
