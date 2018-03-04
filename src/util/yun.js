import { Notification, Message } from 'element-ui';
/**
 * 获取url参数
 */
export const GetQueryString = (url, name) => {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = url.substr(url.indexOf('?') + 1).match(reg);
	if (r != null) return unescape(r[2]);
	return null;
}
export const parseParam = (param) => {

	let paramStr = "";

	if (param instanceof Object) {

		for (let o in param) {
			if (!vaildUtil.ifnull(param[o])) {
				paramStr = `${paramStr}${o}=${param[o]}&`
			}

		}

	}

	return paramStr.substr(0, paramStr.length - 1);

};

/**
 * 弹窗
 */
export const messageBox = function (conf) {
	if (!(conf instanceof Object)) {
		Message({
			title: '错误提示',
			message: conf,
			type: 'info',
		})
	} else if (conf.type == 1) {
		Notification({
			title: conf.title,
			message: conf.msg,
			type: conf.boxtype,
		})
	} else {
		Message({
			title: conf.title,
			message: conf.msg,
			type: conf.boxtype,
		})
	}
}

/**
 * 计算年的时间差
 */
export const nowCovyearOld = function (date) {
	return dateFormat(new Date(), 'yyyy') - date;
}
/**
 * 计算到当年年份的时间差
 */
export const nowCri = function (date) {
	if (new Date().getTime() - new Date(date).getTime() < 0) return true;
	return false;
}
/**
 * 计算年俩个年份的时间差
 */
export const dateCri = function (date1, date2) {
	if (new Date(date2).getTime() - new Date(date1).getTime() < 0) return true;
	return false;
}
//根据个数返回相应的数组
export const getArraynum = function (len) {
	let list = [];
	for (let i = 1; i <= len; i++) {
		list.push(i);
	}
	return list;
}
/**
 * 根据身份证计算年龄，性别
 */
export const IdCard = function (UUserCard, num) {
	if (UUserCard.length == 18) {
		if (num == 1) {
			//获取出生日期
			let birth = ''
			birth = UUserCard.substring(6, 10) + "-" + UUserCard.substring(10, 12) + "-" + UUserCard.substring(12, 14);
			return birth;
		}
		if (num == 2) {
			//获取性别
			if (parseInt(UUserCard.substr(16, 1)) % 2 == 1) {
				//男
				return "1";
			} else {
				//女
				return "2";
			}
		}
		if (num == 3) {
			//获取年龄
			var myDate = new Date();
			var month = myDate.getMonth() + 1;
			var day = myDate.getDate();
			var age = myDate.getFullYear() - UUserCard.substring(6, 10) - 1;
			if (UUserCard.substring(10, 12) < month || UUserCard.substring(10, 12) == month && UUserCard.substring(12, 14) <= day) {
				age++;
			}
			return age;
		}
	}
	return '';

}
/**
 * 根据传入的list和制定的属性求和
 */
export const calcListnum = function (list, value) {
	let num = 0;
	for (let i = 0; i < list.length; i++) {
		var o = list[i];
		num = num + Number(o[value]);
	}
	return Number(num);
}
/**
 * Object的属性致空，但是属性存在
 */
export const setObjectnull = function (obj) {
	for (var o in obj) {
		obj[o] = "";
	}
	return obj;
}
/**
 * Object的属性为null的至为空字符串
 */
export const setObjectstr = function (obj) {
	for (var o in obj) {
		if (obj[o] == null || obj[o] == 'null') {
			obj[o] = "";
		}
	}
	return obj;
}
/**
 * 字符串数组转对象数组
 */
export const strCovArray = function () {
	let list = [];
	for (let i = 0; i < arguments.length; i++) {
		const str = arguments[i];
		if (!!str) {
			list.push(str);
		}
	}
	return list;
}
/**
 * JSON格式化
 */
export const formatJson = function (json, options) {
	var reg = null,
		formatted = '',
		pad = 0,
		PADDING = '    ';
	options = options || {};
	options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
	options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;
	if (typeof json !== 'string') {
		json = JSON.stringify(json);
	} else {
		try {
			json = JSON.parse(json);
		} catch (e) {
			new Error('不是JSON对象');
		}

		json = JSON.stringify(json);
	}
	reg = /([\{\}])/g;
	json = json.replace(reg, '\r\n$1\r\n');
	reg = /([\[\]])/g;
	json = json.replace(reg, '\r\n$1\r\n');
	reg = /(\,)/g;
	json = json.replace(reg, '$1\r\n');
	reg = /(\r\n\r\n)/g;
	json = json.replace(reg, '\r\n');
	reg = /\r\n\,/g;
	json = json.replace(reg, ',');
	if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
		reg = /\:\r\n\{/g;
		json = json.replace(reg, ':{');
		reg = /\:\r\n\[/g;
		json = json.replace(reg, ':[');
	}
	if (options.spaceAfterColon) {
		reg = /\:/g;
		json = json.replace(reg, ':');
	}
	(json.split('\r\n')).forEach(function (node, index) {
		var i = 0,
			indent = 0,
			padding = '';

		if (node.match(/\{$/) || node.match(/\[$/)) {
			indent = 1;
		} else if (node.match(/\}/) || node.match(/\]/)) {
			if (pad !== 0) {
				pad -= 1;
			}
		} else {
			indent = 0;
		}

		for (i = 0; i < pad; i++) {
			padding += PADDING;
		}

		formatted += padding + node + '\r\n';
		pad += indent;
	});
	return formatted;
}



/**
 * 查找字符串是否存在
 */
export const findStrArray = (dic, value) => {
	if (!vaildUtil.ifnull(dic)) {
		for (let i = 0; i < dic.length; i++) {
			if (dic[i] == value) {
				return i;
				break;
			}
		}
	}
	return -1;
}
/**
 * 查找对象数组是否存在
 */
export const findObjArray = (dic, obj, v1, v2) => {
	v1 = v1 || 'value';
	v2 = v2 || 'value';
	for (let i = 0; i < dic.length; i++) {
		let o = dic[i];
		if (o[v1] == obj[v2]) {
			return i;
			break;
		}
	}
	return -1;
}
/**
 * 根据字典的值取缓存里面的
 */
export const getDicvalue = (obj) => {
	var flag = 0;
	if (obj == 'Area_CD1' || obj == 'INDUSTRY_CLASS') {
		flag = 1000;
	}
	if (localStorage.getItem(obj) == null) {
		return eval(obj);
	} else {
		return JSON.parse(localStorage.getItem(obj));
	}
}
/**
 * 验证俩个对象的属性值是否相等
 */
export const validObj = (olds, news) => {
	var flag = true;
	for (var obj in news) {
		if (news[obj] != olds[obj]) {
			flag = false;
			break;
		}
	}
	return flag;
}
/**
 * 数据转换器
 */
export const dataFormat = (data, type) => {
	data = getSessionStore(data) || getStore(data) || null;
	if (vaildUtil.ifnull(data)) return undefined;
	if (type == 1) {	//转json对象
		return JSON.parse(data);
	} else if (type == 2) {//转Boolean对象
		return eval(data);
	} else {
		return data;
	}
}

//删除数组制定元素
export const removeByValue = (arr, val) => {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i] == val) {
			arr.splice(i, 1);
			return arr;
			break;
		}
	}
}

