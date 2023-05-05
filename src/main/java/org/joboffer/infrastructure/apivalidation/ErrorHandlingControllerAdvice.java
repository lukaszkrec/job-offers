package org.joboffer.infrastructure.apivalidation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@ControllerAdvice
@AllArgsConstructor
public class ErrorHandlingControllerAdvice {

    private final Clock clock;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        List<String> propertyPath = constraintViolationException.getConstraintViolations()
                .stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
                .toList();

        return ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(String.format("Validation failed for object='%s'"))
                .timestamp(OffsetDateTime.now(clock))
                .violations(List.of(Violation.builder()
                        .constraintPropertyPath(propertyPath)
                        .build()))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<Violation> violations = methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .stream().map(fieldError -> Violation.builder()
                        .message(fieldError.getDefaultMessage())
                        .property(fieldError.getField())
                        .rejectedValue(String.valueOf(fieldError.getRejectedValue()))
                        .build())
                .toList();

        return ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(String.format("Validation failed for object='%s'. Errors count: %d", methodArgumentNotValidException.getObjectName(), methodArgumentNotValidException.getFieldErrors().stream().count()))
                .timestamp(OffsetDateTime.now(clock))
                .violations(violations)
                .build();
    }
}
