# AOP分析

## AOP框架的责任

### AOP是什么？

Aspect Oriented Programming面向切面编程，在不改变代码的情况下，对类方法进行功能增强

### AOP框架要做什么？

AOP框架中要使用用户提供AOP功能，让用户可以用AOP技术实现对类方法进行功能增强

## AOP元素分析

### Advice 

通知，增强的功能

**用户提供 框架使用**

### Joint Points

连接点，可选的方法点

**框架提供，用户使用**

### Pointcut

切入点，选择切入的方法点

**用户提供，框架使用**

### Aspect

切面，选择的（多个）方法点+增强的功能

**用户提供 ，框架使用**

### Introduction

引入：添加新的方法、属性到已存在的类中

### Weaving

不改原类的代码，加入功能增强

**框架实现**

思考：这些东西，哪些是用户提供的，哪些是AOP框架要写好的？



根据AOP的定义，得出AOP需要提供的功能特性

- Aspect 切面

  - Advice 通知

    进行功能增强：功能

  - Pointcuts 切入点

    对类方法增强：选择要增强的方法

- Weaving 织入（编织）
  - 不改变原类的代码，实现功能增强

## 图解AOP

![image-20201005103234565](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005103234565.png)

## AOP概念-特定分析

Advice、Pointcut、Weaving各自有什么特点？

- Advice
  - 用户性：由用户提供增强功能逻辑代码
  - 变化性：不同的增强需求，会有不同的逻辑
  - 可选时机：可选择在方法功能前、后、异常时进行功能增强
  - 多重性：同一个切入点上可以有多重增强

- Pointcut
  - 用户性：由用户来指定
  - 变化性：用户可灵活指定
  - 多点性：用户可以选择在多个点上进行增强

- Weaving
  - 无侵入性，不改变原类代码
  - 在AOP框架中实现

## Advice设计

### Advice是由用户提供，我们来使用，它是多变的。

- 我们如何能认识用户提供的东西？用户在我们写好框架后使用我们的框架
- 如何让我们的代码隔绝用户提供的多变性

**我们定义一套标准接口，用户通过实现接口来提供他们不同的逻辑**

**重要设计原则：如何应对变化，面向接口编程**

### 定义Advice接口

接口没有定义任何方法

![image-20201005103857484](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005103857484.png)

### Advice的特性：可选时机，可选择在方法功能前、后、异常时进行功能增强

- 有的advice是在方法执行前进行增强

  前置增强

- 有的是在方法执行后进行增强

  后置增强

- 有的会是之前、之后都进行增强

  环绕增强

- 有的则只是在方法执行抛出异常时进行异常增强处理

  异常处理增强



问：我们需要做什么？

​	定义标准接口方法，让用户可以实现它，提供各种增强

问：这四种增强所需的参数一样吗？

​	不清楚，分析分析

## Advice设计-前置增强分析

### 前置增强：在方法执行前进行增强

- 它可能需要什么参数？

  目的是对方法进行增强，应该需要的是方法相关的信信息；我们使用它时 ，能给它的好像也只有要执行方法的信息

- 运行时方法有哪些信息？
  - 方法本身	Method
  - 方法所属的对象   Object
  - 方法的参数  Object[]

- 前置增强的返回值是什么？

  在方法执行前进行增强，不需要返回值！

## Advice设计-后置增强分析

### 后置增强：在方法执行后进行增强

- 它可能需要什么参数？
  - 方法本身	Method
  - 方法所属的对象  Object
  - 方法的参数     Object[]
  - 方法的返回值  Object

- 它的返回值是什么？

  在方法执行后进行增强，不需要返回值！

## Advice设计-环绕增强分析

### 环绕增强：包裹方法进行增强

- 它可能需要什么参数？
  - 方法本身   Method
  - 方法所属的对象   Object
  - 方法的参数   Object[]

- 它的返回值是什么？

  方法被它包裹，也即方法的将由它来执行，它需要返回方法的返回值。  Object

## Advice设计-异常处理增强分析

### 异常处理增强：捕获方法执行时的异常，进行增强处理

- 进行异常处理增强需要包裹方法吗？

  需要

- 那它可否在环绕中实现？

  可以

![image-20201005105136200](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005105136200.png)

## Advice设计

### 经前面的分析，我们一共需要定义三个方法

思考：是把这三个定义到一个接口中，换是分三个接口定义？

**分三个接口，还可通过类型来区分不同的Advice**

![image-20201005105304872](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005105304872.png)



## Pointcut分析

### Pointcut的特点

- 用户性

  由用户来指定

- 变化性

  用户灵活指定

- 多点性

  用户可以选择在多个点上进行增强

### 我们需要做什么？

为用户提供一个东西，让他们可以灵活地指定多个方法点，而我们又能懂！

思考：切入点是由用户来指定在哪些方法点上进行增强，那么这个哪些方法点如何来表示，能满足上面的特点？

### 分析

- 指定哪些方法，是不是一个描述信息？

- 如何来指定一个方法？  **xx类的xx方法**

- 重载怎么办？   **加上参数类型**

- 总结：其实就是一个完整的方法签名！

  ![image-20201005105732929](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005105732929.png)

- 如何做到多点性，灵活性？在一个描述中指定一类方法？

  - 某个包下的某个类的某个方法
  - 某个包下的所有类中所有方法
  - 某个包下的所有类中的do开头的方法
  - 某个包下的以service结尾的类中的do开头的方法
  - 某个包下的及其子包下的以service结尾的类的中的do开头的方法

  **我们需要一个表达式，能灵活描述这些的信息的表达式**

- 要表达哪些信息？

  包名.类名.方法名（参数类型）

- 每部分的要求是怎样的？
  - 包名：有父子特定，要能模糊匹配
  - 类名：要能模糊匹配
  - 方法：要能模糊匹配
  - 参数类型：参数可以多个

- 这个表达式将被我们用来决定是否需要多某个类的某方法进行增强，这个决定过程应该是怎样的？

  匹配类，匹配方法

- 一个表达式如果不好实现，分成多个表达式进行组合是否容易些？

  **是的，可以这么考虑**

- 我们掌握的哪些表达式有哪些？它们是否能满足这里的需求？

  - 正则表达式

  - Ant Path表达式

  - AspectJ的pointcut表达式  

    ![image-20201005110442957](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005110442957.png)

  正则表达式可以，Aspectj本就是切面编程，也是可以的。

  ### AspectJ表达式

  ![image-20201005110603279](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005110603279.png)

  ### 该来对Pointcut进行接口、类设计了

  - 切点应有什么属性？

    切点定义表达式

  - 切点应对外提供什么行为（方法）

  - 切点将被我们用来作什么？

    对类、方法进行匹配

    切点应提供匹配类，匹配方法的行为

  - 如果在我们设计的框架中要能灵活扩展切点的实现方式，我们该如何设计？

    这又是一个要支持可多变的问题，就像通知一样，我们来定义一套标准接口，定义好基本行为，面向接口编程，屏蔽掉具体的实现。

    无论哪种实现，都实现匹配类、匹配方法的接口

  ### Pointcut标准接口

  ![image-20201005111052219](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111052219.png)

  ![image-20201005111101701](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111101701.png)

### AspectJExpressionPointcut实现

![image-20201005111135316](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111135316.png)

![image-20201005111142794](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111142794.png)

### 实现步骤

- 引入AspectJ的jar

  ![image-20201005111222643](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111222643.png)

- 掌握AspectJ的api使用，我们只使用它的切点表达式解析匹配部分

  - 入口：获得切点表达式

    ![image-20201005111340056](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111340056.png)

  - 解析表达式

    ![image-20201005111359706](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111359706.png)

  - 用PointcutExpression匹配类，不可靠，没关系，通过方法匹配来正确匹配

    ![image-20201005111456466](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111456466.png)

  - 用PointcutExpression匹配方法，可靠

    ![image-20201005111520115](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111520115.png)

## Advice Pointcut使用

### 用户该如何使用我们提供的东西？

![image-20201005111609366](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111609366.png)

## Aspect设计

### 为用户提供更简单的外观，Advisor（通知者）组合Advice和Pointcut

![image-20201005111734928](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111734928.png)

### 扩展不同的Advice实现

![image-20201005111750949](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111750949.png)

### 还可这样设计

![image-20201005111810359](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005111810359.png)

## Weaving织入-分析

### 织入要完成什么？

将用户提供的增强功能加到指定的方法上。这部分是要我们实现的！

### 请思考以下问题：

- 在什么时候做织入？

  创建bean实例的时候，在bean初始化后，再对其进行增强

- 如何确定bean要增强

  对bean类及方法挨个匹配用户指定的切面，如果有切面匹配就是要增强

- 如何织入

  代理

### Weaving 织入-设计

### 整理一下AOP的使用流程，帮助我们更好的去设计织入

![image-20201005112134959](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005112134959.png)

### 请思考

- 用户到哪里去注册切面？

  BeanFactory？

- 判断匹配、织入的逻辑写到哪里？

  写到BeanFactory中？

- 我们现在是不是要在bean创建过程中加一项处理？

  后续可能在bean创建过程中还会加入更多的别的处理，如果直接在BeanFactory中实现会有什么不好？

  - BeanFactory类代码会爆炸
  - 不易扩展

- 回顾总结下Bean的产生过程都经历了什么

  ![image-20201005112446488](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005112446488.png)

  在这个过程中，将会有更多的处理逻辑加入到过程的不同阶段

  我们如何设计能让我们一次写好BeanFactory后，不改代码，就可以灵活扩展！

  在各个节点加入扩展点，加入注册机制

  什么是扩展点，什么是注册机制？

  **想想观察者模式（监听模式）**

  这里有六个扩展点，就是六个主题，六类观察者。

  该设计几个接口？

  ![image-20201005113039987](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113039987.png)

  ### 应用观察者模式来加入我们的AOP织入

  ![image-20201005113109224](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113109224.png)

  ## Weaving织入-实现

  ### 现在来聚焦实现织入：判断bean是否需要织入增强

  ![image-20201005113157238](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113157238.png)

  ### 现在来聚焦实现织入：代理增强

  ![image-20201005113223104](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113223104.png)

### 回顾-JDK动态代理

### 在运行时，对接口创建代理对象

**![image-20201005113314139](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113314139.png)**

## 回顾-CGLIB动态代理

![image-20201005113340699](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113340699.png)

## Weaving织入-搭架子

![image-20201005113411731](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113411731.png)

## Weaving织入-增强逻辑实现

![image-20201005113445739](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113445739.png)

## Weaving织入-代理创建实现

![image-20201005113513352](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113513352.png)

## Weaving织入-传递构造参数、参数类型

![image-20201005113550554](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113550554.png)

- 如何来传递创建bean实例时获得的数据到初始化后的Aop中？

  ![image-20201005113621728](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113621728.png)

  这里我们要考虑BeanFactory的IOC&DI的纯洁性，不应与其他功能有染！

  ## Weaving织入-如何使用AopProxy

  ![image-20201005113749940](D:\code\spring-framework-study\my-spring-aop\study-record\spring-aop框架\img\image-20201005113749940.png)

  