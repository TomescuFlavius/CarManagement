package app.cars.exceptions;

import static app.cars.constants.ExceptionsConstants.CAR_NOT_FOUND;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException() {
        super(CAR_NOT_FOUND);
    }
}
