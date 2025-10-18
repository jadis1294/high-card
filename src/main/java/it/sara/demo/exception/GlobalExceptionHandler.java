package it.sara.demo.exception;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.web.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<GenericResponse> handleGenericException(GenericException ex) {
        GenericResponse response = new GenericResponse();
        response.setStatus(ex.getStatus());
        return ResponseEntity.ok(response); // HTTP 200 anche per errori
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleAllExceptions(Exception ex) {
        StatusDTO status = new StatusDTO();
        status.setCode(500);
        status.setMessage("Errore interno del server");
        status.setTraceId(java.util.UUID.randomUUID().toString());

        GenericResponse response = new GenericResponse();
        response.setStatus(status);
        return ResponseEntity.ok(response);
    }
}
