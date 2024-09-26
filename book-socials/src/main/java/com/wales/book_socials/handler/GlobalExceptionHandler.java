package com.wales.book_socials.handler;

import com.wales.book_socials.exception.OperationNotPermittedException;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.wales.book_socials.handler.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException lockedException) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
                        .error(lockedException.getMessage())
                        .build());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException disabledException) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
                        .error(disabledException.getMessage())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException badCredentialsException) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getCode())
                        .businessErrorDescription(BAD_CREDENTIALS.getDescription())
                        .error(BAD_CREDENTIALS.getDescription())
                        .build());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException messagingException) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .error(messagingException.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException messagingException) {
        Set<String> errors = new HashSet<>();
        messagingException.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMsg = error.getDefaultMessage();
                    errors.add(errorMsg);
                });
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        // log the error
        e.printStackTrace();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Internal error, please contact the admin")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .error(e.getMessage())
                        .build());
    }


}
