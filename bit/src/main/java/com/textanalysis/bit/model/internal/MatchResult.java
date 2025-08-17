package com.textanalysis.bit.model.internal;

public class MatchResult {
    private final String word;
    private final int lineNumber;
    private final int charPosition;
    private final int globalCharOffset;
    
    public MatchResult(String word, int lineNumber, int charPosition, int globalCharOffset) {
        this.word = word;
        this.lineNumber = lineNumber;
        this.charPosition = charPosition;
        this.globalCharOffset = globalCharOffset;
    }
    
    // Getters
    public String getWord() { return word; }
    public int getLineNumber() { return lineNumber; }
    public int getCharPosition() { return charPosition; }
    public int getGlobalCharOffset() { return globalCharOffset; }
}
