package ro.mycode.carmanagementsystem.exceptions;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException() {
        super("CarNotFoundException");
    }
}
