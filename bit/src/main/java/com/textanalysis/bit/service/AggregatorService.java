package com.textanalysis.bit.service;
import com.textanalysis.bit.model.dto.WordLocation;
import com.textanalysis.bit.model.dto.WordMatch;
import com.textanalysis.bit.model.internal.MatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AggregatorService {
    
    private static final Logger logger = LoggerFactory.getLogger(AggregatorService.class);
    
    public List<WordMatch> aggregateResults(List<List<MatchResult>> allResults) {
        logger.info("Aggregating results from {} chunks", allResults.size());
        
        // Use ConcurrentHashMap for thread safety
        Map<String, Set<MatchResult>> wordToResults = new ConcurrentHashMap<>();
        
        // Collect all results
        for (List<MatchResult> chunkResults : allResults) {
            for (MatchResult result : chunkResults) {
                wordToResults.computeIfAbsent(result.getWord().toLowerCase(), 
                    k -> ConcurrentHashMap.newKeySet()).add(result);
            }
        }
        
        // Convert to final format and deduplicate
        List<WordMatch> finalResults = new ArrayList<>();
        
        for (Map.Entry<String, Set<MatchResult>> entry : wordToResults.entrySet()) {
            String word = entry.getKey();
            Set<MatchResult> results = entry.getValue();
            
            // Deduplicate based on global character offset
            Map<Integer, MatchResult> uniqueResults = results.stream()
                .collect(Collectors.toMap(
                    MatchResult::getGlobalCharOffset,
                    r -> r,
                    (existing, replacement) -> existing // Keep first occurrence
                ));
            
            // Convert to WordLocation and sort by position
            List<WordLocation> locations = uniqueResults.values().stream()
                .sorted(Comparator.comparingInt(MatchResult::getGlobalCharOffset))
                .map(result -> new WordLocation(result.getLineNumber(), result.getGlobalCharOffset()))
                .collect(Collectors.toList());
            
            if (!locations.isEmpty()) {
                finalResults.add(new WordMatch(word, locations));
                logger.debug("Word '{}' found {} times", word, locations.size());
            }
        }
        
        // Sort results by word name for consistent output
        finalResults.sort(Comparator.comparing(WordMatch::getWord));
        
        logger.info("Aggregation complete. Found matches for {} words", finalResults.size());
        return finalResults;
    }
}
