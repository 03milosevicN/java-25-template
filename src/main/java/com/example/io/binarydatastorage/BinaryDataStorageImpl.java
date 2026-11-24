package com.example.io.binarydatastorage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

record Student(int id,
        String name,
        double average,
        boolean active) {

}

public class BinaryDataStorageImpl implements IBinaryDataStorage {

    private final String PATH_REGEX = "^(?:~|/|\\.{1,2}/)(?:[^\\s/]+/)*[^\\s/]+$";
    private final String CWD = new File("").getAbsolutePath();

    @Override
    public void writeStudents(String dest, Student[] students, boolean appendable) throws IOException {

        if (!Pattern.matches(PATH_REGEX, dest)) {
            throw new IOException("ERR: %s isn't a valid file name or path.".formatted(dest));
        }

        File destFile = new File("%s/%s".formatted(CWD, dest));
        System.out.println("INFO: Writing binary data to %s.".formatted(destFile.getName()));
        long START = System.nanoTime();
        for (var student : students) {
            if (appendable) {
                try (DataOutputStream out = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(destFile, true)))) {
                    out.writeInt(student.id());
                    out.writeUTF(student.name());
                    out.writeDouble(student.average());
                    out.writeBoolean(student.active());
                }
            } else {
                try (DataOutputStream out = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(destFile)))) {
                    out.writeInt(student.id());
                    out.writeUTF(student.name());
                    out.writeDouble(student.average());
                    out.writeBoolean(student.active());
                }
            }
        }
        long END = System.nanoTime();
        long DURATION = END - START;
        System.out.println("INFO: Finished. Operation took %d ns.".formatted(DURATION));
        System.out.println("INFO: Contents: ");
        for (var s : students) System.out.println(s.toString());

    }

    @Override
    public void writeStudent(String dest, Student student, boolean appendable) throws IOException {

        if (!Pattern.matches(PATH_REGEX, dest)) {
            throw new IOException("ERR: %s isn't a valid file name or path.".formatted(dest));
        }

        File destFile = new File("%s/%s".formatted(CWD, dest));
        System.out.println("INFO: Writing binary data to %s.".formatted(destFile.getName()));
        long START = System.nanoTime();
        if (appendable) {
            try (DataOutputStream out = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(destFile, true)))) {
                out.writeInt(student.id());
                out.writeUTF(student.name());
                out.writeDouble(student.average());
                out.writeBoolean(student.active());
            }
        } else {
            try (DataOutputStream out = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(destFile)))) {
                out.writeInt(student.id());
                out.writeUTF(student.name());
                out.writeDouble(student.average());
                out.writeBoolean(student.active());
            }
        }
        long END = System.nanoTime();
        long DURATION = END - START;
        System.out.println("INFO: Finished. Operation took %d ns.".formatted(DURATION));
        System.out.println("INFO: Contents: %s".formatted(student.toString()));

    }

    @Override
    public void readData(String src) throws IOException {
        File srcFile = new File("%s/%s".formatted(CWD, src));

        long START = System.nanoTime();
        try (var in = new DataInputStream(new BufferedInputStream(new FileInputStream(srcFile)))) {
            int id = in.readInt();
            String name = in.readUTF();
            double avg = in.readDouble();
            boolean active = in.readBoolean();

            var s = new Student(id, name, avg, active);
            System.out.println(s.toString());
        }
        long END = System.nanoTime();
        long DURATION = END - START;
        System.out.println("INFO: Finished. Operation took %d ns.".formatted(DURATION));
    }

}
