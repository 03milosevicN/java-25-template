package com.example.io.textfileprocessor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class FileServiceImpl implements IFileService {

    private final String PATH_REGEX = "^(?:~|/|\\.{1,2}/)(?:[^\\s/]+/)*[^\\s/]+$";
    private final String CWD = new File("").getAbsolutePath();
    

    @Override
    public void readFile(String src) throws IOException {

        if (!Pattern.matches(PATH_REGEX, src)) {
            throw new IOException("ERR: %s isn't a valid file name or path.".formatted(src));
        }

        File inputFile = new File("%s/%s".formatted(CWD, src));

        if (inputFile.length() == 0) {
            throw new IOException("ERR: %s is empty.".formatted(src));
        }

        System.out.println("INFO: Reading charaters from %s".formatted(src));
        long START = System.nanoTime();
        try (FileReader reader = new FileReader(src)) {
            int c;
            while ((c = reader.read()) != -1) {
                System.out.print("%s\n".formatted((char) c));
            }
        }
        long END = System.nanoTime();
        long DURATION = END - START;
        System.out.println("INFO: Finished. Operation took %d ns".formatted(DURATION));
        
    }

    @Override
    public void writeFile(String dest, String text, boolean appendable) throws IOException {

        File inputFile = new File("%s/%s".formatted(CWD, dest));

        if (!Pattern.matches(PATH_REGEX, dest)) {
            throw new IOException("ERR: %s is not a valid file name or path.".formatted(dest));
        }
        if (inputFile.isDirectory()) {
            throw new IOException("ERR: %s is a directory path.".formatted(inputFile));
        }

        System.out.println("INFO: Writing charaters to %s".formatted(dest));
        long START = System.nanoTime();
        if (appendable) {
            try (FileWriter writer = new FileWriter(dest, true)) {
                writer.write(text);
            }
        } else {
            try (FileWriter writer = new FileWriter(dest)) {
                writer.write(text);
            }
        }
        long END = System.nanoTime();
        long DURATION = END - START;
        System.out.println("INFO: Finished. Operation took %d ns.".formatted(DURATION));

    }

}
