package com.atguigu.myssm.myspringmvc;

import com.atguigu.myssm.ioc.BeanFactory;
import com.atguigu.myssm.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 1. 请求参数处理
 * 2. invoke
 * 视图处理
 */

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    private BeanFactory beanFactory = null;

    public DispatcherServlet() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext applicationContext = getServletContext();
        Object beanFactoryObj = applicationContext.getAttribute("beanFactory");
        if (beanFactoryObj != null) {
            beanFactory = (BeanFactory) beanFactoryObj;
        } else {
            throw new RuntimeException("failed to obtain IOC container");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // 设置编码
//        req.setCharacterEncoding("UTF-8");
        // 假设url是: http://localhost:8080/pro15/hello.do
        // 那么servletPath是: /hello.do
        // 我的思路是:
        // 第一步: /hello.do -> hello
        // -> HelloController
        String servletPath = req.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);

        Object beanObj = beanFactory.getBean(servletPath);
        if (beanObj == null) {
            System.out.println("servletPath not found: " + servletPath);
            throw new RuntimeException("illegal servlet path!");
        }

        String operate = req.getParameter("operate");
        if (StringUtil.isEmpty(operate)) {
            operate = "index";
        }

        try {
            Method[] methods = beanObj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (operate.equals(method.getName())) {
                    // 1. 统一获取请求参数
                    // get parameter array of current method
                    Parameter[] parameters = method.getParameters();
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();
                        if ("req".equals(parameterName)) {
                            parameterValues[i] = req;
                        } else if ("resp".equals(parameterName)) {
                            parameterValues[i] = resp;
                        } else if ("session".equals(parameterName)) {
                            parameterValues[i] = req.getSession();
                        } else {
                            String parameterValue = req.getParameter(parameterName);
                            String typeName = parameter.getType().getName();

                            Object parameterObj = parameterValue;
                            if (parameterObj != null) {
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }
                            parameterValues[i] = parameterObj;
                        }
                    }

                    // 2. invoke method of controllers
                    method.setAccessible(true);
                    Object returnObj = method.invoke(beanObj, parameterValues);
                    // 3. process view
                    String methodReturnStr = (String) returnObj;
                    if (methodReturnStr.startsWith("redirect:")) {
                        String redirectStr = methodReturnStr.substring("redirect:".length());
                        resp.sendRedirect(redirectStr);
                    } else {
                        super.processTemplate(methodReturnStr, req, resp);
                    }
                }
            }
//            else {
//                throw new RuntimeException("operate值非法!");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DispatcherServletException("Dispatcher Servlet ERROR");
        }
    }
}
