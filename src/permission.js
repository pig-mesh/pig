import router from './router/router'
import store from './store'
import { getToken } from '@/util/auth'
import { vaildUtil } from '@/util/yun';
import { setTitle } from '@/util/util';
import { validatenull } from '@/util/validate';
import { asyncRouterMap } from '@/router/router'
function hasPermission(roles, permissionRoles) {
    if (!permissionRoles) return true
    return roles.some(role => permissionRoles.indexOf(role) >= 0)
}
const whiteList = ['/login', '/404', '/401']
const lockPage = '/lock'
router.addRoutes(asyncRouterMap); // 动态添加可访问路由表
router.beforeEach((to, from, next) => {
    store.commit('SET_TAG', from.query.src ? from.query.src : from.path);
    if (getToken()) { // determine if there has token
        /* has token*/
        if (store.getters.isLock && to.path != lockPage) {
            next({ path: lockPage })
        } else if (to.path === '/login') {
            next({ path: '/' })
        } else {
            if (store.getters.roles.length === 0) {
                store.dispatch('GetUserInfo').then(res => {
                    const roles = res.roles
                    next({ ...to, replace: true })
                }).catch(() => {
                    store.dispatch('FedLogOut').then(() => {
                        next({ path: '/login' })
                    })
                })
            } else {
                next()
            }
        }
    } else {
        /* has no token*/
        if (whiteList.indexOf(to.path) !== -1) {
            next()
        } else {
            next('/login')
        }
    }
})

//寻找子菜单的父类
function findMenuParent(tagCurrent, tag, tagWel) {
    let index = -1;
    tagCurrent.forEach((ele, i) => {
        if (ele.value == tag.value) {
            index = i;
        }
    })
    if (tag.value == tagWel.value) {//判断是否为首页
        tagCurrent = [tagWel];
    } else if (index != -1) {//判断是否存在了
        tagCurrent.splice(index, tagCurrent.length - 1);
    } else {//其他操作
        let currentPathObj = store.getters.menu.filter(item => {
            if (item.children.length == 1) {
                return item.children[0].href === tag.value;
            } else {
                let i = 0;
                let childArr = item.children;
                let len = childArr.length;
                while (i < len) {
                    if (childArr[i].href === tag.value) {
                        return true;
                    }
                    i++;
                }
                return false;
            }
        })[0];
        tagCurrent = [tagWel];
        validatenull(currentPathObj) ? '' : tagCurrent.push(currentPathObj);
        tagCurrent.push(tag);
    }
    return tagCurrent;
}
router.afterEach((to, from) => {
    setTimeout(() => {
        const tag = store.getters.tag;
        const tagWel = store.getters.tagWel;
        let tagCurrent = store.getters.tagCurrent;
        setTitle(tag.label);
        store.commit('SET_TAG_CURRENT', findMenuParent(tagCurrent, tag, tagWel));
    }, 0);
})
