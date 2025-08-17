package com.textanalysis.bit.service;
import com.textanalysis.bit.model.dto.TextAnalysisRequest;
import com.textanalysis.bit.model.dto.TextAnalysisResponse;
import com.textanalysis.bit.model.dto.WordMatch;
import com.textanalysis.bit.model.internal.MatchResult;
import com.textanalysis.bit.model.internal.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class TextAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(TextAnalysisService.class);
    
    private final TextReaderService textReaderService;
    private final MatcherService matcherService;
    private final AggregatorService aggregatorService;
    private final Executor textProcessingExecutor;
    
    public TextAnalysisService(
            TextReaderService textReaderService,
            MatcherService matcherService,
            AggregatorService aggregatorService,
            @Qualifier("textProcessingExecutor") Executor textProcessingExecutor) {
        this.textReaderService = textReaderService;
        this.matcherService = matcherService;
        this.aggregatorService = aggregatorService;
        this.textProcessingExecutor = textProcessingExecutor;
    }
    
    public TextAnalysisResponse analyzeText(TextAnalysisRequest request) {
        logger.info("Starting text analysis for URL: {} with {} words", 
                   request.getTextUrl(), request.getWords().size());
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Step 1: Read text in chunks
            List<TextChunk> chunks = textReaderService.readTextInChunks(request.getTextUrl());
            
            // Step 2: Convert words to set for faster lookup (case-insensitive)
            Set<String> wordsToFind = request.getWords().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
            
            // Step 3: Process chunks in parallel
            List<CompletableFuture<List<MatchResult>>> futures = chunks.stream()
                .map(chunk -> CompletableFuture.supplyAsync(
                    () -> matcherService.findMatches(chunk, wordsToFind),
                    textProcessingExecutor
                ))
                .collect(Collectors.toList());
            
            // Step 4: Wait for all futures to complete
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
            );
            
            // Step 5: Collect all results
            List<List<MatchResult>> allResults = allFutures
                .thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()))
                .join();
            
            // Step 6: Aggregate results
            List<WordMatch> matches = aggregatorService.aggregateResults(allResults);
            
            long processingTime = System.currentTimeMillis() - startTime;
            
            logger.info("Text analysis completed in {} ms. Found matches for {} words",
                       processingTime, matches.size());
            
            return new TextAnalysisResponse(matches, "SUCCESS", processingTime);
            
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            logger.error("Text analysis failed after {} ms", processingTime, e);
            throw e;
        }
    }
}

