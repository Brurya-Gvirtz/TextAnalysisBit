package com.textanalysis.bit.model.dto;

public class WordLocation {
    private int lineOffset;
    private int charOffset;
    
    public WordLocation() {}
    
    public WordLocation(int lineOffset, int charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
    }
    
    public int getLineOffset() { return lineOffset; }
    public void setLineOffset(int lineOffset) { this.lineOffset = lineOffset; }
    public int getCharOffset() { return charOffset; }
    public void setCharOffset(int charOffset) { this.charOffset = charOffset; }
}
