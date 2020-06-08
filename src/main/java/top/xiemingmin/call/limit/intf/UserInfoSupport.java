package top.xiemingmin.call.limit.intf;

/**
 * *******************************************************************************************
 * ***            X I E M I N G M I N . T O P  ---  U N C L E  G E O R G E                 ***
 * *******************************************************************************************
 *
 * @author xiemingmin
 * Date: 2020/4/29 22:09
 * Description: 接口防刷工具
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
