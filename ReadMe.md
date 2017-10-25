# 分为四个模块

* interface：接口定义，需要消费者和生产者分别引用
* service：生产者具体实现，这里使用mysql
* provider：生产者HTTP请求方式的Tomcat实现，可以通过调用接口实现和dubbo一样的功能，主要用于测试比较
* consumer：消费者，两种方式调用生产者服务：dubbo和HTTP请求


测试结果发现用HTTP请求和Dubbo的完成时间基本差不多，其实理论上完全可以使用HTTP自建RPC，但是基于方便扩展以及维护的考虑，还是使用dubbo好的多啊。

----

## 20171024新增
* provider_jar 用自建Main方法类运行spring容器启动dubbo服务

>为什么对于服务提供方的工程，dubbo官方推荐使用可执行jar的方式运行呢，下面对三种启动方式进行比较:  
** 使用web容器（Tomcat、Jetty等）启动dubbo服务 ** ： 增加端口管理复杂性， tomcat/jetty等都需要占用端口，dubbo服务也需要端口；浪费资源（内存），单独启动 tomcat，jetty占用内存大  
** 使用自建Main方法类运行spring容器启动dubbo服务**：Dobbo提供的优雅停机高级特性没用上，并且自已编写启动类可能会有缺陷  
** 使用Dubbo框架提供的Main方法类运行Spring容器启动服务**：官方建议使用，dubbo框架本身提供启动类（com.alibaba.dubbo.container.Main），可实现优雅关机

* 添加Filter例子
    * 添加类implements Filter
    * 在src\main\resources\META-INF\dubbo添加文件,文件名为`com.alibaba.dubbo.rpc.Filter`，内容为`logFilter=net.younian.dubbo.filter.LogFilter`
    * spring-dubbo.xml添加配置` <dubbo:provider filter="logFilter,testFilter"/>`

内置@Activate Filter 通过注解的order值进行排序，值越小调用越靠前；  
用户自定义的filter不参与排序，但是在配置文件中`filter="filterA,filterB"`是可以指定顺序的。
如果用户定义的Filter想参与内置filter排序，则在类上添加`@Activate(group = {Constants.PROVIDER}, order = 1)` 并取消 spring-dubbo.xml中的filter配置，此时相当于转换成内置Filter参与排序；
```
public List<T> getActivateExtension(URL url, String[] values, String group) {
        List<T> exts = new ArrayList<T>();
        List<String> names = values == null ? new ArrayList<String>(0) : Arrays.asList(values);
        if (!names.contains(Constants.REMOVE_VALUE_PREFIX + Constants.DEFAULT_KEY)) {
            getExtensionClasses();
            for (Map.Entry<String, Activate> entry : cachedActivates.entrySet()) {
                String name = entry.getKey();
                Activate activate = entry.getValue();
                if (isMatchGroup(group, activate.group())) {
                    T ext = getExtension(name);
                    if (!names.contains(name)
                            && !names.contains(Constants.REMOVE_VALUE_PREFIX + name)
                            && isActive(activate, url)) {
                        exts.add(ext);
                    }
                }
            }
            Collections.sort(exts, ActivateComparator.COMPARATOR);
        }
        List<T> usrs = new ArrayList<T>();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (!name.startsWith(Constants.REMOVE_VALUE_PREFIX)
                    && !names.contains(Constants.REMOVE_VALUE_PREFIX + name)) {
                if (Constants.DEFAULT_KEY.equals(name)) {
                    if (usrs.size() > 0) {
                        exts.addAll(0, usrs);
                        usrs.clear();
                    }
                } else {
                    T ext = getExtension(name);
                    usrs.add(ext);
                }
            }
        }
        if (usrs.size() > 0) {
            exts.addAll(usrs);
        }
        return exts;
    }
```

-----

## 20171025新增
### 添加测试Hystrix自动降级

>在服务可用率低的情况下，为了防止大量线程阻塞在等待中，启动熔断，将该接口的调用 **快速失败** ，使其他优先级较高的服务能够正常使用。

### 示例流程
* 消费者添加POM依赖
    ```
    <dependency>
        <groupId>com.netflix.hystrix</groupId>
        <artifactId>hystrix-core</artifactId>
        <version>1.5.12</version>
    </dependency>
    ```
* 实现Filter `HystrixFilter` ,以及继承`HystrixCommand`的`DubboHystrixCommand`
* Provider 的调用函数中添加随机超时代码4秒，并且在`dubbo:service`中添加timeout属性值为3秒，这样可以实现随机比率的请求超时。
* 启动Provider，启动Consumer，可以看见有一定数量的成功请求，而且在一定次数的请求失败之后Consumer调用直接返回 `getFallback` 的预设值。100次请求完成时间约为10秒左右。
* **比较：** 去掉Consumer的Filter，重新启动Consumer，100次完成时间是约58秒。

> 由此可以得出：Hystrix能够在服务低成功率时，为了避免多次重复请发（默认dubbo会在请求失败之后重试），并且如果需要保证其他服务的运行，不在等待 **优先级不是很重要服务** 返回，则会直接返回预设结果，防止请求堆积

-----