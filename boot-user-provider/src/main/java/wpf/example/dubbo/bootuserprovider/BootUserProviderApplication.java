package wpf.example.dubbo.bootuserprovider;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ImportResource;

/**
 * 1. 导入依赖：
 *      1)、dubbo-starter（包含了curator等，只需要引入dubbo-starter即可）
 *      2)、其他依赖，curator
 *
 * 2. SpringBoot与dubbo整合的三种方式：
 *      1）、导入dubbo-starter，在application.properties配置属性，使用@Service【暴露
 *          服务】，使用@Reference【引用服务】。这种方式无法做到方法级别的精确。
 *      2）、保留dubbo 的 xml配置文件;
 * 		    导入dubbo-starter，使用@ImportResource导入dubbo的配置文件即可。采用了保留
 * 		    配置文件的方式当然可以做到方法级别的精确。
 *      3）、使用注解API的方式：
 * 		    将每一个组件手动创建到容器中,让dubbo来扫描其他的组件。即每一个xxx标签对应着
 * 		    xxxConfig类。还是需要使用@Service暴露服务。
 */

//方式一：开启基于注解的dubbo功能。其实主要用作包扫描，也可以在application.properties中
// 指定包扫描，就不需要该注解了。
//@EnableDubbo

//方式二：
//@ImportResource(locations = "classpath:provider.xml")

//方式三：
//@DubboComponentScan(basePackages = "wpf.example.dubbo.bootuserprovider")

@EnableDubbo
@EnableHystrix  //开启服务容错
@SpringBootApplication
public class BootUserProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootUserProviderApplication.class, args);
    }

}
