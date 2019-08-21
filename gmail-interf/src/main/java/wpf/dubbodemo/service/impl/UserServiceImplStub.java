package wpf.dubbodemo.service.impl;

import wpf.dubbodemo.bean.UserAddress;
import wpf.dubbodemo.service.UserService;

import java.util.List;

/**
 * 本地存根，在远程调用之前可做一些别的操作，如参数验证、缓存、判断
 */
public class UserServiceImplStub  implements UserService {

    private final UserService userService;

    /**
     * 该有参构造器是必须的
     * 传入的是UserService远程的代理对象，且是dubbo自动传入的
     * @param userService
     */
    public UserServiceImplStub(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        System.out.println("本地存根被调用了");
        if (!"".equals(userId)){
            return userService.getUserAddressList(userId);
        }
        return null;
    }
}
