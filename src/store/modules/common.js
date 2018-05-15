import {
  setStore,
  getStore,
  removeStore
} from '@/util/store'
import {
  validatenull
} from '@/util/validate'
import {
  getDic
} from '@/api/admin'
import {
  baseUrl
} from '@/config/env';
import website from '@/const/website'
const common = {

  state: {
    isCollapse: false,
    isFullScren: false,
    isLock: getStore({
      name: 'isLock'
    }) || false,
    theme: getStore({
      name: 'theme'
    }) || '#409EFF',
    lockPasswd: getStore({
      name: 'lockPasswd'
    }) || '',
    website: website,
  },
  actions: {
    // 获取字典公用类
    GetDic({
      commit,
      state,
      dispatch
    }, dic) {
      return new Promise((resolve, reject) => {
        if (dic instanceof Array) {
          Promise.all(dic.map(ele => getDic(ele))).then(data => {
            const result = {}
            dic.forEach((ele, index) => {
              result[ele] = data[index].data
            })
            resolve(result)
          })
        }
      })
    }
  },
  mutations: {
    SET_COLLAPSE: (state, action) => {
      state.isCollapse = !state.isCollapse
    },
    SET_FULLSCREN: (state, action) => {
      state.isFullScren = !state.isFullScren
    },
    SET_LOCK: (state, action) => {
      state.isLock = true
      setStore({
        name: 'isLock',
        content: state.isLock,
        type: 'session'
      })
    },
    SET_THEME: (state, color) => {
      state.theme = color
      setStore({
        name: 'theme',
        content: state.theme
      })
    },
    SET_LOCK_PASSWD: (state, lockPasswd) => {
      state.lockPasswd = lockPasswd
      setStore({
        name: 'lockPasswd',
        content: state.lockPasswd,
        type: 'session'
      })
    },
    CLEAR_LOCK: (state, action) => {
      state.isLock = false
      state.lockPasswd = ''
      removeStore({
        name: 'lockPasswd'
      })
      removeStore({
        name: 'isLock'
      })
    }
  }
}
export default common
