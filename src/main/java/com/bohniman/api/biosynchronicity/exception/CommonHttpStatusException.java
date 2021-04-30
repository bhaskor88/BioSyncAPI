package com.bohniman.api.biosynchronicity.exception;

import javax.servlet.http.HttpServletResponse;

import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class CommonHttpStatusException {

    // @ExceptionHandler(NoHandlerFoundException.class) // HTTP 400 Exception
    // public ResponseEntity<JsonResponse> noHandlerFoundException(HttpServletResponse response,
    //         final NoHandlerFoundException e) {
    //     return new ResponseEntity<JsonResponse>(new JsonResponse(false, null, "No Handler Found"),
    //             HttpStatus.NOT_FOUND);
    // }
}
