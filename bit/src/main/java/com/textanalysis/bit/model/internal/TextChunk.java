package com.textanalysis.bit.model.internal;

public class TextChunk {
    private final String content;
    private final int startLine;
    private final int endLine;
    private final int globalCharOffset;

    public TextChunk(String content, int startLine, int endLine, int globalCharOffset) {
        this.content = content;
        this.startLine = startLine;
        this.endLine = endLine;
        this.globalCharOffset = globalCharOffset;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getGlobalCharOffset() {
        return globalCharOffset;
    }
}
