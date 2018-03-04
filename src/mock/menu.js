import {
  baseUrl
} from '@/config/env'
export const menu = [{
  id: 1,
  label: "首页",
  href: '/wel/index',
  icon: 'icon-huanyingye',
  meta: {},
  children: [],
}, {
  id: 13,
  label: "权限测试页",
  href: '/role/index',
  icon: 'icon-quanxian',
  meta: {
    roles: ['admin'],
  },
  children: [],
}, {
  id: 2,
  label: "第三方网站",
  href: '',
  icon: 'icon-iframe',
  meta: {},
  children: [{
    id: 3,
    label: "百度",
    href: 'https://www.baidu.com',
    icon: 'icon-baidu1',
    children: [],
  }, {
    id: 4,
    label: "搜狐",
    href: 'http://www.shouhu.com',
    icon: 'icon-souhu',
    children: [],
  }, {
    id: 5,
    label: "360",
    href: 'http://www.360.com',
    icon: 'icon-msnui-360',
    children: [],

  }]
}, {
  id: 6,
  label: "表格CRUD",
  href: '/table/index',
  icon: 'icon-biaoge',
  meta: {},
  children: []
}, {
  id: 9,
  label: "表单CRUD",
  href: '/form/index',
  icon: 'icon-biaodan',
  meta: {},
  children: []
}, {
  id: 7,
  label: "阿里图标",
  href: '/iconfont/index',
  icon: 'icon-changyonglogo27',
  meta: {},
  children: []
}, {
  id: 9,
  label: "系统管理",
  icon: 'icon-liuliangyunpingtaitubiao08',
  meta: {},
  children: [
    {
      id: 10,
      label: "用户管理",
      href: '/admin/user',
      icon: 'icon-yonghuguanli',
      children: []
    }, {
      id: 11,
      label: "角色管理",
      href: '/admin/role',
      icon: 'icon-jiaoseguanli',
      children: []
    },
    {
      id: 12,
      label: "菜单管理",
      href: '/admin/menu',
      icon: 'icon-caidanguanli',
      children: []
    }
  ]
}, {
  id: 8,
  label: "综合错误",
  href: '#',
  icon: 'icon-cuowu',
  meta: {},
  children: [
    {
      id: 14,
      label: "错误日志",
      href: '/errlog/index',
      icon: 'icon-rizhi',
      children: []
    }, {
      id: 15,
      label: "错误页面",
      href: '/errlog/page',
      icon: 'icon-cuowutishitubiao',
      children: []
    }
  ]
}
]

