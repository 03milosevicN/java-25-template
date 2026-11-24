package com.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class App {

    public static void byteStreamInput() {
        try (FileInputStream input = new FileInputStream("example.txt")) {
            int data;
            while ((data = input.read()) != -1) {
                System.out.println(data);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void characterStreamReader() {
        try ( BufferedReader reader = new BufferedReader(new FileReader("example.txt")) ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        characterStreamReader();
    }

}