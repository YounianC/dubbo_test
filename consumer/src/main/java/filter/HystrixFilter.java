package filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import hystrix.DubboHystrixCommand;

public class HystrixFilter implements Filter {

    @Override
    public Result invoke(Invoker invoker, Invocation invocation) throws RpcException {
        DubboHystrixCommand command = new DubboHystrixCommand(invoker, invocation);
        return (Result) command.execute();
    }

}
