import Vue from 'vue'
import VueAxios from 'vue-axios'
import ElementUI from 'element-ui'
import VueMoment from 'vue-moment'
import App from './App';
import "babel-polyfill";
import axios from './router/axios'
import './permission' // 权限
import './errorLog' // 错误日志
import router from './router/router'
import store from './store'
import AVUE from 'avue-cli/lib/avue.js'
import {
  loadStyle
} from './util/util'
import * as urls from '@/config/env'
import {
  iconfontUrl,
  iconfontVersion
} from '@/config/env'
import * as filters from './filters' // 全局filter
import './styles/common.scss'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueMoment)
Vue.use(VueAxios, axios)

Object.keys(urls).forEach(key => {
  Vue.prototype[key] = urls[key]
})

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

iconfontVersion.forEach(ele => {
  loadStyle(iconfontUrl.replace('$key', ele))
})


export function createApp() {
  const app = new Vue({
    router,
    store,
    render: h => h(App)
  })
  return {
    app,
    router,
    store
  }
}
