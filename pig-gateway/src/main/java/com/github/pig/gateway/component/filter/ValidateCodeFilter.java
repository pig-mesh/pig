package com.github.pig.gateway.component.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.R;
import com.github.pig.common.util.exception.ValidateCodeException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lengleng
 * @date 2017-12-18
 * 验证码校验，true开启，false关闭校验
 * 更细化可以 clientId 进行区分
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ValidateCodeFilter.class);

    private static final String EXPIRED_CAPTCHA_ERROR = "验证码已过期，请重新获取";

    @Value("${security.validate.code:true}")
    private boolean isValidate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isValidate && (StringUtils.contains(request.getRequestURI(), SecurityConstants.OAUTH_TOKEN_URL)
                || StringUtils.contains(request.getRequestURI(), SecurityConstants.MOBILE_TOKEN_URL))) {
            PrintWriter printWriter = null;
            try {
                checkCode(request, response, filterChain);
            } catch (ValidateCodeException e) {
                logger.info("登录失败：{}", e.getMessage());
                response.setCharacterEncoding(CommonConstant.UTF8);
                response.setContentType(CommonConstant.CONTENT_TYPE);
                R<String> result = new R<>(e);
                response.setStatus(478);
                printWriter = response.getWriter();
                printWriter.append(objectMapper.writeValueAsString(result));
            } finally {
                IOUtils.closeQuietly(printWriter);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void checkCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        String code = httpServletRequest.getParameter("code");
        if (StringUtils.isBlank(code)) {
            throw new ValidateCodeException("请输入验证码");
        }

        String randomStr = httpServletRequest.getParameter("randomStr");
        if (StringUtils.isBlank(randomStr)) {
            randomStr = httpServletRequest.getParameter("mobile");
        }

        String key = SecurityConstants.DEFAULT_CODE_KEY + randomStr;
        if (!redisTemplate.hasKey(key)) {
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        Object codeObj = redisTemplate.opsForValue().get(key);

        if (codeObj == null) {
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        String saveCode = codeObj.toString();
        if (StringUtils.isBlank(saveCode)) {
            redisTemplate.delete(key);
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        if (!StringUtils.equals(saveCode, code)) {
            redisTemplate.delete(key);
            throw new ValidateCodeException("验证码错误，请重新输入");
        }

        redisTemplate.delete(key);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
