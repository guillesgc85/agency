package com.agency.ads.app.handler;

import com.agency.ads.common.constant.ErrorCode;
import com.agency.ads.common.exception.AdsException;
import com.agency.ads.common.response.ErrorResponse;
import com.agency.ads.common.response.ExceptionErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.concurrent.CompletionException;

@ControllerAdvice
public class AdsExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(AdsExceptionHandler.class);

    /**
     * Handles AdsException.
     */
    @ExceptionHandler(AdsException.class)
    public ResponseEntity<ErrorResponse> adsExceptionHandler(AdsException ex) {
        ExceptionErrorResponse exceptionErrorResponse = new ExceptionErrorResponse(ex.getErrorCode(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(exceptionErrorResponse);

        logger.error("--AdsExceptionHandler:validationErrorHandler --code: [{}] --message: [{}]", ex.getErrorCode(), ex.getMessage());
        HttpStatus httpStatus = null;
        if (ex.getStatus() != null) {
            httpStatus = HttpStatus.resolve(ex.getStatus());
        }

        return new ResponseEntity(errorResponse, httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles mismatch or values not allowed issues, when excepted attributes as part of an
     * endpoint. For instance. Let say that we are expecting and enumerated as a pathVariable,
     * header or so... if the received value is not part of the enum set, will throw this exception.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ExceptionErrorResponse exceptionErrorResponse = new ExceptionErrorResponse(
                ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH,
                ex.getCause() != null && ex.getCause().getLocalizedMessage() != null ? ex.getCause().getLocalizedMessage() : ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(exceptionErrorResponse);

        logger.error(
                "--AdsExceptionHandler:handleMethodArgumentTypeMismatchException --code: [{}] --message: [{}]",
                errorResponse.getError().getCode(),
                errorResponse.getError().getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles missing parameter
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ExceptionErrorResponse exceptionErrorResponse = new ExceptionErrorResponse(
                ErrorCode.CODE_BAD_REQUEST,
                ex.getCause() != null && ex.getCause().getLocalizedMessage() != null ? ex.getCause().getLocalizedMessage() : ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(exceptionErrorResponse);
        logger.error(
                "--AdsExceptionHandler:handleMissingServletRequestParameterException --code: [{}] --message: [{}]",
                errorResponse.getError().getCode(),
                errorResponse.getError().getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles case of concurrent http consumptions. When we don't have response from one of them.
     */
    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ErrorResponse> handleCompletionException(CompletionException ex) {
        ExceptionErrorResponse exceptionErrorResponse = new ExceptionErrorResponse(
                ErrorCode.CODE_COMPLETION,
                ex.getCause() != null && ex.getCause().getLocalizedMessage() != null ? ex.getCause().getLocalizedMessage() : ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(exceptionErrorResponse);
        logger.error(
                "--AdsExceptionHandler:handleCompletionException --code: [{}] --message: [{}]",
                errorResponse.getError().getCode(),
                errorResponse.getError().getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleCompletionException(HttpMessageNotReadableException ex) {
        ExceptionErrorResponse exceptionErrorResponse = new ExceptionErrorResponse(
                ErrorCode.REQUIRED_REQUEST_BODY,
                ex.getCause() != null && ex.getCause().getLocalizedMessage() != null ? ex.getCause().getLocalizedMessage() : ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(exceptionErrorResponse);
        logger.error(
                "--AdsExceptionHandler:handleCompletionException --code: [{}] --message: [{}]",
                errorResponse.getError().getCode(),
                errorResponse.getError().getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles @Valid exceptions
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ExceptionErrorResponse exceptionErrorResponse = new ExceptionErrorResponse(
                ErrorCode.CODE_BAD_REQUEST,
                ErrorCode.MSG_BAD_REQUEST
        );
        ErrorResponse errorResponse = new ErrorResponse(exceptionErrorResponse);
        logger.error(
                "--AdsExceptionHandler:handleMethodArgumentNotValidException --code: [{}] --message: [{}]",
                errorResponse.getError().getCode(),
                ex.getCause() != null && ex.getCause().getLocalizedMessage() != null ? ex.getCause().getLocalizedMessage() : ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles any other RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ExceptionErrorResponse exceptionErrorResponse = new ExceptionErrorResponse(
                ErrorCode.CODE_INTERNAL_SERVER_ERROR,
                ex.getCause() != null && ex.getCause().getLocalizedMessage() != null ? ex.getCause().getLocalizedMessage() : ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(exceptionErrorResponse);
        logger.error(
                "--AdsExceptionHandler:handleRuntimeException --code: [{}] --message: [{}]",
                errorResponse.getError().getCode(),
                errorResponse.getError().getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
