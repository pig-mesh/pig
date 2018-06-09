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

package com.github.pig.common.util;

import com.github.pig.common.constant.CommonConstant;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Optional;

/**
 * @author lengleng
 * @date 2017/12/22
 */
public class UserUtilsTest {
    @Test
    public void getToken() throws Exception {
        String authorization = null;
        System.out.println(StringUtils.substringAfter(authorization, CommonConstant.TOKEN_SPLIT));
    }

    @Test
    public void optionalTest() {
        Optional<String> optional = Optional.ofNullable("");
        System.out.println(optional.isPresent());
    }

}