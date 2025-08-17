package com.textanalysis.bit.model.dto;
import java.util.List;

public class WordMatch {
    private String word;
    private List<WordLocation> locations;
    
    public WordMatch() {}
    
    public WordMatch(String word, List<WordLocation> locations) {
        this.word = word;
        this.locations = locations;
    }
    
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    public List<WordLocation> getLocations() { return locations; }
    public void setLocations(List<WordLocation> locations) { this.locations = locations; }
}
