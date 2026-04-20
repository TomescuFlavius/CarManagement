package app.cars.exceptions;
import static app.cars.constants.ExceptionsConstants.CAR_ALREADY_EXISTS;

public class CarAlreadyExistException extends RuntimeException{
    public CarAlreadyExistException(){
        super(CAR_ALREADY_EXISTS);
    }
}