package com.visma.kalmar.api.exception;

import com.visma.kalmar.api.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> handleResourceNotFound(ResourceNotFoundException ex) {
        Error error = new Error();
        error.setCode(ex.getResourceType());
        error.setMessage(ex.getMessage());
        error.setMessageParameters(ex.getMessageParameters());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Error> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        Error error = new Error();
        error.setCode(ex.getResourceType());
        error.setMessage(ex.getMessage());
        error.setMessageParameters(ex.getMessageParameters());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidInputDataException.class)
    public ResponseEntity<Error> handleInvalidInputData(InvalidInputDataException ex) {
        Error error = new Error();
        error.setCode(ex.getResourceType());
        error.setMessage(ex.getMessage());
        error.setMessageParameters(ex.getMessageParameters());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Error> handleExternalServiceException(ExternalServiceException ex) {
        Error error = new Error();
        error.setCode(ex.getServiceName());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGenericException(Exception ex) {
        Error error = new Error();
        error.setCode("INTERNAL_ERROR");
        error.setMessage("An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
