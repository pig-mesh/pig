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

import Vue from 'vue' // 导入Vue用于生成Vue实例
import Hello from '@/components/Hello' // 导入组件
// 测试脚本里面应该包括一个或多个describe块，称为测试套件（test suite）
describe('Hello.vue', () => {
  // 每个describe块应该包括一个或多个it块，称为测试用例（test case）
  it('should render correct contents', () => {
    const Constructor = Vue.extend(Hello) // 获得Hello组件实例
    const vm = new Constructor().$mount() // 将组件挂在到DOM上
    //断言：DOM中class为hello的元素中的h1元素的文本内容为Welcome to Your Vue.js App
    expect(vm.$el.querySelector('.hello h1').textContent)
      .to.equal('Welcome to Your Vue.js App')
  })
})
