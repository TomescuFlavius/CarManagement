package app.cars.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import app.cars.exceptions.CarAlreadyExistException;
import app.cars.exceptions.CarNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CarExceptionHandler {

    @ExceptionHandler(CarAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handleCarAlreadyExist(CarAlreadyExistException carAlreadyExistException){
        ApiErrorResponse errorResponse=new ApiErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), carAlreadyExistException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCarNotFound(CarNotFoundException carNotFoundException){
        ApiErrorResponse errorResponse=new ApiErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), carNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

}
