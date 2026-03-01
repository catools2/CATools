package org.catools.mcp.exceptions;

import java.io.File;

/**
 * Signals that an attempt to open the file denoted by a specified pathname has failed.
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(File file, String message) {
        this(file.getPath(), message);
    }

    public FileNotFoundException(String filename, String message) {
        super(String.format("File %s not found. message: %s", filename, message));
    }
}
