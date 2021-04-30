package com.bohniman.api.biosynchronicity.exception;

import javax.servlet.http.HttpServletResponse;

import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class) // This will catch ConstraintViolationException.class
    public ResponseEntity<JsonResponse> dataIntegrityViolationException(HttpServletResponse response,
            final DataIntegrityViolationException e) {
        return new ResponseEntity<JsonResponse>(
                new JsonResponse(false, null, "Constrain Violation : " + e.getCause().getCause().getLocalizedMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResponse> javaLangException(HttpServletResponse response, final Exception e) {
        return new ResponseEntity<JsonResponse>(new JsonResponse(false, null, "Exception : " + e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

}
