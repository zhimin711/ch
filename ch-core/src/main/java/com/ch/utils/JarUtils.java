package com.ch.utils;

import com.ch.e.PubError;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * @author 01370603
 * @date 2018/9/25 16:50
 */
public class JarUtils {

    private JarUtils() {
    }

    private static Map<String, URLClassLoader> clazzLoader = Maps.newConcurrentMap();

    /**
     * 加载Jar包
     *
     * @param path jar包路径
     * @throws NoSuchMethodException
     */
    public static void load(String path) throws NoSuchMethodException {
        // 系统类库路径
        File libPath = new File(path);
        // 获取所有的.jar和.zip文件
        File[] jarFiles = libPath.listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".zip"));
        if (jarFiles != null) {
            // 从URLClassLoader类中获取类所在文件夹的方法
            // 对于jar文件，可以理解为一个存放class文件的文件夹
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            boolean accessible = method.isAccessible();        // 获取方法的访问权限
            try {
                if (!accessible) {
                    method.setAccessible(true);
                    // 设置方法的访问权限
                }
                // 获取系统类加载器
                URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                for (File file : jarFiles) {
                    URL url = file.toURI().toURL();
                    try {
                        method.invoke(classLoader, url);
//                        LOG.debug("读取jar文件[name={}]", file.getName());
                    } catch (Exception e) {
//                        LOG.error("读取jar文件[name={}]失败", file.getName());
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } finally {
                method.setAccessible(accessible);
            }

        }
    }

    /**
     * 加载类Jar包并加载类
     *
     * @param path      jar包路径
     * @param className 类全名称
     * @return
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClassForJar(String path, String className) throws MalformedURLException, ClassNotFoundException {
        if (clazzLoader.get(path) != null) {
            return clazzLoader.get(path).loadClass(className);
        }
        URL url1 = new URL(path);
        Class<?> clazz;
        synchronized (JarUtils.class) {
            URLClassLoader myClassLoader1 = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());
            clazz = myClassLoader1.loadClass(className);
            clazzLoader.put(path, myClassLoader1);
        }
        return clazz;
    }


    /**
     * 重载类Jar包并加载类
     *
     * @param path      jar包路径
     * @param className 类全名称
     * @return
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     */
    public static Class<?> reloadClassForJar(String path, String className) throws MalformedURLException, ClassNotFoundException {
        if (clazzLoader.get(path) != null) {
            try {
                URLClassLoader.class.getMethod("close");
                clazzLoader.get(path).close();
                clazzLoader.remove(path);
            } catch (NoSuchMethodException | SecurityException | IOException e) {
                ExceptionUtils._throw(PubError.CONNECT, e);
            }
        }
        return loadClassForJar(path, className);
    }

}
