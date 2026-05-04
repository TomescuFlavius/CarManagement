package app.appUsers.service;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Email sau parola invalida");
    }
}
