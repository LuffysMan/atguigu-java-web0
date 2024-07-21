package com.atguigu.myssm.myspringmvc;

import com.atguigu.myssm.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, Object> beanMap = new HashMap<>();

    public DispatcherServlet() {
    }

    public void init(ServletConfig servletConfig) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
        //1. create DocumentBuilderFactory instance
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            //2. create DocumentBuilder instance
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //3. create Document instance
            Document document = documentBuilder.parse(inputStream);
            //4. get all nodes of tag id of "bean"
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Class<?> beanClass = Class.forName(className);
                    Object beanObj = beanClass.getDeclaredConstructor().newInstance();

                    Method setServletContextMethod = beanClass.getDeclaredMethod("setServletContext", ServletContext.class);
                    setServletContextMethod.setAccessible(true);
                    ServletContext servletContext = servletConfig.getServletContext();
                    setServletContextMethod.invoke(beanObj, servletContext);

                    beanMap.put(beanId, beanObj);
                }
            }
        } catch (ParserConfigurationException | ClassNotFoundException | IOException | SAXException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码
        req.setCharacterEncoding("UTF-8");
        // 假设url是: http://localhost:8080/pro15/hello.do
        // 那么servletPath是: /hello.do
        // 我的思路是:
        // 第一步: /hello.do -> hello
        // -> HelloController
        String servletPath = req.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);

        Object controllerBeanObj = beanMap.get(servletPath);
        if (controllerBeanObj == null) {
            System.out.println("servletPath not found: " + servletPath);
            throw new RuntimeException("illegal servlet path!");
        }

        String operate = req.getParameter("operate");
        if (StringUtil.isEmpty(operate)) {
            operate = "index";
        }

        try {
            Method method = controllerBeanObj.getClass().getDeclaredMethod(operate, HttpServletRequest.class, HttpServletResponse.class);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(controllerBeanObj, req, resp);
            } else {
                throw new RuntimeException("operate值非法!");
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
