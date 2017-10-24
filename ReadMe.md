# 分为四个模块

* interface：接口定义，需要消费者和生产者分别引用
* service：生产者具体实现，这里使用mysql
* provider：生产者HTTP请求方式的Tomcat实现，可以通过调用接口实现和dubbo一样的功能，主要用于测试比较
* consumer：消费者，两种方式调用生产者服务：dubbo和HTTP请求


测试结果发现用HTTP请求和Dubbo的完成时间基本差不多，其实理论上完全可以使用HTTP自建RPC，但是基于方便扩展以及维护的考虑，还是使用dubbo好的多啊。

## 20171024新增
* provider_jar 用自建Main方法类运行spring容器启动dubbo服务

>为什么对于服务提供方的工程，dubbo官方推荐使用可执行jar的方式运行呢，下面对三种启动方式进行比较:  
** 使用web容器（Tomcat、Jetty等）启动dubbo服务 ** ： 增加端口管理复杂性， tomcat/jetty等都需要占用端口，dubbo服务也需要端口；浪费资源（内存），单独启动 tomcat，jetty占用内存大  
** 使用自建Main方法类运行spring容器启动dubbo服务**：Dobbo提供的优雅停机高级特性没用上，并且自已编写启动类可能会有缺陷  
** 使用Dubbo框架提供的Main方法类运行Spring容器启动服务**：官方建议使用，dubbo框架本身提供启动类（com.alibaba.dubbo.container.Main），可实现优雅关机

