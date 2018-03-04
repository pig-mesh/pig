/**
 * 配置编译环境和线上环境之间的切换
 *
 * baseUrl: 老项目域名地址
 * khglUrl: 客户管理域名地址
 * dicUrl : 字典服务器地址
 * routerMode: 路由模式
 * imgBaseUrl: 图片所在域名地址
 * welUrl :默认欢迎页
 *
 */


let baseUrl = '';
let iconfontVersion = 'j0ic7mgvwddt2o6r';
let iconfontUrl = `//at.alicdn.com/t/font_567566_${iconfontVersion}.css`;
let codeUrl = `/admin/code`
if (process.env.NODE_ENV == 'development') {
	baseUrl = `http://api.frps.shop`;
} else if (process.env.NODE_ENV == 'production') {
	baseUrl = `http://api.frps.shop`;
}

export { baseUrl, iconfontUrl, codeUrl }
