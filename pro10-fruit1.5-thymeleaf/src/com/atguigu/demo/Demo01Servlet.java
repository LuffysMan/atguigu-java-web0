package com.atguigu.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/demo01")
public class Demo01Servlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 向 request 保存作用域保存数据
        req.setAttribute("k", "demo01-demo02");
        // 客户端重定向
//        resp.sendRedirect("demo02");
        // 服务端内部转发
        req.getRequestDispatcher("demo02").forward(req, resp);
    }
}
