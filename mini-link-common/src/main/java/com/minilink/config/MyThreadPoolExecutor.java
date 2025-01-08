package com.minilink.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-6  20:06
 * @Description: 自定义线程池
 * @Version: 1.0
 */
@Configuration
public class MyThreadPoolExecutor {
    public static final String THREAD_POOL_NAME = "threadPoolTaskExecutor";

    /**
     * CPU 密集型：线程数建议设置为 CPU 核心数 + 1。
     * IO 密集型：线程数建议设置为 2 * CPU 核心数。
     */
    @Bean(THREAD_POOL_NAME)
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cpuCores + 1);
        executor.setMaxPoolSize(cpuCores + 1);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("mini-link-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
