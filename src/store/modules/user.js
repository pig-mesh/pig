import { getToken, setToken, removeToken } from '@/util/auth'
import { setStore, getStore } from '@/util/store'
import { validatenull } from '@/util/validate'
import { loginByUsername, getUserInfo, getTableData, getMenu, logout, getMenuAll } from '@/api/user'
const user = {
    state: {
        userInfo: {},
        permission: getStore('permission') || {},
        roles: getStore('roles') || [],
        menu: [],
        menuAll: [],
        token: getStore('token') || '',
    },
    actions: {
        //根据用户名登录
        LoginByUsername({ commit, state, dispatch }, userInfo) {
            return new Promise((resolve, reject) => {
                loginByUsername(userInfo.username, userInfo.password, userInfo.code, userInfo.randomStr).then(response => {
                    const data = response.data
                    setToken(data.access_token)
                    commit('SET_TOKEN', data.access_token)
                    commit('SET_REFRESH_TOKEN', data.refresh_token)
                    commit('CLEAR_LOCK');
                    resolve();
                })
            })
        },
        //根据手机号登录
        LoginByPhone({ commit, state, dispatch }, userInfo) {
            return new Promise((resolve, reject) => {
                loginByUsername(userInfo.phone, userInfo.code).then(res => {
                    const data = response.data
                    setToken(data.access_token)
                    commit('SET_TOKEN', data.access_token)
                    commit('SET_REFRESH_TOKEN', data.refresh_token)
                    commit('CLEAR_LOCK');
                    resolve();
                })
            })
        },
        GetTableData({ commit, state, dispatch }, page) {
            return new Promise((resolve, reject) => {
                getTableData(page).then(res => {
                    const data = res.data;
                    resolve(data);
                })
            })
        },
        GetUserInfo({ commit, state, dispatch }) {
            return new Promise((resolve, reject) => {
              getUserInfo(state.token).then(response => {
                const data = response.data.data
                commit('SET_ROLES', data.roles)
                commit('SET_NAME', data.sysUser.username)
                commit('SET_AVATAR', data.sysUser.avatar)
                commit('SET_INTRODUCTION', data.sysUser.introduction)
                const permissions = {}
                for (let i = 0; i < data.permissions.length; i++) {
                  permissions[data.permissions[i]] = true
                }
                commit('SET_PERMISSIONS', permissions)
                resolve(response)
              }).catch(error => {
                reject(error)
              })
            })
        },
        // 登出
        LogOut({ commit, state }) {
            return new Promise((resolve, reject) => {
                logout().then(() => {
                    commit('SET_TOKEN', '')
                    commit('SET_ROLES', [])
                    commit('DEL_ALL_TAG');
                    removeToken()
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },
        //注销session
        FedLogOut({ commit }) {
            return new Promise(resolve => {
                commit('SET_TOKEN', '')
                commit('DEL_ALL_TAG');
                removeToken()
                resolve()
            })
        },
        //获取系统菜单
        GetMenu({ commit }) {
            return new Promise(resolve => {
                getMenu().then((res) => {
                    const data = res.data;
                    commit('SET_MENU', data);
                    resolve(data);
                })
            })
        },
        //获取全部菜单
        GetMenuAll({ commit }) {
            return new Promise(resolve => {
                getMenuAll().then((res) => {
                    const data = res.data;
                    commit('SET_MENU_ALL', data);
                    resolve(data);
                })
            })
        },

    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token;
            setStore({ name: 'token', content: state.token, type: 'session' })
        },
        SET_MENU: (state, menu) => {
            const list = menu.filter(ele => {
                if (validatenull(ele.meta.roles)) {
                    return true;
                }
                if (ele.meta.roles.indexOf(state.roles[0]) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
            state.menu = list;
        },
        SET_MENU_ALL: (state, menuAll) => {
            state.menuAll = menuAll;
        },
        SET_TOKEN: (state, token) => {
          state.token = token
        },
        SET_REFRESH_TOKEN: (state, rfToken) => {
          state.refresh_token = rfToken
        },
        SET_INTRODUCTION: (state, introduction) => {
          state.introduction = introduction
        },
        SET_NAME: (state, name) => {
          state.name = name
        },
        SET_AVATAR: (state, avatar) => {
          state.avatar = avatar
        },
        SET_ROLES: (state, roles) => {
          state.roles = roles
        },
        SET_PERMISSIONS: (state, permissions) => {
          state.permissions = permissions
        }
    }
}
export default user
