package wpf.example.dubbo.bootuserprovider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Component;
import wpf.dubbodemo.bean.UserAddress;
import wpf.dubbodemo.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * 关于Hystrix：
 * 主要测试了两个情况：
 *  1.根据随机数抛出异常，测试消费方的 @HystrixCommand(fallbackMethod = "callAfterException")
 *
 *  2.直接抛出异常，测试 Hystrix 的熔断机制。见注解中的注释。
 *      更多属性：https://www.cnblogs.com/yawen/p/6655352.html
 * @HystrixCommand 的更多属性配置及其含义：
 *  https://www.cnblogs.com/yawen/p/6655352.html
 *  https://www.cnblogs.com/520playboy/p/8074347.html
 */
@Service    //dubbo的注解，暴露服务
@Component
public class UserServiceImpl implements UserService {

    //默认是线程隔离：请求和服务之间加入了线程池。每个请求线程会从线程池中获取一个线程
    // 去调用依赖代码，而不是直接去调用，调用结束后线程池会回收线程。调用超时可以直接
    // 返回。线程池中没有空闲的线程时，可做降级处理，即调用fallbackMethod；默认不采用
    // 排队，加速失败判定时间。线程数是可以被设定的（coreSize）。调用结束后可统计调用
    // 的结果（成功/失败），当失败的次数达到一定的阈值之后就会触发熔断机制。
    // 这种模式适合绝大多数情况，开销较高，若每秒有上百个线程应该考虑用信号量模式。
    @HystrixCommand(commandProperties = {
            //指定线程池的大小，默认为10，有限流作用。
            @HystrixProperty(name="coreSize",value = "10"),
            //指定排队的队列大小，默认为-1表示不开启。
            @HystrixProperty(name="maxQueueSize",value = "10"),
            //指定排队队列的阈值，达到阈值后不再让其排队，直接拒绝；maxQueueSize为-1的
            //  时候该项配置无效。
            @HystrixProperty(name="queueSizeRejectionThreshold",value = "8"),
            //每10秒内失败 value=2 次就会熔断，默认是20次。
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
            //熔断后多少时间去尝试使用服务
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "60000"),
            //超时时间，默认1000，超时可以直接中断
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    //限流主要配置：coreSize、maxQueueSize、queueSizeRejectionThreshold、
    //  timeoutInMilliseconds
    //熔断主要配置：circuitBreaker.requestVolumeThreshold、
    //  circuitBreaker.sleepWindowInMilliseconds、
    //  execution.isolation.thread.timeoutInMilliseconds

    //信号量隔离模式：每个请求线程进来需要获取一个信号量，获取到才能继续访问，依赖代码
    // 的调用仍然是由请求线程来进行调用，调用完成后归还信号量。当请求的线程大于最大请
    // 求数（maxConcurrentRequests）时，直接拒绝，然后转fallbackMethod。失败的次
    // 数达到了一定的阈值之后就会触发熔断机制。
    // （测试了一下，没有信号量的时候，不会进行熔断）
    // 由于获取信号量的操作是同步的，所以导致无法做超时（只能依靠调用协议超时，无法主动释放）。
    // 开销相对较小，并发请求受信号量的个数的限制。该模式适用于调用本地服务，无网络请求的情况
/*   @HystrixCommand(commandProperties = {
            //指定为信号隔离
            @HystrixProperty(name="execution.isolation.strategy",value = "SEMAPHORE"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "60000"),
            //信号量最大并发度，默认是10，其实这就有限流的作用了。
            @HystrixProperty(name="execution.isolation.semaphore.maxConcurrentRequests",value = "5"),
            //超时时间，默认1000，信号量隔离模式下是执行完了再判断是否超时
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })*/
    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        System.out.println("\n-------Provider's method was called--------\n");
        UserAddress address1 = new UserAddress(1, "北京市昌平区宏福科技园综合楼3层", "1", "李老师", "010-56253825", "Y");
        UserAddress address2 = new UserAddress(2, "深圳市宝安区西部硅谷大厦B座3层（深圳分校）", "1", "王老师", "010-56253825", "N");
        throw new RuntimeException();
//        if(Math.random()>0.5){
//            throw new RuntimeException();
//        }
        //return Arrays.asList(address1,address2);
    }
}
