package com.textanalysis.bit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.text-analysis")
public class AppConfig {
    private int chunkSize = 1000;
    private int chunkOverlap = 50;
    private int threadPoolSize = 10;
    private String maxFileSize = "100MB";
    private String requestTimeout = "300s";
    
    // Getters and Setters
    public int getChunkSize() { return chunkSize; }
    public void setChunkSize(int chunkSize) { this.chunkSize = chunkSize; }
    public int getChunkOverlap() { return chunkOverlap; }
    public void setChunkOverlap(int chunkOverlap) { this.chunkOverlap = chunkOverlap; }
    public int getThreadPoolSize() { return threadPoolSize; }
    public void setThreadPoolSize(int threadPoolSize) { this.threadPoolSize = threadPoolSize; }
    public String getMaxFileSize() { return maxFileSize; }
    public void setMaxFileSize(String maxFileSize) { this.maxFileSize = maxFileSize; }
    public String getRequestTimeout() { return requestTimeout; }
    public void setRequestTimeout(String requestTimeout) { this.requestTimeout = requestTimeout; }
}
