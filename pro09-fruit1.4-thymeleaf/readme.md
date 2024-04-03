# 5. 会话
## Session 保存作用域
- session保存作用域是和具体的某一个session对应的
- 常用API
  - session.setAttribute(k, v)
  - session.getAttribute(k)

# 6. 服务器内部转发以及客户端重定向
1. 服务器内部转发: request.getRequestDispatcher("...").forward(req, resp)
   - 一次请求响应的过程, 客户端不感知内部经过了多少次转发
   - 地址栏无变化
2. 客户端重定向: response.sendRedirect("...")
   - 两次请求响应的过程. 客户端肯定知道请求URL有变化
   - 地址栏有变化

# 7. Thymeleaf - 视图模板技术
1. 添加thymeleaf 的jar包
2. 新建一个Servlet类:ViewBaseServlet
3. 在web.xml文件中添加配置
     1. 配置前缀 view-prifix
     2. 配置后缀 view-suffix
     3. xx
4. 使得我们的Servlet继承ViewBaseServlet
5. 根据逻辑视图名称 得到 物理视图名称
6. 使用thymeleaf的标签
   1. th:if
   2. th:unless
   3. th:each
   4. th:text
