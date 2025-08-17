package com.textanalysis.bit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    
    private final AppConfig appConfig;
    
    public AsyncConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
    
    @Bean(name = "textProcessingExecutor")
    public Executor textProcessingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(appConfig.getThreadPoolSize());
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("TextProcessor-");
        executor.initialize();
        return executor;
    }
}
