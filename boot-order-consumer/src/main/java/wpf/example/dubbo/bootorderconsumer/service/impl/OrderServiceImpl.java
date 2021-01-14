package wpf.example.dubbo.bootorderconsumer.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpf.dubbodemo.bean.UserAddress;
import wpf.dubbodemo.service.OrderService;
import wpf.dubbodemo.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * 1、将服务提供者注册到注册中心（暴露服务）
 * 		1）、导入dubbo依赖（2.6.2）\操作zookeeper的客户端(curator)
 * 		2）、配置服务提供者
 *
 * 2、让服务消费者去注册中心订阅服务提供者的服务地址
 */
@Service
class OrderServiceImpl implements OrderService {

    //@Autowired
    @Reference  //远程引用服务
    //@Reference(loadbalance = "random")  //设置负载均衡机制（四种）
    //@Reference(mock="fail" /* mock = force */)  //服务降级，这个一般是通过zookeeper设置，这里写死不好
    //@Reference(url = "127.0.0.1:20880") //dubbo直连，不经过注册中心
    //@Reference(cluster="failsafe") //集群容错
    UserService userService;

    //该方法出错了，就转向callAfterException方法进行容错处理。
    @HystrixCommand(fallbackMethod = "callAfterException",
        commandProperties = {
            //回调方法的最大并发度，默认是10
            @HystrixProperty(name="fallback.isolation.semaphore.maxConcurrentRequests",value = "50")
        }
    )
    @Override
    public List<UserAddress> initOrder(String userId) {
        System.out.println("用户id："+userId);
        //1、查询用户的收货地址
        List<UserAddress> addressList = userService.getUserAddressList(userId);
        for (UserAddress userAddress : addressList) {
            System.out.println(userAddress.getUserAddress());
        }
        return addressList;
    }

    public List<UserAddress> callAfterException(String userId){
        System.out.println("********fallbackMethod was called********");
        return Arrays.asList(new UserAddress(10,"测试",userId,"测试","测试","Y"));
    }
}