package com.ch.utils;

import com.ch.type.SystemType;

/**
 * 平台工具类
 * Created by 01370603 on 2017/11/13.
 */
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
}
