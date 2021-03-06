package net.younian.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;

@Activate(group = {Constants.PROVIDER}, order = 1)
public class TestFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //System.out.println("TestFilter filter is called..");
        return invoker.invoke(invocation);
    }
}
