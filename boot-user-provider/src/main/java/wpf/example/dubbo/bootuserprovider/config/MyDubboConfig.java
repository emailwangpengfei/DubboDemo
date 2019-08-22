package wpf.example.dubbo.bootuserprovider.config;

import com.alibaba.dubbo.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wpf.dubbodemo.service.UserService;

import java.util.Arrays;

//@Configuration
public class MyDubboConfig {
    //<dubbo:application name="boot-user-provider-ByConfigClass"></dubbo:application>
    @Bean
    public ApplicationConfig applicationConfig(){
        ApplicationConfig config = new ApplicationConfig();
        config.setName("boot-user-provider-ByConfigClass");
        return config;
    }

    //<dubbo:registry protocol="zookeeper" address="127.0.0.1:2181"></dubbo:registry>
    @Bean
    public RegistryConfig registryConfig(){
        RegistryConfig config = new RegistryConfig();
        config.setProtocol("zookeeper");
        config.setAddress("127.0.0.1:2181");
        return config;
    }

    //<dubbo:protocol name="dubbo" port="20882"></dubbo:protocol>
    @Bean
    public ProtocolConfig protocolConfig(){
        ProtocolConfig config = new ProtocolConfig();
        config.setName("dubbo");
        config.setPort(20880);
        return config;
    }

    /**
     * <dubbo:service interface="wpf.dubbodemo.service.UserService"
     *                    ref="userServiceImpl01" timeout="1000" version="1.0.0">
     *         <dubbo:method name="getUserAddressList" timeout="1000"></dubbo:method>
     * </dubbo:service>
     * 服务的实现
     * <bean id="userServiceImpl01" class="wpf.example.dubbo.bootuserprovider.service.impl.UserServiceImpl"></bean>
     * @return
     */
    @Bean
    public ServiceConfig<UserService> serviceConfig(UserService userService){
        ServiceConfig<UserService> config = new ServiceConfig<>();
        config.setInterface("wpf.dubbodemo.service.UserService");
        config.setRef(userService);
        config.setTimeout(1000);
        config.setVersion("1.0.0");

        //配置每一个method的信息
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("getUserAddressList");
        methodConfig.setTimeout(1000);

        //将method的设置关联到service配置中
        config.setMethods(Arrays.asList(methodConfig));

        return config;
    }

    //<dubbo:monitor protocol="registry"></dubbo:monitor>
//    public MonitorConfig monitorConfig(){
//        return null;
//    }

    //<dubbo:provider timeout="1000"></dubbo:provider>
    @Bean
    public ProviderConfig providerConfig(){
        ProviderConfig config = new ProviderConfig();
        config.setTimeout(2000);
        return config;
    }
}
