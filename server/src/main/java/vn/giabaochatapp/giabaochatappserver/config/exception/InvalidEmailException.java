package vn.giabaochatapp.giabaochatappserver.config.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }

}
