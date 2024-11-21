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
2. 事务管理:
   1. 引入事务管理的原因: 假设一个service有3个DAO操作, 那么, 如果把事务提交和回滚放到DAO层, 可能会出现部分提交, 部分由于失败, 回滚的情况.  那么这个service的表现就不一致了.
   2. 涉及到的组件
      - OpenSessionInViewFilter: 过滤器, 一旦在Servlet中发生sql异常, 就捕获并执行事务回滚.
      - ThreadLocal: 线程本地变量,  解决3个DAO需要使用同一个connection才能统一事务管理的问题
   3. ThreadLocal: 本地线程, 我们可以通过 set 方法在当前线程上存储数据, 通过get方法在当前线程获取数据
      - set(obj) 方法源码分析:
        ```code
        public void set(T value) {
             Thread t = Thread.currentThread();    // 获取当前线程
             ThreadLocalMap map = getMap(t);       // 每一个线程都维护各自的一个容器(ThreadLocalMap)
             if (map != null) {
                 map.set(this, value);             // 这里的this对应的是ThreadLocal对象本身, 因为一个线程可以有多个ThreadLocal
             } else {
                 createMap(t, value);              // 默认情况下map是没有初始化的, 第一次调用 set 方法, 会初始化
             }
        }
        ```
      - get 方法源码分析
        ```code
        public T get() {
            Thread t = Thread.currentThread();
            ThreadLocalMap map = getMap(t);
            if (map != null) {
                ThreadLocalMap.Entry e = map.getEntry(this);
                if (e != null) {
                    @SuppressWarnings("unchecked")
                    T result = (T)e.value;
                    return result;
                }
            }
            return setInitialValue();
        }
        ```
   4. 监听器
      1. ServletContextListener: 监听 ServletContext 对象的创建和销毁的过程
      2. HttpSessionListener: 监听 HttpSession 对象的创建和销毁的过程
      3. ServletRequestListener: 监听 Request 对象的创建和销毁的过程
      4. ServletContextAttributeListener: 监听 ServletContext 的保存作用域的改动(add, remove, replace)
      5. HttpSessionAttributeListener: 监听 HttpSession 的保存作用域的改动(add, remove, replace)
      6. ServletRequestAttributeListener: 监听 ServletRequest 的保存作用域的改动(add, remove, replace)
      7. HttpSessionBindingListener: 监听某个对象在 session 域中的创建与移除
      7. HttpSessionActivationListener: 监听某个对象在 session 域中的序列化和反序列化
