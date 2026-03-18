package app.exceptions;

import static app.constants.ExceptionsConstants.CAR_NOT_FOUND;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException() {
        super(CAR_NOT_FOUND);
    }
}
