package antony.lappa.inspectrum.controller.exceptionhandler;


import antony.lappa.inspectrum.controller.dto.ErrorDto;
import antony.lappa.inspectrum.exception.InvalidCredentialsException;
import antony.lappa.inspectrum.exception.UserAlreadyExistException;
import antony.lappa.inspectrum.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {

        ErrorDto errorDto = new ErrorDto(e.getMessage());

        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorDto> handleUserAlreadyExistException(UserAlreadyExistException e) {

        ErrorDto errorDto = new ErrorDto(e.getMessage());

        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDto> handleInvalidCredentialsException(InvalidCredentialsException e) {

        ErrorDto errorDto = new ErrorDto(e.getMessage());

        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleServerErrorException(Exception e) {

        ErrorDto errorDto = new ErrorDto(e.getMessage());

        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
