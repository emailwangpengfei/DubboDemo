import org.springframework.context.support.ClassPathXmlApplicationContext;
import wpf.dubbodemo.service.OrderService;

import java.io.IOException;

/**
 * 遇到的坑：
 *  1. provider.xml的<dubbo:service />节点中的版本号 和 consumer.xml中的<dubbo:reference />
 *      节点的version（版本号）值需要一致
 *  2. 由于安装了虚拟机，服务提供者提供服务的时候使用的是虚拟网卡。禁用即可。
 */
public class MainApplication {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        OrderService service = context.getBean(OrderService.class);
        service.initOrder("1");

        System.out.println("调用结束。。。。");
        System.in.read();
    }
}
