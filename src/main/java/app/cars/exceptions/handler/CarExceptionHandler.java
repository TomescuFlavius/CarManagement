package app.cars.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import app.cars.exceptions.CarAlreadyExistException;
import app.cars.exceptions.CarNotFoundException;
import org.springframework.web.client.HttpClientErrorException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        ApiErrorResponse errorResponse=new ApiErrorResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException authenticationException){
        ApiErrorResponse errorResponse=new ApiErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), authenticationException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(MethodArgumentNotValidException exception){
       ApiErrorResponse apiErrorResponse=new ApiErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage());
       return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }

}
