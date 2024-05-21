package com.atguigu.fruit.servlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {
    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 设置编码
        req.setCharacterEncoding("utf-8");

        // 2. 设置参数
        int fid = Integer.parseInt(req.getParameter("fid"));
        String fname = req.getParameter("fname");
        int price = Integer.parseInt(req.getParameter("price"));
        int fcount = Integer.parseInt(req.getParameter("fcount"));
        String remark = req.getParameter("remark");

        // 3. 执行更新
        fruitDAO.updateFruit(new Fruit(fid, fname, price, fcount, remark));

        // 4. 资源跳转
        // super.processTemplate("index", req, resp);
        // req.getRequestDispatcher("index.html").forward(req, resp);
        // 此处需要重定向, 目的是重定向给IndexServlet发请求, 重新获取fruitList, 然后覆盖到session中, 这样
        // index.html页面上显示的数据才是最新的.
        resp.sendRedirect("index");
    }
}
