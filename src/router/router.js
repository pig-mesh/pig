import Vue from 'vue'
import VueRouter from 'vue-router'
import {
  routerMode
} from '@/config/env'
import store from '@/store'
import {
  formatRoutes
} from '@/util/util'
import Layout from '@/page/index/'
import baseRouter from './_router'
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
  routes: [].concat(...formatRoutes(store.state.user.menu), baseRouter)
})
