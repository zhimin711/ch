package com.ch.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 * 2017/2/5.
 * @version 1.0
 * @since 1.8
 */
@Slf4j
public class IOUtils {


    private IOUtils() {
    }

    /**
     * 关闭IO
     *
     * @param closeables IO
     */
    public static void close(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    if (closeable != null)
                        closeable.close();
                } catch (IOException e) {
                    log.error("Close io failed!", e);
                }
            }
        }
    }

    /**
     * Closes a URLConnection.
     *
     * @param conn the connection to close.
     * @since 2.4
     */
    public static void close(final URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

}
