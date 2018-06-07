/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

import Mock from 'mockjs'
export const userInfo = {
    userInfo: {
        username: 'admin',
        name: 'avue',
    },
    roles: ['admin'],
    permission: [
        'sys_crud_btn_add',
        'sys_crud_btn_export',
        'sys_menu_btn_add',
        'sys_menu_btn_edit',
        'sys_menu_btn_del',
        'sys_role_btn1',
        'sys_role_btn2',
        'sys_role_btn3',
        'sys_role_btn4',
        'sys_role_btn5',
        'sys_role_btn6',
    ],//权限级别
}
let List = []
for (let i = 0; i < 5; i++) {
    List.push(Mock.mock({
        id: '@increment',
        name: Mock.mock('@cname'),
        username: Mock.mock('@last'),
        'type|0-1': 0,
        'sex|0-1': 0,
        grade: [0, 1],
        address: Mock.mock('@cparagraph(1, 3)'),
        check: [1, 3, 4]
    }))
}
export const tableData = {
    total: 11,
    pageSize: 10,
    tableData: List
};

