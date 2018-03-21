import Vue from 'vue';
import VueRouter from 'vue-router';
import { routerMode } from '@/config/env';
import store from '@/store'
import { getStore, getSessionStore, vaildUtil } from '@/util/yun'



import Myiframe from '@/components/iframe/iframe.vue'
import Layout from '@/page/index/'
import errorPage404 from '@/components/errorPage/404.vue';
import errorPage403 from '@/components/errorPage/403.vue';
import errorPage500 from '@/components/errorPage/500.vue';
const _import = require('./_import');
Vue.use(VueRouter);
export default new VueRouter({
	// mode: 'history',
	strict: process.env.NODE_ENV !== 'production',
	scrollBehavior(to, from, savedPosition) {
		if (savedPosition) {
			return savedPosition
		} else {
			if (from.meta.keepAlive) {
				from.meta.savedPosition = document.body.scrollTop;
			}
			return {
				x: 0,
				y: to.meta.savedPosition || 0
			}
		}
	},
});
export const asyncRouterMap = [
	{ path: '/login', name: '登录页', component: _import('page', 'login/index') },
	{ path: '/lock', name: '锁屏页', component: _import('page', 'lock/index') },
	{ path: '*', redirect: '/404', hidden: true },
	{ path: '/404', component: errorPage404, name: '404' },
	{ path: '/403', component: errorPage403, name: '403' },
	{ path: '/500', component: errorPage500, name: '500' },
	{
		path: '/',
		name: '主页',
		redirect: '/wel'
	},
	{
		path: '/myiframe',
		component: Layout,
		redirect: '/myiframe',
		children: [
			{
				path: ":routerPath",
				name: 'iframe',
				component: Myiframe,
				props: true
			}
		]

	}, {
		path: '/wel',
		component: Layout,
		redirect: '/wel/index',
		children: [
			{
				path: 'index',
				name: '首页',
				component: _import('page', 'wel')
			}
		]
	}, {
		menuId: 1,
		path: '/admin',
		component: Layout,
		name: '系统管理',
		hidden: false,
		redirect: '/admin/user',
		meta: {
			title: '系统管理',
		},
		children: [
			{ menuId: 2, path: 'user', component: _import('views', 'admin/user/index'), name: '用户管理', meta: { title: '用户管理' } },
			{ menuId: 3, path: 'menu', component: _import('views', 'admin/menu/index'), name: '菜单管理', meta: { title: '菜单管理' } },
			{ menuId: 4, path: 'role', component: _import('views', 'admin/role/index'), name: '角色管理', meta: { title: '角色管理' } },
			{ menuId: 5, path: 'dept', component: _import('views', 'admin/dept/index'), name: '部门管理', meta: { title: '部门管理' } },
			{ menuId: 6, path: 'dict', component: _import('views', 'admin/dict/index'), name: '字典管理', meta: { title: '字典管理' } },
			{ menuId: 7, path: 'log', component: _import('views', 'admin/log/index'), name: '日志管理', meta: { title: '日志管理' } }
		]
	},
]