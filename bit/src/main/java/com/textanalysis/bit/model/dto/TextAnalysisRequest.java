package com.textanalysis.bit.model.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class TextAnalysisRequest {
    @NotBlank(message = "Text URL is required")
    @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
    private String textUrl;
    
    @NotEmpty(message = "Words list cannot be empty")
    private List<String> words;
    
    public TextAnalysisRequest() {}
    
    public TextAnalysisRequest(String textUrl, List<String> words) {
        this.textUrl = textUrl;
        this.words = words;
    }
    
    public String getTextUrl() { return textUrl; }
    public void setTextUrl(String textUrl) { this.textUrl = textUrl; }
    public List<String> getWords() { return words; }
    public void setWords(List<String> words) { this.words = words; }
}
