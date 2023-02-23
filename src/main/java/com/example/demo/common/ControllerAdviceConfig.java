package com.example.demo.common;

import com.example.demo.exception.OptionalObjectNullException;
import com.example.demo.model.CommonErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class ControllerAdviceConfig {

    @ExceptionHandler(OptionalObjectNullException.class)
    protected ResponseEntity<CommonErrorResponse> handleOptionalObjectNullException(OptionalObjectNullException e, WebRequest webRequest) {
        log.error("OptionalObjectNullException", e);
        Object body = webRequest.getAttribute("body", RequestAttributes.SCOPE_REQUEST);
        if (body != null) {
            log.info("handleOptionalObjectNullException param={}", body);
        }
        CommonErrorResponse errorResponse = new CommonErrorResponse("Parameter error");
        errorResponse.setResultCode(99);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
