/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.admin.api.vo;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@Data
public class ImageCode implements Serializable {

	private String code;

	private LocalDateTime expireTime;

	private BufferedImage image;

	public ImageCode(BufferedImage image, String sRand, int defaultImageExpire) {
		this.image = image;
		this.code = sRand;
		this.expireTime = LocalDateTime.now().plusSeconds(defaultImageExpire);
	}

}
