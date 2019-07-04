package com.ch;

/**
 * 描述：String status
 *
 * @author 80002023
 * 2017/2/4.
 * @version 1.0
 * @since 1.8
 */
public class Constants {

    public static final String SUPER_ADMIN = "admin";
    public final static String CURRENT_USER = "currentUser";
    public static final String TOKEN_HEADER = "Authorization";
    public final static String TOKEN_USER = "X-AUTH-USER";
    public final static String TOKEN = "token";
    public final static String SECURITY_ROLE_PREFIX = "ROLE_";
    public final static String SECURITY_SEPARATOR = ":";
    public final static String SEPARATOR = "-";
    public final static String SEPARATOR_2 = ",";


    public final static String STATUS = "status";
    /**
     * base int status
     */
    public final static int UNKNOWN = -1;
    public final static int FAILED = 0;
    public final static int SUCCESS = 1;
    public final static int ERROR = 2;
    /**
     * base String status
     */
    public final static String DISABLED = "0";
    public final static String ENABLED = "1";

    /**
     * boolean status
     */
    public final static boolean YES = true;
    public final static boolean NO = false;

}
