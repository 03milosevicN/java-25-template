package com.example.nio.niofilemanager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

public class NioFileManagerService {

    /**
     * Create a file.
     * @param name File name
     */
    public void createFile(String name) {
        if (Files.exists(Path.of(name))) {
            System.out.println("File %s already exists.".formatted(name));
        }
        try {
            System.out.println("Created file: %s".formatted(Path.of(name).getFileName()));
            Files.createFile(Path.of(name));
        } catch (IOException e) {
            System.err.println("IOException caught when making file: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Copies source file to given destination.
     * @param srcName Source file name
     * @param destName Destination file name
     * @throws IOException
     */
    public void copyFile(String srcName, String destName) throws IOException {
        Path src = Path.of(srcName);
        if (!Files.exists(src)) {
            throw new IOException("Source file doesn't exist.");
        }
        Path dest = Path.of(destName);
        if (!Files.exists(dest)) { 
            System.out.println("INFO: %s does not exist but will be created.".formatted(dest.getFileName()));
            Files.createFile(dest); 
        }
        System.out.println("INFO: Copying %s into %s.".formatted(src.getFileName(), dest.getFileName()));
        Files.copy(src, dest);
    }

    /**
     * Moves source file to given destination.
     * @param srcName Source file name
     * @param destName Destination file name
     * @param replaceExisting StandardCopyOption.REPLACE_EXISTING flag
     * @throws IOException
     */
    public void moveFile(String srcName, String destName, boolean replaceExisting) throws IOException {
        Path src = Path.of(srcName);
        if (!Files.exists(src)) {
            throw new IOException("Source file doesn't exist.");
        }
        Path dest = Path.of(destName);
        if (!Files.exists(dest)) {
            System.out.println("INFO: %s does not exist but will be created.".formatted(dest.getFileName()));
            Files.createFile(dest); 
        }
        System.out.println("INFO: Moving %s data into %s.".formatted(src.getFileName(), dest.getFileName()));
        if (replaceExisting) {
            System.out.println("Replacing existing destination");
            Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.move(src, dest);
        }
    }   

    /**
     * Delete file.
     * @throws IOException
     */
    public void deleteFile(String fileName) throws IOException {
        Path file = Path.of(fileName);
        if (!Files.exists(file)) {
            throw new IOException("Source file doesn't exist.");
        }
        System.out.println("INFO: Deleting %s.".formatted(file.getFileName()));
        Files.delete(file);
    }

    /**
     * Write or append text into file.
     * @param fileName Source file name
     * @param text Text to write
     * @param appendable StandardOpenOption.APPEND flag
     */
    public void writeToFile(String fileName, String text, boolean appendable) {
        try {
            if (appendable) {
                System.out.println("Appending text to %s".formatted(fileName));
                Files.writeString(Path.of(fileName), text, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            } else {
                System.out.println("Writing contents to %s".formatted(fileName));
                Files.writeString(Path.of(fileName), text, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            System.err.println("IOException caught when making file: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Reading file contents.
     * @param fileName Source file name
     */
    public void readFile(String fileName) {
        System.out.println("Reading contents from %s".formatted(fileName));
        try {
            List<String> lines = Files.readAllLines(Path.of(fileName), StandardCharsets.UTF_8);
            for (var line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("IOException caught when reading file contents: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Create a directory.
     * @param name Directory name
     */
    public void createDir(String name) {
        if (Files.exists(Path.of(name))) {
            System.out.println("File %s already exists.".formatted(name));
        }
        try {
            System.out.println("Created dir: %s".formatted(Path.of(name).getFileName()));
            Files.createDirectory(Path.of(name));
        } catch (IOException e) {
            System.err.println("IOException caught when making dir: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Listing contents of given directory name.
     * @param dirName Directory name
     * @throws IOException
     */
    public void lsDir(String dirName) throws IOException {
        if (!Files.exists(Path.of(dirName))) {
            throw new IOException("Source directory doesn't exist.");
        }
        if (!Files.isDirectory(Path.of(dirName))) {
            throw new IOException("Source isn't a directory.");
        }
        System.out.println("Listing contents of %s".formatted(dirName));
        try (Stream<Path> paths = Files.list(Path.of(dirName))) {
            paths.forEach(System.out::println);
        }
    }

}
