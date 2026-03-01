package org.catools.mcp.exceptions;

import java.io.File;

/** Signals that an attempt to open the file denoted by a specified pathname has failed. */
public class CFileNotFoundException extends RuntimeException {
  public CFileNotFoundException(File file, String message) {
    this(file.getPath(), message);
  }

  public CFileNotFoundException(String filename, String message) {
    super(String.format("File %s not found. message: %s", filename, message));
  }
}
