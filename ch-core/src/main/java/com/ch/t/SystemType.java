package com.ch.t;

/**
 * 系统类型
 * Created by 01370603 on 2017/11/13.
 */
public enum SystemType {
    ANY("any"),
    LINUX("Linux"),
    MAC_OS("Mac OS"),
    MAC_OS_X("Mac OS X"),
    WINDOWS("Windows"),
    OS2("OS/2"),
    SOLARIS("Solaris"),
    SUN_OS("SunOS"),
    MPE_IX("MPE/iX"),
    HP_UX("HP-UX"),
    AIX("AIX"),
    OS390("OS/390"),
    FREE_BSD("FreeBSD"),
    IRIX("Irix"),
    DIGITAL_UNIX("Digital Unix"),
    NET_WARE_411("NetWare"),
    OSF1("OSF1"),
    OPEN_VMS("OpenVMS"),
    OTHERS("Others");

    SystemType(String desc) {
        this.description = desc;
    }

    public String toString() {
        return description;
    }

    private String description;
}
