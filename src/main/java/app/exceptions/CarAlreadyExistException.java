package app.exceptions;
import static app.constants.ExceptionsConstants.CAR_ALREADY_EXISTS;

public class CarAlreadyExistException extends RuntimeException{
    public CarAlreadyExistException(){
        super(CAR_ALREADY_EXISTS);
    }
}