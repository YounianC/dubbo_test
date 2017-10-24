package net.younian.dubbo.filter;

import com.alibaba.dubbo.rpc.*;

public class LogFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("LogFilter filter is called..");
        return invoker.invoke(invocation);
    }
}
