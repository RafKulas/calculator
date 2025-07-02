package dev.rkulik.exception;

public class UnsupportedOperationException extends RuntimeException {
    public UnsupportedOperationException(String character) {
        super("Unsupported operation appearance: %s".formatted(character));
    }
}
