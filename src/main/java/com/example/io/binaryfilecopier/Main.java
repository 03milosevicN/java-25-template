package com.example.io.binaryfilecopier;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        FileCopierService bfc = new FileCopierService();
        try {
            bfc.copyProto("source.txt", "dest.txt");
        } catch (IOException e) {
            System.out.println("IOException: %s".formatted(e.getMessage()));
        }

    }

}
