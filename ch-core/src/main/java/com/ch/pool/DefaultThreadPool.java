package com.ch.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * 描述：默认静态线程池
 * <p>queue size: 1000</p>
 * <p>core pool size: 10</p>
 * <p>max pool size: 30</p>
 * <p>keep alive time: 50</p>
 *
 * @author 80002023
 * 2017/3/15.
 * @version 1.0
 * @since 1.8
 */
public class DefaultThreadPool {

    private final static Logger logger = LoggerFactory.getLogger(DefaultThreadPool.class);

    private final static BlockingQueue<Runnable> QUEUE;

    private final static ExecutorService POOL;

    static {
        QUEUE = new ArrayBlockingQueue<>(10000);
//        POOL = new ThreadPoolExecutor(10, 30, 50, TimeUnit.MILLISECONDS, QUEUE);
        int num = Runtime.getRuntime().availableProcessors() / 2;
        POOL = createFixedPool(num);
    }

    private static ExecutorService getInstance() {
        return POOL;
    }

    public static ExecutorService createFixedPool(int num) {
        return Executors.newFixedThreadPool(num);
    }

    /**
     * 执行一个线程，无返回
     *
     * @param command 线程
     */
    public static void exe(Runnable command) {
        getInstance().execute(command);
    }

    /**
     * 提交线程任务
     *
     * @param task 线程任务
     * @param <T>  任务执行对象
     * @return 返回线程执行结果
     */
    public static <T> Future<T> submit(Callable<T> task) {
        return getInstance().submit(task);
    }

    public static <T> T invokeAny(Collection<Callable<T>> tasks) {
        try {
            return getInstance().invokeAny(tasks);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("DefaultThreadPool invokeAny Error!", e);
        }
        return null;
    }

    /**
     * 批量提交线程任务
     *
     * @param tasks 线程任务
     * @param <T>   任务执行对象
     * @return 返回线程执行结果集
     */
    public static <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks) {
        try {
            return getInstance().invokeAll(tasks);
        } catch (InterruptedException e) {
            logger.error("DefaultThreadPool invokeAll Error!", e);
        }
        return null;
    }

    /**
     * 关闭线程池
     */
    @Deprecated
    protected static void shutdown() {
        if (getInstance() != null && !getInstance().isShutdown()) {
            getInstance().shutdown();
        }
    }


}
