package com.github.pig.demo.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("('ROLE_ADMIN22')")
public @interface HasAdminRole {
}
