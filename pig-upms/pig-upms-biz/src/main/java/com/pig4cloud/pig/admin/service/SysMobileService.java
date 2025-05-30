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

package com.pig4cloud.pig.admin.service;

import com.pig4cloud.pig.common.core.util.R;

/**
 * 系统手机服务接口：提供手机验证码发送功能
 *
 * @author lengleng
 * @date 2025/05/30
 */
public interface SysMobileService {

	/**
	 * 发送手机验证码
	 * @param mobile 手机号码
	 * @return 发送结果，成功返回true，失败返回false
	 */
	R<Boolean> sendSmsCode(String mobile);

}
