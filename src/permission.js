import router from './router/router'
import store from './store'
import { getToken } from '@/util/auth'
import { vaildUtil } from '@/util/yun';
import { setTitle } from '@/util/util';
import { validatenull } from '@/util/validate';
import { asyncRouterMap } from '@/router/router'
import { Duplex } from 'stream';
function hasPermission(roles, permissionRoles) {
    if (!permissionRoles) return true
    return roles.some(role => permissionRoles.indexOf(role) >= 0)
}
const whiteList = ['/login', '/404', '/401']
const lockPage = '/lock'
router.addRoutes(asyncRouterMap); // 动态添加可访问路由表
router.beforeEach((to, from, next) => {
    store.commit('SET_TAG', to.query.src ? to.query.src : to.path);
    if (store.getters.access_token) { // determine if there has token
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
function findMenuParent(tag) {
    let tagCurrent = [];
    const menu = store.getters.menu;
    //如果是一级菜单直接返回
    for (let i = 0, j = menu.length; i < j; i++) {
        if (menu[i].path == tag.value) {
            tagCurrent.push(tag);
            return tagCurrent;
        }
    }

    let currentPathObj = menu.filter(item => {
        if (item.children.length == 1) {
            return item.children[0].path === tag.value;
        } else {
            let i = 0;
            let childArr = item.children;
            let len = childArr.length;
            while (i < len) {
                if (childArr[i].path === tag.value) {
                    return true;
                    break;
                }
                i++;
            }
            return false;
        }
    })[0];
    if (!validatenull(tagCurrent)) {
        tagCurrent.push({
            label: currentPathObj.label,
            value: currentPathObj.path
        });
    }
    tagCurrent.push(tag);
    return tagCurrent;

}
router.afterEach((to, from) => {
    setTimeout(() => {
        const tag = store.getters.tag;
        setTitle(tag.label);
        store.commit('SET_TAG_CURRENT', findMenuParent(tag));
    }, 0);
})
