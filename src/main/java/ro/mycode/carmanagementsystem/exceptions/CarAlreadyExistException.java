package ro.mycode.carmanagementsystem.exceptions;

public class CarAlreadyExistException extends RuntimeException {
    public CarAlreadyExistException() {
        super("CarAlreadyExistException");
    }
}
