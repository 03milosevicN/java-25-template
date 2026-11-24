package com.example.io.binarydatastorage;

import java.io.IOException;

public class Main {

    public static BinaryDataStorageImpl bds = new BinaryDataStorageImpl();

    public static void main(String[] args) {

        Student student1 = new Student(1, "John Binary", 4.00, true);
        Student student2 = new Student(2, "Frank Binary", 2.50, true);
        Student[] students = { student1, student2 };

        try {
            bds.writeStudent("./student.dat", student1, false);
            bds.writeStudents("./students.dat", students, false);
        } catch (IOException e) {
            System.err.println("IOEXception: %s".formatted(e.getMessage()));
        }
        try {
            bds.readData("./students.dat");
        } catch (IOException e) {
            System.err.println("IOEXception: %s".formatted(e.getMessage()));
        }

    }

}
