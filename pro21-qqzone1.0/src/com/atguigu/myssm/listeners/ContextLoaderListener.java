package com.atguigu.myssm.listeners;

import com.atguigu.myssm.ioc.BeanFactory;
import com.atguigu.myssm.ioc.impl.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 1. 获取ServletContext 对象
        ServletContext applicationContext = servletContextEvent.getServletContext();
        // 2. 获取上下文的初始化参数
        String path = applicationContext.getInitParameter("contextConfigLocation");
        // 3. 构造 beanFactory
        BeanFactory beanFactory = new ClassPathXmlApplicationContext(path);
        // 4. 将 beanFactory 保存到application 作用域
        applicationContext.setAttribute("beanFactory", beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
