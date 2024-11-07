# review
到目前为止, 我们的项目的演进过程如下:
1. 最初的做法: 一个请求对应一个servlet, 
   1. 存在问题: servlet太多了
2. 把一系列的请求放到一个servlet中来处理, 通过一个operate值来决定调用FruitServlet中哪一个方法, 使用的是switch-case
   1. 存在问题: 随着业务规模扩大, switch-case的规模也越来越大
3. 根据operate值, 使用反射技术, 获取到对应方法进行调用
   1. 存在问题: 每一个servlet中, 都有类似的反射技术代码.
4. 继续抽取, 设置了DispatcherServlet类. 这个类做了两件事情
   1. 处理配置文件, 生成beanMap: 使用DOM技术, 解析applicationContext.xml, 生成了beanMap容器, 放置了所有的controller对象.
   2. 处理请求url, 找到对应的controller, 调用对应的方法.
      1. 解析 request url, 获取到servletPath: /fruit.do -> fruit
      2. 根据fruit找到对应的组件: FruitController
      3. 根据获取到的 operate 的值, 定位到FruitController中对应的方法
      4. 调用FruitController中的方法
         1. 先获取到方法的参数签名信息: 参数名, 类型
         2. 使用参数名从request对象拿到参数值, 并转换为对应的类型
         3. 执行方法: Object returnObj = method.invoke(this, parameterValues)
         4. 视图处理: redirectStr = (String) returnObj; 
            1. 如果 redirectStr.startsWith("redirect:"), 则调用response.redirect()
            2. 否则调用父类的视图处理模板方法.

# 今日内容
1. 再次学习servlet的初始化方法
   1. Servlet生命周期: 实例化, 初始化, 服务, 销毁
   2. Servlet中初始化方法有两个, init(), init(config)
   3. 如果我们想在Servlet初始化时做一些准备工作, 可以通过重写init()方法来实现
      1. 获取config对象: ServletConfig config = getServletConfig()
      2. 获取初始话参数值: config.getInitParameter(key);
   4. 在web.xml文件中配置servlet
   5. 也可以通过注解的方式进行配置
      ```
      @WebServlet(urlPatterns = {"/demo01"},
      initParams = {
                @WebInitParam(name = "hello", value = "world"),
                @WebInitParam(name = "uname", value = "jim")
      }
      ```

2. 学习servlet中的ServletContext和<context-param>
   1. 获取ServletContext, 有很多方法
      1. 在初始化方法中, 可以通过成员方法getServletContext: ServletContext servletContext = getServletContext();
      2. 在服务方法中:
         1. req.getServletContext();
         2. req.getSession().getServletContext();
      3. 获取初始化值: servletContext.getInitParameter("contextConfigLocation");
3. 什么是业务层
   1. Model1(JSP)和Model2(MVC: Model, View, Controller)
      1. 视图层: 用于做数据展示以及和用户交互的一个界面
      2. 控制层: 接收客户端请求, 具体的业务功能还是需要借助于模型组件来完成
      3. 模型层: 模型分为很多种: 有比较简单的pojo/vo(只有属性的get/set), 有业务模型组件, 有数据访问层组件
         1. pojo/vo: 值对象
         2. DAO: 数据访问对象
         3. BO: 业务对象
   2. 区分业务对象和数据访问对象
      1. DAO中的方法都是单精度或者称之为细粒度方法. 什么叫单精度? 一个方法只考虑一个操作, 比如添加, 那就是insert
      2. BO中的方法属于业务方法, 实际的业务是比较复杂的, 因此业务方法的力度是比较粗的.
         1. 注册这个功能属于业务功能, 也就是说注册这个方法属于业务方法. 那么这个业务方法中包含了多个DAO方法. 也就是说注册这个业务功能需要通过多个DAO方法的组合调用, 从而完成注册功能的实现.
            1. 检查用户名是否已经被注册  - DAO中的select操作
            2. 向用户表新增一条新用户记录 - DAO中的insert操作
            3. ...
   3. 在库存系统中添加业务层组件
4. IOC
   1. 耦合/依赖:
      1. 软件系统的层与层之间是有依赖的
      2. 架构设计的原则: 高内聚, 低耦合
5. 过滤器: Filter
6. 事务管理
7. TransactionManager, ThreadLocal, OpenSessionInViewFilter
