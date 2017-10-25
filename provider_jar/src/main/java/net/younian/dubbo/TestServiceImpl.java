package net.younian.dubbo;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/7/11.
 */
public class TestServiceImpl implements TestService {

    private Map<Integer, Integer> map = new HashMap<>();
    private AtomicInteger total = new AtomicInteger(0);

    @Override
    public String getData(int count) {
        total.incrementAndGet();
        if (map.containsKey(count)) {
            map.put(count, map.get(count) + 1);
        } else {
            map.put(count, 1);
        }
        System.out.println(count + " getData is called, time = " + map.get(count) + " ,total = " + total.get());
        /*try {
            Random random = new Random();
            int r = random.nextInt();
            if (r % 3 < 2) {
                Thread.sleep(4000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "DATA:" + new Date().toString();
    }
}
