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

export const tableOption = {
  "border": true,
  "index": true,
  "expand": true,
  "stripe": true,
  "selection": true,
  "page": false,
  "menuAlign": "center",
  "defaultSort": {
    prop: 'username',
    order: 'descending'
  },
  "align": "center",
  "dic": [],
  "column": [{
      "label": "用户名",
      "prop": "username",
      "span": 24,
      "solt": true,
      "sortable": true,
      "rules": [{
        "required": true,
        "message": "请输入用户名",
        "trigger": "blur"
      }]
    },
    {
      "label": "类型",
      "prop": "type",
      "width": 80,
      "type": "select",
      "sortable": true,
      "dicData": [{
        "label": '后端',
        "value": '0'
      }, {
        "label": '前端',
        "value": '1'
      }]
    },
    {
      "label": "stars",
      "width": "150",
      "prop": "stars",
      "sortable": true,
      "solt": true,
    },
    {
      "label": "码云",
      "solt": true,
      "span": 24,
      "prop": "address",
      "type": "textarea",
      "overHidden": true
    }, {
      "label": "项目介绍",
      "width": "300",
      "prop": "info",
      "editDisabled": true,
      "type": "textarea",
      "span": 24,
      "maxRow": 4,
      "minRow": 4,
      "overHidden": true
    },
  ]
}
