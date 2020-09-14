package com.pig4cloud.pig.common.job.annotation;

import com.pig4cloud.pig.common.job.properties.XxlJobProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 激活xxl-job配置
 *
 * @author lishangbu
 * @date 2020/9/14
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ XxlJobProperties.class })
public @interface EnablePigXxlJob {

}
