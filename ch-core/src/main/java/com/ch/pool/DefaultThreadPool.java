package com.ch.pool;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * 描述：com.ch.pool
 *
 * @author 80002023
 *         2017/3/15.
 * @version 1.0
 * @since 1.8
 */
public class DefaultThreadPool {


    private final static BlockingQueue<Runnable> QUEUE;

    private final static ExecutorService POOL;

    static {
        QUEUE = new ArrayBlockingQueue<>(10000);
        POOL = new ThreadPoolExecutor(10, 30, 50, TimeUnit.MILLISECONDS, QUEUE);
//        POOL = createFixedPool(10);
    }

    public static ExecutorService getInstance() {
        return POOL;
    }

    public static ExecutorService createFixedPool(int num) {
        return Executors.newFixedThreadPool(num);
    }

    public static void exe(Runnable command) {
        getInstance().execute(command);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return getInstance().submit(task);
    }

    public static <T> T invokeAny(Collection<Callable<T>> tasks) {
        try {
            return getInstance().invokeAny(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks) {
        try {
            return getInstance().invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void shutdown() {
        if (getInstance() != null && !getInstance().isShutdown())
            getInstance().shutdown();
    }


}
