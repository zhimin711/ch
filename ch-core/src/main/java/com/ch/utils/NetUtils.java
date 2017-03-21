package com.ch.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 *         2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class NetUtils {

    private NetUtils() {
    }

    public static String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "255.255.255.255";
    }

    public static byte[] getLocalAddress() {
        try {
            return InetAddress.getLocalHost().getAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
