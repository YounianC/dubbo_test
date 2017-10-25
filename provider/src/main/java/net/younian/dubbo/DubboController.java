package net.younian.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DubboController {

    @Autowired
    private TestService testServiceImpl;

    @ResponseBody
    @RequestMapping(value = "getData")
    public String getData() {
        String str = testServiceImpl.getData(-1);
        return str;
    }
}
