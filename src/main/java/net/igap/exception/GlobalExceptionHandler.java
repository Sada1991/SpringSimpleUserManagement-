package net.igap.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.igap.model.dtos.ErrorDetailsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorDetailsDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                      WebRequest webRequest){

    ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), exception.getMessage(),
        webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetailsDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(APIException.class)
  public ResponseEntity<ErrorDetailsDto> handleAPIException(APIException exception,
      WebRequest webRequest){

    ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), exception.getMessage(),
        webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetailsDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
      WrongCityException.class,
      WrongProvinceException.class,
      NationalCodeException.class
  })
  public ResponseEntity<ErrorDetailsDto> handleWrongCityException(RuntimeException exception,
      WebRequest webRequest){

    ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), exception.getMessage(),
        webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetailsDto, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetailsDto> handleGlobalException(Exception exception,
      WebRequest webRequest){

    ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(new Date(), exception.getMessage(),
        webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetailsDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDetailsDto> accessDeniedException(AccessDeniedException ex, WebRequest request) {

    ErrorDetailsDto errorDetails = new ErrorDetailsDto(new Date(), ex.getMessage(),
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorDetailsDto> badCredentialsException(BadCredentialsException ex, WebRequest request) {
    ErrorDetailsDto errorDetails = new ErrorDetailsDto(new Date(), ex.getMessage(),
        request.getDescription(false));

    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error ->{
      String fieldName = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      errors.put(fieldName,message);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

}
