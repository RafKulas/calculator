package dev.rkulik.exception;

public class UnsupportedCharacterException extends RuntimeException {
    public UnsupportedCharacterException(char character) {
        super("Unsupported character appearance: %s".formatted(character));
    }
}
