分为四个模块
	interface：接口定义，需要消费者和生产者分别引用

    service：生产者具体实现，这里使用mysql

    provider：生产者HTTP请求方式的Tomcat实现，可以通过调用接口实现和dubbo一样的功能，主要用于测试比较

    consumer：消费者，两种方式调用生产者服务：dubbo和HTTP请求
	
测试结果发现用HTTP请求和Dubbo的完成时间基本差不多，其实理论上完全可以使用HTTP自建RPC，但是基于方便扩展以及维护的考虑，还是使用dubbo好的多啊。