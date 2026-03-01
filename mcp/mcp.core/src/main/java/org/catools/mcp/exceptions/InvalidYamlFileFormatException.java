package org.catools.mcp.exceptions;

import java.io.File;

/**
 * Signals that attempt to convert file content to yaml information failed.
 */
public class InvalidYamlFileFormatException extends RuntimeException {
    private static final String HELP = "\nTo read yaml to object you need to ensure that your yaml file has correct file format.";

    public InvalidYamlFileFormatException(String fileName) {
        super("Yaml file does not have correct format. Filename: " + fileName + HELP);
    }

    public InvalidYamlFileFormatException(String fileName, Throwable t) {
        super("Yaml file does not have correct format. Filename: " + fileName + HELP, t);
    }

    public InvalidYamlFileFormatException(File file) {
        this(file.getPath());
    }
}
