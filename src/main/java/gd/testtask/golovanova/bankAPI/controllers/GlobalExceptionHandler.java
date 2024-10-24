package gd.testtask.golovanova.bankAPI.controllers;

import gd.testtask.golovanova.bankAPI.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Banks errors handler

    @ExceptionHandler(BankNotFoundException.class)
    private ResponseEntity<BankErrorResponse> handleException(BankNotCreatedException e) {
        BankErrorResponse response = new BankErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BankNotUpdatedException.class)
    private ResponseEntity<BankErrorResponse> handleException(BankNotFoundException e) {
        BankErrorResponse response = new BankErrorResponse(
                "Bank not found", System.currentTimeMillis()
        );
        // http response + 404 status
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BankNotUpdatedException.class)
    private ResponseEntity<BankErrorResponse> handleException(BankNotUpdatedException e) {
        BankErrorResponse response = new BankErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Clients errors handler

    @ExceptionHandler(ClientNotFoundException.class)
    private ResponseEntity<ClientErrorResponse> handleException(ClientNotCreatedException e) {
        ClientErrorResponse response = new ClientErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientNotUpdatedException.class)
    private ResponseEntity<ClientErrorResponse> handleException(ClientNotFoundException e) {
        ClientErrorResponse response = new ClientErrorResponse(
                "Bank not found", System.currentTimeMillis()
        );
        // http response + 404 status
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClientNotUpdatedException.class)
    private ResponseEntity<ClientErrorResponse> handleException(ClientNotUpdatedException e) {
        ClientErrorResponse response = new ClientErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Deposits errors handler

    @ExceptionHandler(DepositNotCreatedException.class)
    private ResponseEntity<DepositErrorResponse> handleException(DepositNotCreatedException e) {
        DepositErrorResponse response = new DepositErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DepositNotFoundException.class)
    private ResponseEntity<DepositErrorResponse> handleException(DepositNotFoundException e) {
        DepositErrorResponse response = new DepositErrorResponse(
                "Deposit not found", System.currentTimeMillis()
        );
        // http response + 404 status
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepositNotUpdatedException.class)
    private ResponseEntity<DepositErrorResponse> handleException(DepositNotUpdatedException e) {
        DepositErrorResponse response = new DepositErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Validation errors handler

    public static void handleValidationErrors(BindingResult bindingResult, Class<? extends RuntimeException> exceptionClass) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            try {
                throw exceptionClass.getConstructor(String.class).newInstance(errorMessage.toString());
            } catch (Exception e) {
                throw new RuntimeException("Error while handling validation", e);
            }
        }
    }
}