package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.persistence.CustomerDao;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Validator used to validate customer related objects.
 */
@Component
public class CustomerValidator {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


  private final CustomerDao customerDao;

  public CustomerValidator(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  /**
   * Method used to validate update dtos of customers.
   *
   * @param customer Dto to check
   * @throws ValidationException Exception occurs if validation errors are found
   * @throws ConflictException   Exception occurs if conflict errors are found
   */
  public void validateForUpdate(CustomerUpdateDto customer) throws ValidationException, ConflictException {
    LOG.trace("validateForUpdate({})", customer);
    List<String> validationErrors = new ArrayList<>();

    if (customer.id() == null) {
      validationErrors.add("No ID given");
    }

    // TODO Validation is not complete…

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of customer for update failed", validationErrors);
    }

    List<String> conflictErrors = new ArrayList<>();
    // TODO got to check for conflicts with related data too.

    if (!conflictErrors.isEmpty()) {
      throw new ConflictException("Update for customer contains conflicts", conflictErrors);
    }
  }

}
