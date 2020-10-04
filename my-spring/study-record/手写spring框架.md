spring的问题域

IOC容器

变化：对象的创建方式不同，抽象出来编程接口

接口：编程痛点，不好扩展，维护性

DI注入

APO功能增强



**设计实现一个IOC容器**

需要以下三个元素：

- bean工厂接口，IOC容器实现抽象接口，向用户提供获取Bean基本入口
- Bean定义接口，向IOC容器
- Bean定义的注册接口

# 手写spring IOC&DI

## IOC分析

### IOC是什么？

**IOC：Inversion of Controller 控制反转，也成依赖倒置（发转）**

是面向对象编程中的一种设计原则，可以用来降低代码之间的耦合度，**常见的方式为依赖注入，依赖查找**

问题：如何理解控制反转？

**反转：依赖对象的获得被反转了。由传统的自己创建，反转为从IOC容器中获取（或自动注入）**

示例：

Class A中用到了Class B的对象b，一般情况下，需要在A的代码中显示的new一个B的对象。采用依赖注入技术后，A的代码只需要定义一个私有的B对象，不需要直接new来获得这个对象，而是通过相关的容器控制程序将B对象在外部new出来注入到A类的引用中。

**体现了面向对象设计原则之一：好莱坞法则（“别来找我们，我们找你）**

### 带来了什么好处？

- 代码更简洁了，不需要去new要使用的对象了
- 面向接口编程，使用者与具体类解耦，易扩展、替换实现者
- 可以方便进行AOP增强

### IOC容器做什么工作？

IOC容器的工作：负责创建、管理类实例，向使用者提供实例

### IOC容器是否是工厂模式的实例？

是的。IOC容器负责来创建类实例对象，需要就从IOC容器中get。也称IOC容器为Bean工厂

## IOC容器&实现

### Bean工厂怎么知道该如何创建bean？

![image-20201003160721140](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003160721140.png)

### 如何告诉它？

**我们可以给它定义一个定义注册接口**

## Bean定义注册接口

BeanDefinitionRegistry

- bean定义注册接口应定义些什么方法？

  **注册、获取bean定义**

- 注册的bean定义信息如何区分？

  **每个bean定义有一个唯一的名称**

  ![image-20201003160943750](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003160943750.png)

  ![image-20201003160950732](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003160950732.png)

## Bean定义

- bean定义的用途是什么？

  **告诉bean工厂该如何创建某类bean**

- 获得类的实例的方式有哪些？
  - new构造方法
  - 工厂方法
    -  静态的
    - 成员方法

- bean工厂帮我们创建bean时，它需要获知什么信息？

  - new构造方法

    **类名**

  - 静态工厂方法

    **工厂类名、工厂方法名**

  - 成员工厂方法？

    **工厂类名？ 工厂bean名  工厂方法名**

- 每次从bean工厂获取bean实例时，是否都需要创建一个新的？

  **否，有的只需要单例**

- bean定义是给bean工厂创建bean用的，那bean定义接口该向bean工厂提供哪些方法？
  - **获取bean的类名：getBeanClass（）：Class**
  - **获取工厂方法名：getFactoryMethodName（）：String**
  - **获取工厂bean名：getFactoryBeanName（）：String**
  - **是否是单例等方法：getScope（）：string   isSingleton（） isPrototype（）**

- 类对象交给IOC容器来管理，类对象的生命周期中换可能有什么生命阶段事情要做吗？
  - **比如创建对象后可能需要进行一些初始化**
  - **换有有些对象在销毁时可能要进行一些特定的销毁逻辑（如释放资源）**
  - **那就在bean定义中提供让用户可指定初始化、销毁方法**
  - **对bean工厂就需要提供getInitMethodName（）， getDestroyMethodName（）**

![image-20201003161923478](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003161923478.png)

![image-20201003161934874](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003161934874.png)

**接口有了，来实现一个通用的bean定义GenericBeanDefinition**

## IOC容器设计与实现

![image-20201003162034715](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003162034715.png)

## BeanFactory实现

### 来实现一个最基础的默认工厂：DefaultBeanFactory

![image-20201003162134280](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003162134280.png)

### 实现定义信息注册

- bean定义信息如何存放？

  **Map**

- bean定义是否可以重名？重名怎么办？

  **我们是设计者，规则有我们来定**

### 实现bean工厂

- 创建的bean用什么存放，方便下次获取？

  **Map**

- 在getBean方法中要做哪些事？

  **创建bean实例，进行初始化**

## 扩展DefaulBeanFactory

### 思考：对于单例bean，我们可否提前实例化？这样有什么好处？

### 增加提前实例化单例Bean的功能

![image-20201003162502162](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003162502162.png)

**实现提前实例化单例Bean**

# DI分析

## DI依赖注入-分析

- 哪些地方会有依赖
  - **构造参数依赖**
  - **属性依赖**

- 依赖注入的本质是什么
  - **给值：给如构造参数值，给属性赋值**

- 参数值、属性值可能是什么值
  - **直接值、bean依赖**

- 直接值会有哪几种情形
  - **基本数据类型、String**
  - **数组、集合**
  - **Properties**
  - **map**

![image-20201003234639585](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003234639585.png)

**本质：参数值、属性值，都是值。bean工厂在进行依赖注入时，就是给入值**

- 如何告诉bean工厂该给如什么构造参数值？即如何来定义参数依赖？
- 如何来定义属性依赖？

# DI实现

## DI依赖注入-构造参数依赖定义分析

**如何定义构造参数依赖？**

![image-20201003234856708](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003234856708.png)

- 我们要创建一个Girl是如何创建的？

  ![image-20201003234921570](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003234921570.png)

- 那我们可不可以这样来定义构造参数依赖？
  - **第一个参数值是：“小丽”**
  - **第二个参数值是：18**
  - **第三个参数值是：‘D'**
  - **第四个参数值是：依赖一个Boy Bean**

## DI-构造参数依赖设计

- 参数可以多个，用什么存储？
  - **集合：List**

- 参数有顺序，如何体现顺序？
  - **按参数顺序放入List**

- 参数值可以是直接值，也可以是bean依赖，如何表示：Object？
  - **可以，也只能Object了**
  - **List<Object> constructorArgumentValues**

- 如果用Object来表示值，如何区分是Bean依赖？

  **为Bean依赖定义一种数据类型（BeanReference），bean工厂在构造Bean实例时，遍历判断参数是否是BeanReference，如是则替换为依赖的bean实例**

- 如果直接是数组、集合等，它们的元素中有的是bean依赖，怎么处理？

  **元素值换是BeanReference，同样bean工厂在使用时需遍历替换**

  **BeanReference该是怎样的？**

## DI实现-BeanReference

### BeanReference就是用来说明bean依赖的：依赖哪个Bean

![image-20201003235549522](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003235549522.png)

![image-20201003235558907](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003235558907.png)

## DI实现-构造参数依赖定义

### 在BeanDefinition中增加获得构造参数值的接口

![image-20201003235716733](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003235716733.png)

### 在GenericBeanDefinition中增加对应的实现

![image-20201003235751728](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003235751728.png)

**构造参数依赖有了，就可以来实现构造参数依赖注入了！**

## DI实现-BeanFactory中实现构造参数依赖注入

### 1.首先需要把bean定义中的构造参数定义转为真实的值，在DefaultBeanFactory中增加一个方法来干这事

![image-20201003235939306](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003235939306.png)

![image-20201003235953833](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201003235953833.png)

## 问题：有参数了，如何断定是哪个构造方法、哪个工厂方法？

- 方法是可以重载的

- 形参定义时可能是接口或父类

- 反射提供的获取构造方法、方法的API，如下

  ![image-20201004090817108](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004090817108.png)

### 判断逻辑：

1. 先根据参数的类型进行精确匹配查找，如未找到，则进行第2步查找
2. 获得所有的构造方法，遍历，通过参数数据过滤，再比对形参类型与实参类型

### 2.当我们判断出构造方法或工厂方法后，对于原型bean，下次获取Bean是否可以省去判断？

**也就是说，对于原型 bean，我们可以缓存下这个构造方法或工厂方法。如何实现？**

**在Beandefinition中增加缓存的方法：**

![image-20201004091142984](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004091142984.png)

**在GenericBeanDefinition中增加对应的实现：**

![image-20201004091214482](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004091214482.png)

### 3在DefaultBeanFactory中增加查找构造方法的方法

- ![image-20201004091403942](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004091403942.png)

### 4修改DefaultBeanFactory中用构造方法创建实例的方法代码

![image-20201004091437449](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004091437449.png)

### 5依照上面的方式修改静态工厂方法、工厂方法方式的参数依赖

![image-20201004091526102](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004091526102.png)

### 6循环依赖该如何处理？

- 构造对象时可以循环依赖吗？

  构造实例对象时的循环依赖会陷入僵尸局面，是不允许构造实例时的循环依赖的。如何发现循环依赖？

  **方式：加入一个正在构造的bean记录，每个bean开始构造时就加入到该记录中，构造完毕后从记录中移除。如果有依赖，先看依赖的baen是否在构造中，如是就构成了循环依赖，抛出异常**



![image-20201004091829070](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004091829070.png)

## DI实现-属性依赖设计&实现

![image-20201004091941805](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004091941805.png)

- 属性依赖是什么？

  **某个属性依赖某个值**

- 该如何描述一个属性依赖？

  **属性名、值，定义一个类来表示这两个值**

- 会有多个属性依赖，怎么存放？

  **List**

- 属性值的情况和构造参数值一样吗？

  **一样的**

### 动手1：定义属性依赖描述实体类PropertyValue

![image-20201004093056562](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004093056562.png)

### 动手2：在BeanDefinition中设计获得属性依赖定义的接口

![image-20201004093138009](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004093138009.png)

![image-20201004093146317](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004093146317.png)

在GenericBeanDefinition中增加对应的实现

![image-20201004093225397](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004093225397.png)

### 动手3：在DefaultBeanFactory中实现属性依赖

![image-20201004093301677](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004093301677.png)

在doGetBean(String beanName)中增加设置属性依赖的调用

![image-20201004093352232](D:\code\spring-framework-study\my-spring\study-record\手写spring框架\img\image-20201004093352232.png)

## 扩展-加需求

### 1 Bean增加别名支持

Bean除了标识唯一的名称外，换可以有任意个别名，别名也是唯一的。

### 2 BeanFactory增加可按Class来获取Bean对象的功能

### 3 加入配置参数加载、注入给Bean的功能

### 4 加入Bean配置的条件依赖生效支持

在bean定义配置中可以指定它条件依赖某些bean或类，当这些bean和类存在时，这个bean配置才有效

