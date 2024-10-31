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
2. 学习servlet中的ServletContext和<context-param>
3. 什么是业务层
4. IOC
5. 过滤器: Filter
6. 事务管理
7. TransactionManager, ThreadLocal, OpenSessionInViewFilter
