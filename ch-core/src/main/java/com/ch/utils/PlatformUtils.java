package com.ch.utils;

import com.ch.t.SystemType;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 平台工具类
 * Created by 01370603 on 2017/11/13.
 */
@Slf4j
public class PlatformUtils {

    private PlatformUtils() {
    }

    public static SystemType getSysType() {
        String name = System.getProperty("os.name").toLowerCase();
        if (CommonUtils.isEmpty(name)) {
            return SystemType.OTHERS;
        }
        if (name.contains("mac") && name.indexOf("os") > 0 && !name.contains("x")) {
            return SystemType.MAC_OS;
        } else if (name.contains("mac") && name.indexOf("os") > 0 && name.indexOf("x") > 0) {
            return SystemType.MAC_OS_X;
        } else if (name.contains("windows")) {
            return SystemType.WINDOWS;
        } else if (name.contains("os/2")) {
            return SystemType.OS2;
        } else if (name.contains("solaris")) {
            return SystemType.SOLARIS;
        } else if (name.contains("sunos")) {
            return SystemType.SUN_OS;
        } else if (name.contains("mpe/ix")) {
            return SystemType.MPE_IX;
        } else if (name.contains("hp-ux")) {
            return SystemType.HP_UX;
        } else if (name.contains("aix")) {
            return SystemType.AIX;
        } else if (name.contains("os/390")) {
            return SystemType.OS390;
        } else if (name.contains("freebsd")) {
            return SystemType.FREE_BSD;
        } else if (name.contains("irix")) {
            return SystemType.IRIX;
        } else if (name.contains("digital") && name.contains("unix")) {
            return SystemType.DIGITAL_UNIX;
        } else if (name.contains("netware")) {
            return SystemType.NET_WARE_411;
        } else if (name.contains("osf1")) {
            return SystemType.OSF1;
        } else if (name.contains("openvms")) {
            return SystemType.OPEN_VMS;
        } else if (name.contains("linux")) {
            return SystemType.LINUX;
        }
        return SystemType.OTHERS;
    }

    public static boolean isWindows() {
        return getSysType() == SystemType.WINDOWS;
    }

    public static boolean isLinux() {
        return getSysType() == SystemType.LINUX;
    }

    public static boolean isUnix() {
        return getSysType() == SystemType.DIGITAL_UNIX;
    }

    public static boolean isMac() {
        return (getSysType() == SystemType.MAC_OS || getSysType() == SystemType.MAC_OS_X);
    }

    public static String getVersion() {
        return System.getProperty("os.version");
    }

    public static String getArch() {
        return System.getProperty("os.arch");
    }

    /**
     * 获取主机名
     *
     * @return
     */
    public static String getHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();//获取主机名
        } catch (Exception e) {
            log.error("get host name error!", e);
        }
        return "";
    }

    /**
     * 获取主机的域名
     *
     * @return 主机的域名
     */
    public static String getCanonicalHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getCanonicalHostName();//获取主机的域名
        } catch (Exception e) {
            log.error("get canonical host name error!", e);
        }
        return "";
    }

    /**
     * 获取本机IP
     *
     * @return 本机IP
     */
    public static String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("get local host ip error!", e);
        }
        return "255.255.255.255";
    }

    /**
     * 获取本机IP byte[]
     *
     * @return 本机IP byte[]
     */
    public static byte[] getLocalAddress() {
        try {
            return InetAddress.getLocalHost().getAddress();
        } catch (UnknownHostException e) {
            log.error("get local host ip byte error!", e);
        }
        return null;
    }

    /**
     * 根据Request（HttpServletRequest）获取来访者的浏览器版本
     * request.getHeader("user-agent")
     *
     * @param headerUserAgent request
     * @return
     */
    public static String getBrowserInfo(String headerUserAgent) {
        String browserVersion = null;
//        String header = request.getHeader("user-agent");
        if (CommonUtils.isEmpty(headerUserAgent)) {
            return browserVersion;
        }
        if (headerUserAgent.indexOf("MSIE") > 0) {
            browserVersion = "IE";
        } else if (headerUserAgent.indexOf("Firefox") > 0) {
            browserVersion = "Firefox";
        } else if (headerUserAgent.indexOf("Chrome") > 0) {
            browserVersion = "Chrome";
        } else if (headerUserAgent.indexOf("Safari") > 0) {
            browserVersion = "Safari";
        } else if (headerUserAgent.indexOf("Camino") > 0) {
            browserVersion = "Camino";
        } else if (headerUserAgent.indexOf("Konqueror") > 0) {
            browserVersion = "Konqueror";
        }
        return browserVersion;
    }

    /**
     * 获取系统版本信息 request.getHeader("user-agent")
     *
     * @param header request
     * @return
     */
    public static String getSystemInfo(String header) {
        String systemInfo = null;
//        String header = request.getHeader("user-agent");
        if (CommonUtils.isEmpty(header)) {
            return systemInfo;
        }
        //得到用户的操作系统
        if (header.indexOf("NT 6.0") > 0) {
            systemInfo = "Windows Vista/Server 2008";
        } else if (header.indexOf("NT 5.2") > 0) {
            systemInfo = "Windows Server 2003";
        } else if (header.indexOf("NT 5.1") > 0) {
            systemInfo = "Windows XP";
        } else if (header.indexOf("NT 6.1") > 0) {
            systemInfo = "Windows 7";
        } else if (header.indexOf("NT 6.2") > 0) {
            systemInfo = "Windows Slate";
        } else if (header.indexOf("NT 6.3") > 0) {
            systemInfo = "Windows 9";
        } else if (header.indexOf("NT 5") > 0) {
            systemInfo = "Windows 2000";
        } else if (header.indexOf("NT 4") > 0) {
            systemInfo = "Windows NT4";
        } else if (header.indexOf("Me") > 0) {
            systemInfo = "Windows Me";
        } else if (header.indexOf("98") > 0) {
            systemInfo = "Windows 98";
        } else if (header.indexOf("95") > 0) {
            systemInfo = "Windows 95";
        } else if (header.indexOf("Mac") > 0) {
            systemInfo = "Mac";
        } else if (header.indexOf("Unix") > 0) {
            systemInfo = "UNIX";
        } else if (header.indexOf("Linux") > 0) {
            systemInfo = "Linux";
        } else if (header.indexOf("SunOS") > 0) {
            systemInfo = "SunOS";
        }
        return systemInfo;
    }

}
