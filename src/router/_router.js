const _import = require('./_import');
import Layout from '@/page/index/'
export default [ {
        path: '/wel',
        component: Layout,
        redirect: '/wel/index',
        children: [
            {
                path: 'index',
                name: '首页',
                component: _import('wel')
            }
        ]
    },
    {
		path: '/',
		name: '主页',
		redirect: '/wel'
	}, {
		path: '/info',
		component: Layout,
		redirect: '/info/index',
		children: [
			{
				path: 'index',
				name: '修改信息',
				component: _import('admin/user/info', 'views')
			}
		]
	},{
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
        { menuId: 2, path: 'user', component: _import('admin/user/index', 'views'), name: '用户管理', meta: { title: '用户管理' } },
        { menuId: 3, path: 'menu', component: _import('admin/menu/index', 'views'), name: '菜单管理', meta: { title: '菜单管理' } },
        { menuId: 4, path: 'role', component: _import('admin/role/index', 'views'), name: '角色管理', meta: { title: '角色管理' } },
        { menuId: 5, path: 'dept', component: _import('admin/dept/index', 'views'), name: '部门管理', meta: { title: '部门管理',keepAlive:true } },
        { menuId: 6, path: 'dict', component: _import('admin/dict/index', 'views'), name: '字典管理', meta: { title: '字典管理' } },
        { menuId: 7, path: 'log', component: _import('admin/log/index', 'views'), name: '日志管理', meta: { title: '日志管理' } }
    ]
}]