package com.example.io.filemanager;

import java.io.File;
import java.io.IOException;

public class FileManagerService {

    private final String cwd = new File("").getAbsolutePath();

    
    public void createFile(String name) {
        File file = new File("%s/%s.txt".formatted(cwd, name));
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    System.out.println(String.format("Created file '%s'", name));
                }
            } catch (IOException e) {
                System.out.println("IOException when creating file: %s".formatted(e.getMessage()));
            }
        }
    }

    public void createDir(String name) {
        File dir = new File("%s/%s".formatted(cwd, name));
        if (!dir.exists()) {
            boolean created = dir.mkdir();
            if (created) {
                System.out.println("Created file '%s'".formatted(name));
            } else {
                System.out.println("Failed to create directory '%s'".formatted(name));
            }
        }
    }

    public void listFiles(String dirName) throws IOException {
        File dir = new File("%s/%s".formatted(cwd, dirName));
        if (!dir.exists()) {
            throw new IOException("IOexception when listing directory content. No such directory.");
        }
        System.out.println("Listing contents: ".formatted(dirName));
        for (File file : dir.listFiles()) {
            System.out.println(file);
        }
    }

    public void renameFile(String name, String newName) throws IOException {
        File currFile = new File("%s/%s".formatted(cwd, name));
        if (!currFile.exists()) {
            throw new IOException("File cannot be renamed. Doesn't exist");
        }
        File newFile = new File("%s/%s".formatted(cwd, newName));
        boolean renamed = currFile.renameTo(newFile);
        if (!renamed) {
            throw new IOException("Failed to rename file");
        }
        System.out.println("Changed %s to %s".formatted(name, newName));
    }

    public void deleteFile(String name) throws IOException {
        File currFile = new File("%s/%s".formatted(cwd, name));
        if (!currFile.exists()) {
            throw new IOException("File cannot be deleted. Doesn't exist.");
        }
        boolean deleted = currFile.delete();
        if (!deleted) {
            throw new IOException("Failed to delete file");
        }
        System.out.println(String.format("Deleted %s", name));
    }

    public void fileInfo(String name) throws IOException {
        File currFile = new File("%s/%s".formatted(cwd, name));
        if (currFile.isFile()) {
            System.out.println(String.format("%s is a file.", currFile.getName()));
        } else if (currFile.isDirectory()) {
            System.out.println(String.format("%s is a directory.", currFile.getName()));
        } else {
            throw new IOException("File information cannot be gathered. Doesn't exist.");
        }
    }

}
