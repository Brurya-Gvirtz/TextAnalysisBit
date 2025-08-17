package com.textanalysis.bit.service;

import com.textanalysis.bit.config.AppConfig;
import com.textanalysis.bit.exception.InvalidUrlException;
import com.textanalysis.bit.exception.FileSizeExceededException;
import com.textanalysis.bit.model.internal.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class TextReaderService {
    
    private static final Logger logger = LoggerFactory.getLogger(TextReaderService.class);
    private final AppConfig appConfig;
    
    public TextReaderService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
    
    public List<TextChunk> readTextInChunks(String textUrl) throws InvalidUrlException, FileSizeExceededException {
        logger.info("Starting to read text from URL: {}", textUrl);
        
        List<TextChunk> chunks = new ArrayList<>();
        
        try {
            URL url = new URL(textUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(30000); // 30 seconds
            connection.setReadTimeout(60000); // 60 seconds
            
            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new InvalidUrlException("HTTP error code: " + responseCode + " for URL: " + textUrl);
            }
            
            // Check content length if available
            long contentLength = connection.getContentLengthLong();
            if (contentLength > 0 && contentLength > parseFileSize(appConfig.getMaxFileSize())) {
                throw new FileSizeExceededException("File size exceeds maximum allowed: " + appConfig.getMaxFileSize());
            }
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                chunks = processTextInChunks(reader);
            }
            
        } catch (IOException e) {
            logger.error("Error reading text from URL: {}", textUrl, e);
            throw new InvalidUrlException("Unable to read from URL: " + textUrl, e);
        }
        
        logger.info("Successfully read {} chunks from URL", chunks.size());
        return chunks;
    }
    
    private List<TextChunk> processTextInChunks(BufferedReader reader) throws IOException {
        List<TextChunk> chunks = new ArrayList<>();
        List<String> currentChunk = new ArrayList<>();
        List<String> overlapBuffer = new ArrayList<>();
        
        String line;
        int currentLine = 0;
        int globalCharOffset = 0;
        int chunkStartLine = 0;
        
        while ((line = reader.readLine()) != null) {
            currentChunk.add(line);
            currentLine++;
            
            // Check if chunk is full
            if (currentChunk.size() >= appConfig.getChunkSize()) {
                // Create chunk with overlap from previous chunk
                StringBuilder chunkContent = new StringBuilder();
                
                // Add overlap from previous chunk
                for (String overlapLine : overlapBuffer) {
                    chunkContent.append(overlapLine).append("\n");
                }
                
                // Add current chunk content
                for (String chunkLine : currentChunk) {
                    chunkContent.append(chunkLine).append("\n");
                }
                
                // Create and add chunk
                TextChunk chunk = new TextChunk(
                    chunkContent.toString(), 
                    chunkStartLine, 
                    currentLine - 1, 
                    globalCharOffset
                );
                chunks.add(chunk);
                
                // Update global char offset
                globalCharOffset += chunkContent.length();
                
                // Prepare overlap for next chunk
                overlapBuffer.clear();
                int overlapStart = Math.max(0, currentChunk.size() - appConfig.getChunkOverlap());
                for (int i = overlapStart; i < currentChunk.size(); i++) {
                    overlapBuffer.add(currentChunk.get(i));
                }
                
                // Reset for next chunk
                chunkStartLine = currentLine - appConfig.getChunkOverlap();
                currentChunk.clear();
            }
        }
        
        // Handle remaining lines
        if (!currentChunk.isEmpty() || !overlapBuffer.isEmpty()) {
            StringBuilder chunkContent = new StringBuilder();
            
            for (String overlapLine : overlapBuffer) {
                chunkContent.append(overlapLine).append("\n");
            }
            
            for (String chunkLine : currentChunk) {
                chunkContent.append(chunkLine).append("\n");
            }
            
            if (chunkContent.length() > 0) {
                TextChunk chunk = new TextChunk(
                    chunkContent.toString(), 
                    chunkStartLine, 
                    currentLine - 1, 
                    globalCharOffset
                );
                chunks.add(chunk);
            }
        }
        
        return chunks;
    }
    
    private long parseFileSize(String sizeStr) {
        sizeStr = sizeStr.toUpperCase().trim();
        long multiplier = 1;
        
        if (sizeStr.endsWith("KB")) {
            multiplier = 1024;
            sizeStr = sizeStr.substring(0, sizeStr.length() - 2);
        } else if (sizeStr.endsWith("MB")) {
            multiplier = 1024 * 1024;
            sizeStr = sizeStr.substring(0, sizeStr.length() - 2);
        } else if (sizeStr.endsWith("GB")) {
            multiplier = 1024 * 1024 * 1024;
            sizeStr = sizeStr.substring(0, sizeStr.length() - 2);
        }
        
        return Long.parseLong(sizeStr.trim()) * multiplier;
    }
}
