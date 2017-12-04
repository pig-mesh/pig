package com.github.pig.common.filter;

import com.github.pig.common.util.UserUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lengleng
 * @date 2017/12/4
 */
public class UserFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String username = UserUtils.getUserName((HttpServletRequest) request);
        if (StringUtils.isNotEmpty(username)) {
            UserUtils.setUser(username);
        }
        chain.doFilter(request, response);
        if (StringUtils.isNotEmpty(username)) {
            UserUtils.clearAllUserInfo();
        }
    }

    @Override
    public void destroy() {

    }
}
