package top.xiemingmin.call.limit.intf;

/**
 * @Auther: xiemingmin
 * @Description: 用户信息接口
 * @Date: 2020/4/29 23:09
 */
public interface UserInfoSupport {

    /**
     * <p>
     *      当前线程用户信息，
     *      用户限流插件根据用户信息做限流
     *      eg：LoginContext.get()
     * </p>
     * @return 当前用户信息标识
     */
    String currentUserKey();

}
