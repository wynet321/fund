package net.canglong.fund.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 */
@Configuration
public class ThreadPoolConfig {

    @Value("${fund.threadCount:20}")
    private int threadCount;

    /**
     * 价格处理线程池
     */
    @Bean("priceExecutor")
    public ThreadPoolTaskExecutor priceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCount);
        executor.setMaxPoolSize(threadCount * 2);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("Price-Task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 基金处理线程池
     */
    @Bean("fundExecutor")
    public ThreadPoolTaskExecutor fundExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCount);
        executor.setMaxPoolSize(threadCount * 2);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("Fund-Task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 统计任务线程池
     */
    @Bean("statisticExecutor")
    public ThreadPoolTaskExecutor statisticExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCount);
        executor.setMaxPoolSize(threadCount * 2);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("Statistic-Task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}