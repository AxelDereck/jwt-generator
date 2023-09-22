package org.beenoo.exception;

public class GenerationDataParsingException extends RuntimeException {
    public GenerationDataParsingException(Exception exception) {
        super(exception.getMessage());
    }
}
