package app.cars.exceptions.handler;

import app.appUsers.service.InvalidCredentialsException;
import app.appUsers.service.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import app.cars.exceptions.CarAlreadyExistException;
import app.cars.exceptions.CarNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
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
        ApiErrorResponse errorResponse=new ApiErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), carNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AuthorizationDeniedException accessDeniedException){
        ApiErrorResponse errorResponse=new ApiErrorResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), "Nu ai permisiune pentru a accesa");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception){
       ApiErrorResponse apiErrorResponse=new ApiErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation failed");
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJson(HttpMessageNotReadableException exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Request body is missing or malformed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }
}
