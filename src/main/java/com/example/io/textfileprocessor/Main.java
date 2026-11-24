package com.example.io.textfileprocessor;

import java.io.IOException;

public class Main {

    public static FileServiceImpl fs = new FileServiceImpl();
    public static BufferedFileServiceImpl bfs = new BufferedFileServiceImpl();

    public static void main(String[] args) {
        try {
            bfs.writeFile("./example.txt", "Appending text!!\n", false);
        } catch (IOException e) {
            System.err.println("IOEXception: %s".formatted(e.getMessage()));
        }
    }

}
