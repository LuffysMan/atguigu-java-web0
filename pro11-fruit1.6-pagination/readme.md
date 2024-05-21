review:
# 1. 设置编码
1. post提交方式下的设置编码, 防止中文乱码
   1. request.setCharacterEncoding("utf-8")
2. get提交方式, tomcat8开始, 编码不需要设置
   ```
   // tomcat8之前, get方式设置比较麻烦
   String fname = request.getParameter("fname");
   byte[] bytes = fname.getBytes("iso-8859-1");
   fname = new String(bytes, "UTF-8");
   ```

# 2. Servlet继承关系以及生命周期
1. Servlet接口: init(), service(), destroy()
   2. GenericServlet抽象子类: 实现了init()和destroy(), 留下了abstract service()
   3. HttpServlet抽象子类: 实现了service方法, 在service方法内部通过request.getMethod()来判断请求的方式, 然后根据请求的方式去调用内部的do方法, 每一个do方法进行了简单实现, 主要是如果请求方式不符合, 则报405错误.目的是让我们的Servlet子类去重写对应的方法(如果重写的不对, 则使用父类的405错误实现)
2. 生命周期: 实例化, 初始化, 服务, 销毁
   1. Tomcat负责维护Servlet实例的生命周期
   2. 每个Servlet在Tomcat容器中只有一个实例, 它是线程不安全的
   3. Servlet的启动时机: <load-on-startup>
   4. Servlet3.0开始支持注解: @WebServlet

# 3. HTTP协议
1. 由Request和Response两部分组成.
2. 请求包含了三部分
   1. 请求行
   2. 请求消息头: 浏览器版本信息等
   3. 请求主体: 
      1. GET: query string
      2. POST: form data/request payload
3. 响应包含了三部分: 响应行, 响应消息头, 响应主体

# 4. HttpSession
1. HttpSession: 表示 会话
2. 为什么需要HttpSession, 原因是因为Http协议是无状态的
   1. session id
   2. cookie

# 5. 会话
## Session 保存作用域
- 一次会话有效
- session保存作用域是和具体的某一个session对应的
- 常用API
  - void session.setAttribute(k, v)
  - object session.getAttribute(k)
- 其他API
  - session.getId()
  - session.isNew()
  - session.getCreationTime()
  - session.invalidate()

# 6. 服务器内部转发以及客户端重定向
1. 服务器内部转发: request.getRequestDispatcher("index.html").forward(req, resp)
   - 一次请求响应的过程, 客户端不感知内部经过了多少次转发
   - 地址栏无变化
2. 客户端重定向: response.sendRedirect("index.html")
   - 两次请求响应的过程. 客户端肯定知道请求URL有变化
   - 地址栏有变化

# 7. Thymeleaf - 视图模板技术
1. 添加thymeleaf 的jar包
2. 新建一个Servlet类:ViewBaseServlet. 目的是为了它的两个方法: 创建模板引擎, 处理模板
3. 在web.xml文件中添加配置<context-param>
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

# 今日课程: 保存作用域
原始情况下, 保存作用域我们可以认为有四个
1. page(页面级别, 现在几乎不用)
2. request(一次请求响应范围)
   1. demo01(servlet1): request.setAttribute(k, v) + response.sendRedirect("demo02) 
   2. demo02(servlet2): v = request.getAttribute(k), System.out.println(v);
3. session(一次会话范围)
4. application(一次应用程序范围有效)

# 路径问题
## 相对路径

## 绝对路径
