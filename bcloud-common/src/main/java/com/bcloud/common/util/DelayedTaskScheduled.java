package com.bcloud.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author JianhuiChen
 * @description 延时任务调度类
 * @date 2019-10-10
 */
public class DelayedTaskScheduled {
    /**
     * 默认线程池的大小
     */
    private static final int DEFAULT_CORE_POLL_SIZE = 3;

    /**
     * 定时任务执行线程池
     */
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(DEFAULT_CORE_POLL_SIZE);

    /**
     * 静态内部类延迟加载
     */
    private static class SingletonHolder {
        private static final DelayedTaskScheduled INSTANCE = new DelayedTaskScheduled();
    }

    /**
     * 私有构造方法防止直接构造
     */
    private DelayedTaskScheduled() {
    }

    /**
     * 获取单例
     *
     * @return 异步任务调度类
     */
    public static DelayedTaskScheduled getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 延时调度一个任务 默认60s
     *
     * @param task 任务
     */
    public static void schedule(Runnable task) {
        DelayedTaskScheduled.schedule(task, 60, TimeUnit.SECONDS);
    }

    /**
     * 延时调度一个任务
     *
     * @param task     任务
     * @param delay    延时
     * @param timeUnit 延时描述
     */
    public static void schedule(Runnable task, long delay, TimeUnit timeUnit) {
        DelayedTaskScheduled.getInstance().getThreadPool().schedule(task, delay, timeUnit);
    }


    private ScheduledExecutorService getThreadPool() {
        return threadPool;
    }

    /**
     * example
     */
    public static void main(String[] args) {
        DelayedTaskScheduled.schedule(() -> System.out.println("30s passed"), 30, TimeUnit.SECONDS);
        DelayedTaskScheduled.schedule(() -> {
            System.out.println("60s passed");
            System.exit(0);
        });
    }
}
