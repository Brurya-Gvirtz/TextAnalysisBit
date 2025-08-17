package com.textanalysis.bit.model.dto;

import java.util.List;

public class TextAnalysisResponse {
    private List<WordMatch> matches;
    private String status;
    private long processingTimeMs;
    
    public TextAnalysisResponse() {}
    
    public TextAnalysisResponse(List<WordMatch> matches, String status, long processingTimeMs) {
        this.matches = matches;
        this.status = status;
        this.processingTimeMs = processingTimeMs;
    }
    
    public List<WordMatch> getMatches() { return matches; }
    public void setMatches(List<WordMatch> matches) { this.matches = matches; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
}

