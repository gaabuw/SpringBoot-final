package com.utn.productos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 1. Manejador para ProductoNotFoundException
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductoNotFound(ProductoNotFoundException ex, WebRequest request){

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), // 404
                HttpStatus.NOT_FOUND.getReasonPhrase(), // "Not Found"
                ex.getMessage(), // El mensaje que pasamos (ej: "Producto no encontrado")
                request.getDescription(false) // La URL
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    // 2. Manejador para MethodArgumentNotValidException (Error 400 - Validaciones)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        // Obtenemos todos los mensajes de error de las validaciones y los unimos
        String messages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), // 400
                HttpStatus.BAD_REQUEST.getReasonPhrase(), // "Bad Request"
                messages, // Los mensajes de validación
                request.getDescription(false) // La URL
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 3. Manejador genérico para cualquier otra excepción (Error 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), // "Internal Server Error"
                "Ocurrió un error inesperado: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
