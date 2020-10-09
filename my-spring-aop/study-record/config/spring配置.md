# 手写spring配置

## 配置分析

**框架为什么提供配置方式**

- 使用简单、改动灵活
- 不需要改动代码

![image-20201008135503509](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008135503509.png)

### 配置方式的工作过程

- 能用什么样的配置方法？

  XML和注解

各自的配置方式工作过程如图：

![image-20201008135554350](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008135554350.png)

### 定义xml标记、注解

![image-20201008135617693](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008135617693.png)

### Bean定义需要指定哪些信息？

**BeanDefinition接口告诉我们相关信息**

![image-20201008135658475](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008135658475.png)

**xml需要哪些标记信息？**

![image-20201008135714616](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008135714616.png)

![image-20201008135747994](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008135747994.png)

需要定义什么注解？

![image-20201008135938449](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008135938449.png)

### 如何指定配置？

![image-20201008135958067](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008135958067.png)

需要为用户提供一种方法？

该放在BeanFactory中吗？

![image-20201008140038524](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008140038524.png)

方框中所做的事情是解析bean配置，向beanFactory注册bean定义。它不是beanfactory的事情

应单独定义接口、类来完成这件事情

### ApplicationContext接口设计

**ApplicationContext用来完成Bean配置解析**

![image-20201008140223552](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008140223552.png)

xml配置方式的实现																			将BeanFactory抽出来放到父类中

xml源可能是多个

完成的工作：加载、解析、创建、注册



注解配置方式的实现：

扫描的包多个：list

完成多个：扫描、获取、创建、注册



**用户要使用我们的框架需要知道哪些接口、类？**

- 指定配置相关，xml、annotation
- 获取bean相关，BeanFactory

![image-20201008140536117](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008140536117.png)

外观模式应用：让用户只需要知道ApplicationContext及其子类是否对用户更简单？如何完成？



**提供一个新的外观（外观模式的应用）**

ApplicationContext继承自BeanFactory，两个接口合并到一起

![image-20201008140708440](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008140708440.png)



### 配置怎么加载及扫描？

![image-20201008140731905](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008140731905.png)

### XML文件来源处理

**xml配置文件来源会有的多种吗？**

![image-20201008140804235](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008140804235.png)

- 它们的加载方式一样吗？

  不一样

- 对应xml解析来说，从加载过程中它希望获得的是什么？

  InputStream

我们希望能加载不同来源的xml，向解析提供一致的使用接口。如何做？如何设计接口、类？

**xml配置文件接口的设计**

![image-20201008141008316](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141008316.png)

问题：这里我们定义不同的Resource类对应不同来源的xml资源，谁去负责创建它们的对象？

因为用户给定时是一个个的字符（这对他们是最简单的方式）

### xml资源加载器

**用户提供字符串定义资源，需要一个资源加载器分辨不同的资源**

分辨字符串，创建对应的Resource对象的工作就是加载，由ApplicationContext 。

![image-20201008141225090](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141225090.png)

如何区分不同的字符串代表不同的资源？

![image-20201008141247842](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141247842.png)

这里有需要应用工厂模式的味道：

根据不同的字符串前缀创建不同的资源对象

### 注解如何扫描？

**到指定的包目录下找出所有的文件夹（包含子孙包下）**

![image-20201008141407190](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141407190.png)

### 扫描的最终结果

- 扫到了指定包下的所有class文件，我们最终需要的是什么？

  我们需要的是类名

- 这里我们需要定义什么样的接口、类？

  ![image-20201008141520374](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141520374.png)

  最终扫描到的是class文件，File即可

### Annotation扫描工作

扫描的事情是由AnnotationApplicationContext来做还是外包给其他的类来做？

![image-20201008141623564](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141623564.png)

思考：在哪里启动扫描？

AnnotationApplicationContext构造方法中行吗？

### 配置解析

![image-20201008141658351](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141658351.png)

- 加载和扫描的输出是什么？

  Resource

### 扫描结果和BeanDefinition

xml和注解最终的输出都是Resource，这个Resource如何解析成Beandefinition？怎么设计？

![image-20201008141804995](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141804995.png)

需要一套接口和实现类

### 最终完成类图

![image-20201008141828895](D:\code\spring-framework-study\my-spring-aop\study-record\config\spring配置\img\image-20201008141828895.png)







