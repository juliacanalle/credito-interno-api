package io.github.juliacanalle.creditointernoapi.exceptions;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ColaboradorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleColaboradorNotFound(ColaboradorNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmpresaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmpresaNotFoundException(EmpresaNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCpfAlreadyExistsException(CpfAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InactiveColaboradorException.class)
    public ResponseEntity<ErrorResponse> handleInactiveColaboradorException(InactiveColaboradorException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InactiveEmpresaException.class)
    public ResponseEntity<ErrorResponse> handleInactiveEmpresaException(InactiveEmpresaException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ColaboradorNotInCompanyException.class)
    public ResponseEntity<ErrorResponse> handleColaboradorNotInCompanyException(ColaboradorNotInCompanyException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataRangeExceedLimitException.class)
    public ResponseEntity<ErrorResponse> handleDataRangeExceedLimitException(DataRangeExceedLimitException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MinValueGreaterThanMaxValueException.class)
    public ResponseEntity<ErrorResponse> handleMinValueGreaterThanMaxValueException(MinValueGreaterThanMaxValueException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSortFieldException(InvalidSortFieldException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ContaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleContaNotFoundException(ContaNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(AtLeastOneFieldPresentException.class)
    public ResponseEntity<ErrorResponse> handleAtLeastOneFieldPresentException(AtLeastOneFieldPresentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(CepNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCepNotFoundException(CepNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmpresaAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmpresaAlreadyExistsException(EmpresaAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }
}

