package io.github.juliacanalle.creditointernoapi.exceptions;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ColaboradorNotFoundException.class)
    public ResponseEntity<String> handleColaboradorNotFound(ColaboradorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EmpresaNotFoundException.class)
    public ResponseEntity<String> handleEmpresaNotFoundException(EmpresaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<String> handleCpfAlreadyExistsException(CpfAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InactiveColaboradorException.class)
    public ResponseEntity<String> handleInactiveColaboradorException(InactiveColaboradorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(InactiveEmpresaException.class)
    public ResponseEntity<String> handleInactiveEmpresaException(InactiveEmpresaException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(ColaboradorNotInCompanyException.class)
    public ResponseEntity<String> handleColaboradorNotInCompanyException(ColaboradorNotInCompanyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DataRangeExceedLimitException.class)
    public ResponseEntity<String> handleDataRangeExceedLimitException(DataRangeExceedLimitException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MinValueGreaterThanMaxValueException.class)
    public ResponseEntity<String> handleMinValueGreaterThanMaxValueException(MinValueGreaterThanMaxValueException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<String> handleInvalidSortFieldException(InvalidSortFieldException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ContaNotFoundException.class)
    public ResponseEntity<String> handleContaNotFoundException(ContaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}

