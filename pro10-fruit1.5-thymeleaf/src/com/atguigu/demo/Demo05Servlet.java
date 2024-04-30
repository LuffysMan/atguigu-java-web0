package com.atguigu.demo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/demo05")
public class Demo05Servlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 向application保存作用域保存数据
        ServletContext application = req.getServletContext();
        application.setAttribute("k", "demo05-->demo06");
        // 客户端重定向
        resp.sendRedirect("demo06");
        //服务器端转发
//        req.getRequestDispatcher("demo06").forward(req, resp);
    }
}
