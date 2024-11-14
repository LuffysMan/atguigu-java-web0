package com.atguigu.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

//@WebFilter("*.do")
public class Demo01Filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 入口拦截
        System.out.println("input filtering");
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        // 出口拦截
        System.out.println("output filtering");
    }

    @Override
    public void destroy() {

    }
}
