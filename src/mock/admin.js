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
import { dateFormat } from '@/filters/'
let userList = []
for (let i = 0; i < 2; i++) {
    userList.push(Mock.mock({
        id: '@increment',
        name: Mock.mock('@cname'),
        username: Mock.mock('@last'),
        grade: [0, 1],
        state: 0,
        date: dateFormat(new Date()),
    }))
}
export const userTableData = {
    total: 11,
    pageSize: 10,
    tableData: userList
};


let roleList = []
for (let i = 0; i < 2; i++) {
    roleList.push(Mock.mock({
        id: '@increment',
        name: Mock.mock('@cname'),
        date: dateFormat(new Date()),
        check: [1, 3, 5]
    }))
}
export const roleTableData = {
    total: 11,
    pageSize: 10,
    tableData: roleList
};

