package com.example.io.binaryfilecopier;

import java.io.IOException;

public class FileCopyException extends IOException {
    
    public FileCopyException(String message) {
        super(message);
    }

    public FileCopyException(String message, Throwable cause) {
        super(message, cause);
    }

}
