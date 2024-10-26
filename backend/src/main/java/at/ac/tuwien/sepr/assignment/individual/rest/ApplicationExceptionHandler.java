package at.ac.tuwien.sepr.assignment.individual.rest;

import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Central exception handler used to handle validation and conflict exceptions.
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @ExceptionHandler
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ResponseBody
  public ValidationErrorRestDto handleValidationException(ValidationException e) {
    LOG.warn("Terminating request processing with status 422 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());
    return new ValidationErrorRestDto(e.summary(), e.errors());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  public ConflictErrorRestDto handleConflictException(ConflictException e) {
    LOG.warn("Terminating request processing with status 409 due to {}: {}", e.getClass().getSimpleName(), e.getMessage());
    return new ConflictErrorRestDto(e.summary(), e.errors());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public FatalErrorRestDto handleFatalException(FatalException e) {
    LOG.error("Terminating request processing with status 500 due to {}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
    return new FatalErrorRestDto("An unexpected error occurred. Please try again later.");
  }
}
