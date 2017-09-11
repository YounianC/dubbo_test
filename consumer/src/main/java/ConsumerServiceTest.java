import com.alibaba.dubbo.common.threadpool.support.fixed.FixedThreadPool;
import net.younian.dubbo.HttpRequest;
import net.younian.dubbo.TestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/7/11.
 */
public class ConsumerServiceTest {

    public static void main(String[] args) {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});
        context.start();
        int i = 0;
        final long start = System.currentTimeMillis();
        System.out.println(start);
        ExecutorService executorServicePool = Executors.newFixedThreadPool(10);
        final TestService testService = (TestService) context.getBean("testServiceImpl");
        final CountDownLatch cdl = new CountDownLatch(100);
        final StringBuilder stringBuilder = new StringBuilder();
        while (i++ < 100) {
            executorServicePool.execute(new Runnable() {
                public void run() {
                    cdl.countDown();
                    //System.out.println(cdl.getCount());
                    if (cdl.getCount() == 0) {
                        stringBuilder.append(System.currentTimeMillis());
                        long end = Long.parseLong(stringBuilder.toString());
                        System.out.println("" + end + "-" + start + "=" + (end - start));
                    }
                    dubboGetData();
                }

                private void dubboGetData() {
                    String a = testService.getData();
                }

                private void httpGetData() {
                    String b = HttpRequest.sendGet("http://localhost:8080/getData.do", null, null);
                }
            });
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
