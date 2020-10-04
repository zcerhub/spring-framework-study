# spring

spring是一个java轻量级的IOC容器，实现了AOP特性，非侵入性框架。提供了对持久层、事务、web层等各个方面组件集成与一致性封装。涉及到的组件非常丰富，其核心仍然是spring framework。spring framework真正的核心组件只有几个，下面是spring框架的总体架构图：

![image-20201002202723094](D:\code\spring-framework-study\spring-advance-apply\study-record\spring高级特性和应用\img\image-20201002202723094.png)

核心组件只有三个：Core、Context和Beans。它们构建起了整个Spring的骨骼结构。没有它们就不能有AOP、Web等上层的特性功能。

# Spring的设计理念

**spring的设计理念：构建一个数据结构，然后根据整个数据结构设计它的生存环境。**

**spring的三个核心组件中核心是Beans组件，**Bean则是spring构建的数据结构。

- 在spring中bean才是真正的主角，spring是面向Bean的编程
- Bean在spring中的作用就像是Object对OOP的作用一样
- 通过IOC容器完成依赖注入机制，构建Bean生存环境
- IOC容器就是被Bean包裹的对象，Spring正是通过把对象包装在Bean中，从而达到对这些对象的管理以及一些列额外的操作的目的

达到spring框架设计的目标：依赖注入机制，把对象之间的依赖关系用配置文件或注解来管理。

# 核心组件的协同工作

Bean是spring中关键因素，那Context和Core又有何作用？

把Bean比作一场演出中的演员，那Context就是这场演出的舞台背景，Core即使演出的道具。

![image-20201002203406425](D:\code\spring-framework-study\spring-advance-apply\study-record\spring高级特性和应用\img\image-20201002203406425.png)

Context组件解决Bean的生存环境问题

Context发现每个Bean之间的关系，为它们建立维护好Bean关系

Context是一个Bean关系的集合，这个关系集合又叫做IOC容器，一旦建立起这个IOC容器后Spring就可以工作了。

Core组件就是发现、建立和维护Bean关系需要的一系列工具，从这个角度看来，Core组件叫Util更容易理解。

## spring中设计模式的应用

## spring 的代理模式

spring  aop中的CGLIB、JDK动态代理就是利用代理模式设计来实现的，来看看JDK动态代理的例子

![image-20201002203752808](D:\code\spring-framework-study\spring-advance-apply\study-record\spring高级特性和应用\img\image-20201002203752808.png)

spring除了实现被代理对象的接口，还有springProxy和Advised两个接口。

$Proxy就是创建的代理对象，而subject是抽象主题，代理对象时通过InvocationHandler来持有对目标对象的引用的。

spring中一个真实的代理对象结构如下：

![image-20201002204031331](D:\code\spring-framework-study\spring-advance-apply\study-record\spring高级特性和应用\img\image-20201002204031331.png)

![image-20201002204037963](D:\code\spring-framework-study\spring-advance-apply\study-record\spring高级特性和应用\img\image-20201002204037963.png)

## spring中的策略模式

spring中代理对象的创建就是通过策略模式来实现的

spring中的代理方式有两个，JDK动态代理和CGLIB代理，两个代理方式都使用了策略模式。结构如图：

![image-20201002204415526](D:\code\spring-framework-study\spring-advance-apply\study-record\spring高级特性和应用\img\image-20201002204415526.png)

AopProxy接口表示抽象策略

- Cglib2AopProxy和JDKDynamicAopProxy分别代表两种策略的实现方式
- ProxyFactoryBean就是代表Context角色，它根据条件选择使用Jdk代理方式还是CGLIB方式
- 另外三个类主要是来负责创建具体策略对象
- ProxyFactoryBean通过依赖对象关联具体策略对象，通过调用策略对象getProxy(ClassLoader classLoader)方法来完成操作

## 特性应用

## 事件驱动编程

事件驱动编程，基于发布-订阅模式的编程模型，即观察者模式。

事件驱动模型的核心组件通常包含以下几个：

- 事件源：负责产生事件的对象。比如我们常见的按钮，按钮是一个事件源，能够产生“点击”这个事件
- 事件监听器（事件处理器）：负责处理事件的对象
- 事件：或者称为事件对象，是事件源和事件监听器之间的信息桥梁。是整个事件驱动的核心

![image-20201002205247857](D:\code\spring-framework-study\spring-advance-apply\study-record\spring高级特性和应用\img\image-20201002205247857.png)

事件驱动模型的实现包含以下几种：

- 观察者模式
- JDK观察者模式
- JavaBean事件驱动
- Spring事件驱动

# spring事件驱动编程

spring事件驱动模型原理比较复杂，涉及到的类比较多，从代码示例入手。

示例：当一个订单的支付状态发生变化时，能够通知到邮件服务、短信服务、库存服务

![image-20201002205505786](D:\code\spring-framework-study\spring-advance-apply\study-record\spring高级特性和应用\img\image-20201002205505786.png)

涉及类：

ApplicationEvent

ApplicationListener<E>

SmartApplicationListener

ApplicationContext

## 异步执行

spring有2种异步执行方式：全局异步、注解式配置异步

全局异步的操作步骤：

- 定义并配置Evecutor Bean
- 配置名为applicationEventMulticaster的SimpleApplicationEventMulticaster Bean
- 设置applicationEventMulticaster执行器第一步的Evecutor



@Async注解的使用操作执行步骤：

- 开启异步执行：@EnableAsync
- 配置线程池，非必须，没有则用默认线程池
- Bean方法指定为异步：@Async



异步执行原理本质是AOP，具体步骤：

- 初始化线程池和异常处理器
- 创建异步方法所在Bean后，执行Async对应的BeanPostProcessor，创建AOP代理类，代理对象替换原来的对象
- 代理对象中，异步方法被动态植入了异步执行方法
- 执行异步方法，其实执行的是代理对象里面的方法，从而实现异步，除了@Async这个注解，没有任何侵入



## 定时任务

springTask

- fixedRate，上一次开始执行时间点之后再执行
- fixedDelay，上一次执行完毕时间点之后再执行
- initialDelay，第一次延迟后执行，之后按上面指定的规则执行
- 默认是，上一次执行完毕时间后执行下一轮

spring集成Quartz



Cron表达式，一个cron表达式有至少6个（也可能7个）有空格分隔的时间元素。按照顺序依次是：

1. 秒（0~59）
2. 分钟（0~59）
3. 3 小时（0~23）
4. 4 天（0~31）
5. 5 月（0~11）
6. 6 星期（1~7 1=SUN或SUN，MON，TUE，WED，THU，FRI，SAT）
7. 年份 （1970-2099）

# AOP应用重点剖析

## AOP

AOP（Aspect Orient Programming），作为面对对象编程的一种补充，广泛应用于处理一些具备横切性质的系统级服务，比如事务管理、安全检查、缓存、对象池管理等。

- AOP的实现的关键在于AOP框架自动创建AOP代理，代理分为静态代理和动态代理
- 动态代理在运行时在内存中临时产生动态代理类，达到运行时增强



AOP由切面、切点、连接点、目标对象、回调五个元素构成

- aspect：切面，通俗的讲可以理解为一个功能，比如具备某项能力（如：帮助他们是一种能力），定义为一个切面；
- pointcut：切点，可以理解为一种匹配规则，比如哪些人需要被帮助，通过一些规则进行分组筛选；
- Target Object：目标对象，比如某种能力需要对某个人使用，这个某个人就是目标对象；
- jointpoint：连接点，具体的需要做的事情，可以理解为需要使用某项能力帮助某人做什么事情的时候提供帮助；这个做什么的事情就是连接点
- Advice：回调的通知，比如：在什么时间点去帮助他们，在什么时间点提供某种能力帮助别人；

## AOP在spring框架中的应用

**spring中6种AOP增强方式**

- Before前置增强
- After后置增强
- Around环绕增强
- AfterReturning最终增强
- AfterThrowing异常增强
- DeclareParents引入增强



AOP在spring框架中应用非常广泛，比如最常见的事务。

- 在方法前开启事务
- 方法后提交事务
- 检测到有异常时，回滚事务

## 通知的执行顺序

```java
package com.example.springadvanceapply.aop;

/*
* 织入增强的方式触发先后顺序：
* BeforeAround --》 Before --》 ProcessAround --》 After --》 AfterReturning --》 AfterThrowing
*
*
* 用代码描述
* 前置增强
* try{
*       //TODO 业务方法
*        //最终增强
* }catch(Exception e){
*       e.printStackTrace();
* // 异常增强
* }finnaly{
*   //后置增强
*}
*
* */

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class CustomerAspect {

    /*
     * 前置增强
     * */
    @Before("execution(public * com.example.springadvanceapply.aop.*.*(..))")
    public void before(JoinPoint joinpoint) {
        Map params = AopHelper.getMethodParams(joinpoint);
        params.forEach((key, value) -> {
            if (Objects.isNull(value)) {
                throw new RuntimeException("参数" + key + "不能为空");
            }
        });
        System.out.println("【前置增强】" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
    }

    /*
     * 后置增强
     * */
    @After("target(com.example.springadvanceapply.aop.CustomerServiceImpl)")
    public void after(JoinPoint joinpoint) {
        Map params = AopHelper.getMethodParams(joinpoint);
        params.forEach((key, value) -> {
            if (Objects.isNull(value)) {
                throw new RuntimeException("参数" + key + "不能为空");
            }
        });
        System.out.println("【后置增强】" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
    }

    /*
     * 环绕增强，比前置增强更加强大，能够通过procceed调用被代理对象方法获取执行结果
     * */
    @Around("@annotation(com.example.springadvanceapply.aop.AroundTag)")
    public Object around(ProceedingJoinPoint joinpoint) throws Throwable {
        System.out.println("【进入环绕增强】执行代理方法前" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
        long s = System.currentTimeMillis();
        Object result = null;
        result = joinpoint.proceed();
        System.out.println("【进入环绕增强】执行代理方法后" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName() + ",总共用时：" + (System.currentTimeMillis() - s));
        return result;
    }

    /*
     * 最终增强。和后置增强类似，在业务方法执行后执行
     * 区别：
     * 1.执行顺序
     * */
    @AfterReturning("execution(public * com.example.springadvanceapply.aop.*.*(..))")
    public void afterReturning(JoinPoint joinpoint) {
        Map params = AopHelper.getMethodParams(joinpoint);
        params.forEach((key, value) -> {
            if (Objects.isNull(value)) {
                throw new RuntimeException("参数" + key + "不能为空");
            }
        });
        System.out.println("【afterReturning增强】" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
    }

    /*
     * 异常增强
     * */
    @AfterThrowing(value = "execution(public * com.example.springadvanceapply.aop.*.*(..))", throwing = "e")
    public void afterThrowing(JoinPoint joinpoint, Exception e) {
        Map params = AopHelper.getMethodParams(joinpoint);
        params.forEach((key, value) -> {
            if (Objects.isNull(value)) {
                throw new RuntimeException("参数" + key + "不能为空");
            }
        });
        System.out.println("【AfterThrowing增强】" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName() + "\n异常信息为：" + e.getMessage());
    }

    /*
     * 引入增强，在不修改代码的情况下，将一个已经代理的类，引入新的方法
     * 这里将CustomerServiceImpl引入GoodsService的功能
     * “+”表示CustomService的所有子类；defaultImpl表示默认需要添加的新的类
     * */
    @DeclareParents(value = "com.example.springadvanceapply.aop.CustomerServiceImpl", defaultImpl = GoodsServiceImpl.class)
    public GoodsService goodsService;
}
```

### 没有抛出异常的情况下

```
【进入环绕增强】执行代理方法前com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress
【前置增强】com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress
调用成功addAddress，当前请求参数customerId=12，userName=hash，address=整个世界都是我的
【afterReturning增强】com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress
【后置增强】com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress
【进入环绕增强】执行代理方法后com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress,总共用时：9
```

### 抛出异常的情况下

```
【进入环绕增强】执行代理方法前com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress
【前置增强】com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress
调用成功addAddress，当前请求参数customerId=12，userName=hash，address=整个世界都是我的
【AfterThrowing增强】com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress
异常信息为：/ by zero
【后置增强】com.example.springadvanceapply.aop.CustomerServiceImpl.addAddress
```

### 引入增强

在不改变目标类的情况下给代理类增加新的方法

