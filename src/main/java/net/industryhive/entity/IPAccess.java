package net.industryhive.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局单例实体类
 * 用来存放所有访问IP的访问时间
 * 如果有需要也可以用来存放访问ip的访问次数
 *
 * @author 未央
 * @create 2019-12-03 10:42
 */
public class IPAccess {

    private static IPAccess ipAccess = new IPAccess();

    /**
     * 存储每一个访问ip的访问时间
     */
    private Map<String, Date> accessTime;

    private IPAccess(){
        this.accessTime = new HashMap<String, Date>();
    }

    public static IPAccess getIPAccess(){
        return ipAccess;
    }

    public Map<String, Date> getAccessTime() {
        return accessTime;
    }
}
