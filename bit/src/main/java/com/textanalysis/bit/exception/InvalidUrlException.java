package com.textanalysis.bit.exception;

public class InvalidUrlException extends TextAnalysisException {
    public InvalidUrlException(String message) {
        super(message);
    }
    
    public InvalidUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
