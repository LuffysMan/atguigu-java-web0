# review
1. ServletConfig: Servlet 生命周期中的初始化方法: init(), init(config)
   1. 因此, 如果我们需要在初始化自己的Servlet时做一些自定义的操作, 我们可以重写无参的 init 方法. 可以通过getConfig()获取ServletConfig对象, 可以通过config.getInitParameter()获取初始化参数
2. ServletContext: 可以通过ServletContext获取配置的上下文参数
3. MVC: 
   1. V: view 视图
   2. C: Controller 控制器
   3. M: Model 模型. 模型有很多种类:
      1. DAO: 数据访问模型
      2. BO(Service): 业务逻辑模型
      3. POJO 值对象模型
      4. DTO 数据传输对象
4. IOC: 控制反转 / DI: 依赖注入
   1. 控制反转: 
      1. 最初, 我们在自己的 Controller 中创建了一个成员变量 fruitService, 并进行了初始化. fruitService的控制权在Controller  
      2. 之后, 我们在applicationContext.xml中定义了这个fruitService. 然后创建了beanFactory类, 通过解析XML, 产生fruitService实例, 
      存放在beanMap中. 因此service实例和dao实例的生命周期发生了变化, 控制权从程序员转移到了beanFactory(ssm框架). 这个就是"控制反转"
   2. 依赖注入:
      1. 之前我们在控制层出现代码: FruitService fruitService = new FruitServiceImpl(); 那么, 控制层和service层存在耦合
      2. 之后, 我们将控制层代码修改成: FruitService fruitService = null;  然后在配置文件中描述了控制层和service层的依赖关系:  
      <bean id="fruit" class="com.atguigu.fruit.controllers.FruitController">
         <property name="fruitService" ref="fruitService"/>
      </bean>  
      通过beanFactory解析这个依赖关系, 并装载依赖的service实例到控制层对象

今日内容:
1. 过滤器: Filter
   1. Filter也属于Servlet规范
   2. Filter开发步骤: 新建类实现Filter接口, 然后实现其中的三个方法: init, doFilter, destroy
   3. Filter配置: 可以通过@WebFilter注解, 或者在web.xml中增加 filter 和 filter-mapping 标签
   4. Filter在配置时, 和servlet一样, 也可以配置通配符.
   5. 过滤器链
      1. 注解的方式: 顺序不明(视频教程说是按照全类名字母序), 但实测不是
      2. xml的方式: 按照配置的顺序
2. 事务管理(TransactionManager, ThreadLocal, OpenSessionInViewFilter)
3. 监听器(Listener, ContextLoaderListener)