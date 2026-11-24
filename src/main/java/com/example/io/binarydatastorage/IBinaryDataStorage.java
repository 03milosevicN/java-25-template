package com.example.io.binarydatastorage;

import java.io.IOException;

public interface IBinaryDataStorage {
    
    /**
     * @param src File path
     * @param students List of students
     * @param appendable append to existing file
     * @throws IOException
     */
    void writeStudents(String dest, Student[] students, boolean appendable) throws IOException;

    /**
     * @param src File path
     * @param student Student record
     * @param appendable append to existing file
     * @throws IOException
     */
    void writeStudent(String dest, Student student, boolean appendable) throws IOException;

    /**
     * @param id Reads 1..* students by id
     * @throws IOException
     */
    void readData(String src) throws IOException;
    
}
