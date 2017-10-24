package net.younian.dubbo;


import java.util.Date;

/**
 * Created by Administrator on 2017/7/11.
 */
public class TestServiceImpl implements TestService {

    public String getData() {
        System.out.println("getData is called");
        return  "DATA:" + new Date().toString();
    }
}
