/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Job Security Validator
 * <p>
 * Validates job configurations to prevent Remote Code Execution (RCE)
 * vulnerabilities
 * by blocking dangerous classes and methods that could be exploited via
 * reflection.
 *
 * @author Security Team
 * @date 2025-01-22
 */
@Slf4j
@Component
public class JobSecurityValidator {

    /**
     * Dangerous classes that should never be allowed for reflection-based job
     * execution
     * These classes can be exploited to execute arbitrary system commands
     */
    private static final Set<String> DANGEROUS_CLASSES = new HashSet<>(Arrays.asList(
            // Expression Language classes - can evaluate arbitrary code
            "jakarta.el.ELProcessor",
            "javax.el.ELProcessor",
            "org.apache.el.ExpressionFactoryImpl",

            // Runtime execution classes
            "java.lang.Runtime",
            "java.lang.ProcessBuilder",
            "java.lang.ProcessImpl",

            // Script engines - can execute arbitrary scripts
            "javax.script.ScriptEngine",
            "javax.script.ScriptEngineManager",

            // Class loading - can load malicious classes
            "java.net.URLClassLoader",
            "java.lang.ClassLoader",

            // File operations - can be used for data exfiltration
            "java.io.FileOutputStream",
            "java.io.FileWriter",
            "java.nio.file.Files",

            // Network operations - can be used for reverse shells
            "java.net.Socket",
            "java.net.ServerSocket",
            "java.net.URLConnection",

            // Reflection utilities that bypass security
            "java.lang.reflect.Constructor",
            "java.lang.reflect.Proxy",
            "sun.misc.Unsafe"));

    /**
     * Dangerous package prefixes that should be blocked
     */
    private static final Set<String> DANGEROUS_PACKAGE_PREFIXES = new HashSet<>(Arrays.asList(
            "sun.misc.",
            "com.sun.jndi.",
            "javax.naming.",
            "org.apache.commons.collections.functors.",
            "org.apache.xalan.xsltc.trax.",
            "javassist.",
            "org.codehaus.groovy.",
            "ognl.",
            "freemarker.template."));

    /**
     * Dangerous method names that should never be invoked
     */
    private static final Set<String> DANGEROUS_METHODS = new HashSet<>(Arrays.asList(
            "eval",
            "exec",
            "execute",
            "loadClass",
            "defineClass",
            "getRuntime",
            "getConstructor",
            "newInstance",
            "invoke",
            "forName",
            "getMethod",
            "getDeclaredMethod"));

    /**
     * Patterns for detecting dangerous method parameter values
     */
    private static final List<Pattern> DANGEROUS_PARAM_PATTERNS = Arrays.asList(
            Pattern.compile(".*Runtime.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*ProcessBuilder.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\.exec\\(.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*ScriptEngine.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\.getClass\\(\\).*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\.forName\\(.*", Pattern.CASE_INSENSITIVE));

    @Value("${spring.quartz.security.enabled:true}")
    private boolean securityEnabled;

    /**
     * Validates if a class name is safe to use in reflection-based job execution
     *
     * @param className The fully qualified class name to validate
     * @return true if the class is safe to use, false otherwise
     */
    public boolean isClassSafe(String className) {
        if (!securityEnabled) {
            log.warn("Quartz security validation is DISABLED. This is a security risk!");
            return true;
        }

        if (StrUtil.isBlank(className)) {
            return false;
        }

        // Check exact match against dangerous classes
        if (DANGEROUS_CLASSES.contains(className)) {
            log.error("Security violation: Attempt to use dangerous class: {}", className);
            return false;
        }

        // Check dangerous package prefixes
        for (String prefix : DANGEROUS_PACKAGE_PREFIXES) {
            if (className.startsWith(prefix)) {
                log.error("Security violation: Attempt to use class from dangerous package: {}", className);
                return false;
            }
        }

        return true;
    }

    /**
     * Validates if a method name is safe to invoke
     *
     * @param methodName The method name to validate
     * @return true if the method is safe to invoke, false otherwise
     */
    public boolean isMethodSafe(String methodName) {
        if (!securityEnabled) {
            return true;
        }

        if (StrUtil.isBlank(methodName)) {
            return false;
        }

        if (DANGEROUS_METHODS.contains(methodName.toLowerCase())) {
            log.error("Security violation: Attempt to invoke dangerous method: {}", methodName);
            return false;
        }

        return true;
    }

    /**
     * Validates if method parameters are safe
     *
     * @param paramValue The parameter value to validate
     * @return true if the parameters are safe, false otherwise
     */
    public boolean isParameterSafe(String paramValue) {
        if (!securityEnabled) {
            return true;
        }

        if (StrUtil.isBlank(paramValue)) {
            return true;
        }

        for (Pattern pattern : DANGEROUS_PARAM_PATTERNS) {
            if (pattern.matcher(paramValue).matches()) {
                log.error("Security violation: Dangerous pattern detected in parameter: {}", paramValue);
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a class belongs to an allowed package in strict mode
     *
     * @param className The class name to check
     * @return true if in allowed package, false otherwise
     */
    private boolean isInAllowedPackage(String className) {
        // In strict mode, only allow application-specific packages
        // Customize this list based on your application's safe job classes
        List<String> allowedPackages = Arrays.asList(
                "com.pig4cloud.pig.daemon.quartz.task.", // Application-specific task package
                "com.pig4cloud.pig.common.job.", // Common job utilities
                "org.springframework.scheduling." // Spring scheduling utilities
        );

        for (String allowedPackage : allowedPackages) {
            if (className.startsWith(allowedPackage)) {
                return true;
            }
        }

        log.warn("Security violation (strict mode): Class {} not in allowed packages", className);
        return false;
    }

    /**
     * Comprehensive validation of a job configuration
     *
     * @param className  Class name to execute
     * @param methodName Method name to invoke
     * @param paramValue Method parameter value
     * @return Validation error message, or null if validation passes
     */
    public String validateJobConfig(String className, String methodName, String paramValue) {
        if (!securityEnabled) {
            return null;
        }

        // Validate class name
        if (!isClassSafe(className)) {
            return "安全检查失败";
        }

        // Validate method name
        if (!isMethodSafe(methodName)) {
            return "安全检查失败";

        }

        // Validate parameters
        if (!isParameterSafe(paramValue)) {
            return "安全检查失败";

        }

        return null;
    }
}
