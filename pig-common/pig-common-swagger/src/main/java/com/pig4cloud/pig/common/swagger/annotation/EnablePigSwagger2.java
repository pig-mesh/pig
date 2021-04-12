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

package com.pig4cloud.pig.common.swagger.annotation;

import com.pig4cloud.pig.common.swagger.config.GatewaySwaggerAutoConfiguration;
import com.pig4cloud.pig.common.swagger.config.SwaggerAutoConfiguration;
import com.pig4cloud.pig.common.swagger.support.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.*;

/**
 * @author lengleng
 * @date 2020/10/2 开启pig swagger
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
@Import({ SwaggerAutoConfiguration.class, GatewaySwaggerAutoConfiguration.class })
public @interface EnablePigSwagger2 {

}
