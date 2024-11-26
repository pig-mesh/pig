package com.pig4cloud.pigx.bi.config;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * @author lengleng
 * @date 2024/11/26
 * <p>
 * 删除 积木报表的 header 避免 iframe 嵌套 , 造成页面显示异常
 */
@Component
public class HtmlEnhanceFilter extends OncePerRequestFilter {

    @Value("classpath:/templates/enhance.ftl")
    private Resource resource;


    /**
     * do 过滤器内部
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain Filter Chain （筛选链）
     * @throws ServletException Servlet 异常
     * @throws IOException      io异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 不是首页直接跳过
        if (!request.getRequestURI().equals("/jmreport/list")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 包装响应对象
        EditedResponseWrapper responseWrapper = new EditedResponseWrapper(response);

        // 执行链并捕获响应内容
        filterChain.doFilter(request, responseWrapper);

        // 获取原始 HTML 内容
        String originalHtml = responseWrapper.toString();

        // 在 HTML 内容中插入自定义 JS
        String enhancedHtml = originalHtml.replace("</body>", IoUtil.read(resource.getInputStream(), Charset.defaultCharset()) + "</body>");

        // 将增强后的内容写回响应
        response.getWriter().write(enhancedHtml);
    }


    /**
     * 可编辑响应流包装器
     *
     * @author lengleng
     * @date 2024/11/26
     */
    static class EditedResponseWrapper extends HttpServletResponseWrapper {

        private CharArrayWriter charArrayWriter = new CharArrayWriter();

        public EditedResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() {
            return new PrintWriter(charArrayWriter);
        }

        @Override
        public String toString() {
            return charArrayWriter.toString();
        }
    }

}
