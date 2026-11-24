package com.example.io.textfileprocessor;

import java.io.IOException;

public interface IBufferedFileService {
    
    /**     
     * Reads file from given source, outputs text.
     * @param input File path or file name 
     */
    void readFile(String src) throws IOException;

    /**
     * Write file to given destination.
     * @param dest File name
     * @param appendable Append text to existing destination
     */
    void writeFile(String dest, String text, boolean appendable) throws IOException;

}
