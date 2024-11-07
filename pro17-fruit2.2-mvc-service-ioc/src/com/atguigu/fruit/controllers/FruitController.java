package com.atguigu.fruit.controllers;

import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.fruit.service.FruitService;
import com.atguigu.myssm.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FruitController {
    //The init method of ViewBaseServlet always be called cause FruitServlet is a Servlet before we modify it.
    private final FruitService fruitService = null;

    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {
        // 3. 执行更新
        fruitService.updateFruit(new Fruit(fid, fname, price, fcount, remark));

        // 4. 资源跳转
        return "redirect:fruit.do";
    }

    private String edit(Integer fid, HttpServletRequest req) {
        if (fid != null) {
            Fruit fruit = fruitService.getFruitByFid(fid);
            req.setAttribute("fruit", fruit);
            return "edit";
        }
        return "error";
    }

    private String index(String operate, String keyword, Integer pageNo, HttpServletRequest req) {
        if (pageNo == null) {
            pageNo = 1;
        }

        HttpSession session = req.getSession();
        session.setAttribute("pageNo", pageNo);

        if (StringUtil.isNotEmpty(operate) && "search".equals(operate)) {
            pageNo = 1;
            if (StringUtil.isEmpty(keyword)) {
                keyword = "";
            }
            session.setAttribute("keyword", keyword);
        } else {
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null) {
                keyword = (String) keywordObj;
            } else {
                keyword = "";
            }

        }

        List<Fruit> fruitList = fruitService.getFruitList(keyword, pageNo);
        session.setAttribute("fruitList", fruitList);

        //总页数
        int pageCount = fruitService.getPageCount(keyword);

        session.setAttribute("pageCount", pageCount);
        return "index";
    }

    private String search(String operate, String keyword, Integer pageNo, HttpServletRequest req) {
        return this.index(operate, keyword, pageNo, req);
    }

    private String add(String fname, Integer price, Integer fcount, String remark) {
        Fruit fruit = new Fruit(0, fname, price, fcount, remark);
        fruitService.addFruit(fruit);
        return "redirect:fruit.do";
    }

    private String del(Integer fid) {
        if (fid != null) {
            fruitService.delFruit(fid);
            return "redirect:fruit.do";
        }
        return "error";
    }
}
