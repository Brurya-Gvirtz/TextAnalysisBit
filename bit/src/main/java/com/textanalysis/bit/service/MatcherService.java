package com.textanalysis.bit.service;

import com.textanalysis.bit.model.internal.MatchResult;
import com.textanalysis.bit.model.internal.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MatcherService {
    
    private static final Logger logger = LoggerFactory.getLogger(MatcherService.class);
    
    public List<MatchResult> findMatches(TextChunk chunk, Set<String> wordsToFind) {
        logger.debug("Processing chunk from line {} to {}", chunk.getStartLine(), chunk.getEndLine());
        
        List<MatchResult> results = new ArrayList<>();
        String content = chunk.getContent();
        String[] lines = content.split("\n");
        
        int currentGlobalCharOffset = chunk.getGlobalCharOffset();
        int currentLineNumber = chunk.getStartLine();
        
        for (String line : lines) {
            for (String word : wordsToFind) {
                // Create case-insensitive word boundary pattern
                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                
                while (matcher.find()) {
                    int charPositionInLine = matcher.start();
                    int globalCharPosition = currentGlobalCharOffset + charPositionInLine;
                    
                    MatchResult result = new MatchResult(
                        word,
                        currentLineNumber,
                        charPositionInLine,
                        globalCharPosition
                    );
                    
                    results.add(result);
                    logger.debug("Found '{}' at line {} position {}", word, currentLineNumber, charPositionInLine);
                }
            }
            
            currentGlobalCharOffset += line.length() + 1; // +1 for newline character
            currentLineNumber++;
        }
        
        logger.debug("Found {} matches in chunk", results.size());
        return results;
    }
}

