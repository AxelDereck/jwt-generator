package org.beenoo.jwt.generator.exception;

public class GenerationDataParsingException extends RuntimeException {
    public GenerationDataParsingException(Exception exception) {
        super(exception.getMessage());
    }
}
