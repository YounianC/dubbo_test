package net.younian.dubbo.filter;

import com.alibaba.dubbo.rpc.*;

public class TestFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("TestFilter filter is called..");
        return invoker.invoke(invocation);
    }
}
