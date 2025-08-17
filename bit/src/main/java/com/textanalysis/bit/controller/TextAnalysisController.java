package com.textanalysis.bit.controller;

import com.textanalysis.bit.model.dto.TextAnalysisRequest;
import com.textanalysis.bit.model.dto.TextAnalysisResponse;
import com.textanalysis.bit.service.TextAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/text-analysis")
@Tag(name = "Text Analysis", description = "API for finding words in large text files")
public class TextAnalysisController {
    
    private static final Logger logger = LoggerFactory.getLogger(TextAnalysisController.class);
    
    private final TextAnalysisService textAnalysisService;
    
    public TextAnalysisController(TextAnalysisService textAnalysisService) {
        this.textAnalysisService = textAnalysisService;
    }
    
    @PostMapping("/match")
    @Operation(summary = "Find words in text from URL", 
               description = "Downloads text from the provided URL and searches for specified words")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "404", description = "Text URL not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TextAnalysisResponse> matchWords(@Valid @RequestBody TextAnalysisRequest request) {
        logger.info("Received text analysis request for URL: {}", request.getTextUrl());
        
        TextAnalysisResponse response = textAnalysisService.analyzeText(request);
        
        logger.info("Text analysis completed successfully");
        return ResponseEntity.ok(response);
    }
}
