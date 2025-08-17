package com.textanalysis.bit.exception;

public class FileSizeExceededException extends TextAnalysisException {
    public FileSizeExceededException(String message) {
        super(message);
    }
    
    public FileSizeExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
