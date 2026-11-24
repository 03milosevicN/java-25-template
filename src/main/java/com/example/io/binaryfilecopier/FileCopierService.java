package com.example.io.binaryfilecopier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopierService implements FileCopier {
    
    //?: try varying buffer sizes: 8kB, 64kB, 1mB, ...
    private static final int BYTE_BUFFER_SIZE = 8192; // 8kB
    
    private final String CWD = new File("").getAbsolutePath();


    /**
     * Implements cross-concern of creating file because of faulty FileManagerService API.
     */
    public void copyProto(String sourceName, String destName) throws IOException {
        
        File src = new File("%s/%s".formatted(CWD, sourceName));
        if (!src.exists()) {
            try {
                boolean created = src.createNewFile();
                if (created) System.out.println("INFO: Created file from given source name.");
            } catch (IOException e) { System.out.println("ERR: IOException when creating file: %s".formatted(e.getMessage())); }
        }

        File dest = new File("%s/%s".formatted(CWD, destName));
        if (!dest.exists()) {
            try {
                boolean created = dest.createNewFile();
                if (created) System.out.println("INFO: Created file from given destination name.");
            } catch (IOException e) { System.out.println("ERR: IOException when creating file: %s".formatted(e.getMessage())); }
        }

        try (
            FileInputStream input = new FileInputStream(src);
            FileOutputStream output = new FileOutputStream(dest);
        ) {
            
            if (!src.isFile() || src == null) {
                throw new FileCopyException("ERR: Provided source '%s' is not a file or does not exist.".formatted(src.getName()));
            }
            if (src.equals(dest)) {
                throw new FileCopyException("ERR: Provided source '%s' matches the destination '%s'.".formatted(src.getName(), dest.getName()));
            }

            byte[] buffer = new byte[BYTE_BUFFER_SIZE];

            System.out.println("INFO: Reading bytes into buffer and copying onto destination.");
            long START = System.nanoTime();
            int bytesRead;
            while ( (bytesRead = input.read(buffer)) != -1 ) {
                output.write(buffer, 0, bytesRead);
            }
            long END = System.nanoTime();
            long DURATION = END - START;
            System.out.println("INFO: Finished. Operation took %d ns".formatted(DURATION));

        }

    }
 
    @Override
    public void copy(File source, File destination) throws IOException {
        try (
            FileInputStream input = new FileInputStream(source);
            FileOutputStream output = new FileOutputStream(destination);
        ) {            
            if (!source.isFile() || source == null) {
                throw new FileCopyException("ERR: Provided source '%s' is not a file or does not exist.".formatted(source));
            }
            if (source.equals(destination)) {
                throw new FileCopyException("ERR: Provided source '%s' matches the destination '%s'.".formatted(source, destination));
            }

            byte[] buffer = new byte[BYTE_BUFFER_SIZE];
            int bytesRead;

            System.out.println("INFO: Reading bytes into buffer and copying onto destination.");
            long start = System.nanoTime();
            while ((bytesRead = input.read(buffer)) != -1 ) {
                output.write(buffer, 0, bytesRead);
            }
            long end = System.nanoTime();
            long duration = end - start;
            System.out.println("INFO: Finished. Operation took %d ns".formatted(duration));

        }
    }
    
}
