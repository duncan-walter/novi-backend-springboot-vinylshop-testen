package walter.duncan.vinylwebshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
        ProblemDetail problemDetail = this.createProblemDetailsBase(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("urn:vinyl-webshop:api:problem:resource:not-found"));
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ProblemDetail> uncaughtExceptionHandler(RuntimeException exception) {
        // TODO: Log exception stacktrace and other details somewhere so that the application can be improved upon.

        ProblemDetail problemDetail = this.createProblemDetailsBase(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setType(URI.create("urn:vinyl-webshop:api:problem:internal-server-error"));
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("An internal server error occurred while processing your request. Please contact support if the issue persists.");

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    private ProblemDetail createProblemDetailsBase(HttpStatus httpStatus) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}