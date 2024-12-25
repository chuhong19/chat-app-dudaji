package vn.giabaochatapp.giabaochatappserver.config.exception;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String message) {
        super(message);
    }
}
