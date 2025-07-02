package dev.rkulik.exception;

public class MissingBracketException extends RuntimeException {
    public MissingBracketException() {
        super("Missing ')'");
    }
}
