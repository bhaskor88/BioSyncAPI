package com.bohniman.api.biosynchronicity.exception;

import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<JsonResponse> fileStorageException(final FileStorageException e) {
        return new ResponseEntity<JsonResponse>(new JsonResponse(false, null, "Error Saving File "),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MyFileNotFoundException.class)
    public ResponseEntity<JsonResponse> myFileNotFoundException(final MyFileNotFoundException e) {
        return new ResponseEntity<JsonResponse>(new JsonResponse(false, null, "File Not Found "), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<JsonResponse> badRequestException(final BadRequestException e) {
        return new ResponseEntity<JsonResponse>(new JsonResponse(false, null, "Bad Request: "+e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class) // This will catch ConstraintViolationException.class
    public ResponseEntity<JsonResponse> dataIntegrityViolationException(final DataIntegrityViolationException e) {
        return new ResponseEntity<JsonResponse>(
                new JsonResponse(false, null, "Constrain Violation : " + e.getCause().getCause().getLocalizedMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResponse> javaLangException(final Exception e) {
        return new ResponseEntity<JsonResponse>(new JsonResponse(false, null, "Exception : " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
