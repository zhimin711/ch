package com.ch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * 描述：com.ch.utils
 *
 * @author 80002023
 *         2017/2/5.
 * @version 1.0
 * @since 1.8
 */
public class IOUtils extends org.apache.commons.io.IOUtils {

    private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

    private IOUtils() {
    }

    public static void close(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    if (closeable != null)
                        closeable.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                    logger.error("Close {} io failed! {}", closeable.getClass().getName(), e.getMessage());
                }
            }
        }
    }
}
