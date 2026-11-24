package com.example.io.binaryfilecopier;

import java.io.File;
import java.io.IOException;

public interface FileCopier {
    void copy(File source, File destination) throws IOException;
}
