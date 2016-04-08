package com.netease.channel.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 异步线程工具类
 */
public class AsyncExecutor {

    private static AsyncExecutor asyncExecutor = new AsyncExecutor();
    private static ThreadPoolExecutor exec = new ThreadPoolExecutor(5, 10, 300,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000),
            new channelThreadFactory(), new DiscardOldestPolicy());

    private AsyncExecutor() {
    }

    public static AsyncExecutor getInstance() {
        return asyncExecutor;
    }

    public void submit(Runnable task) {
        exec.execute(task);
    }

}
