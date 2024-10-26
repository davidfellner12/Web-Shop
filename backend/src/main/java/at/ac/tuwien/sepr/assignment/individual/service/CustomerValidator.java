package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.persistence.CustomerDao;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

//TODO: it has to be differentiated between optional and fixed parameters
/**
 * Validator used to validate customer related objects.
 */
//TODO LOG warn
@Component
public class CustomerValidator {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


  private final CustomerDao customerDao;
  private final View error;

  public CustomerValidator(CustomerDao customerDao, View error) {
    this.customerDao = customerDao;
    this.error = error;
  }

  /**
   * Method used to validate update dtos of customers.
   *
   * @param customer Dto to check
   * @throws ValidationException Exception occurs if validation errors are found
   * @throws ConflictException   Exception occurs if conflict errors are found
   */
  public void validateForUpdate(CustomerUpdateDto customer) throws ValidationException, ConflictException, NotFoundException {
    LOG.trace("validateForUpdate({})", customer);

    List<String> validationErrors = new ArrayList<>();
    CustomerCreateDto toCheck = new CustomerCreateDto(
            customer.firstName(),
            customer.lastName(),
            customer.dateOfBirth(),
            customer.email());
    validateCustomer(toCheck);

    if (customer.id() == null) {
      validationErrors.add("No ID for updating given");
    }

    List<String> conflictErrors = new ArrayList<>();
    if (!(customerDao.emailExists(customer.email()))){
      conflictErrors.add("Email address does not exist for update");
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of customer for update failed", validationErrors);
    }
    if (!conflictErrors.isEmpty()) {
      throw new ConflictException("Update for customer contains conflicts", conflictErrors);
    }
  }

  public void validateForCreate(CustomerCreateDto customer) throws ValidationException, ConflictException, NotFoundException {
    LOG.trace("validateForCreate({})", customer);

    List<String> validationErrors = validateCustomer(customer);
    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of customer for update failed", validationErrors);
    }

    List<String> conflictErrors = new ArrayList<>();
    if (customerDao.emailExists(customer.email())){
      conflictErrors.add("Email already exists");
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of customer for update failed", validationErrors);
    }
    if (!conflictErrors.isEmpty()) {
      throw new ConflictException("Update for customer contains conflicts", conflictErrors);
    }
  }

  private List<String> validateCustomer(CustomerCreateDto customer) throws  NotFoundException{
    List<String> errorList = new ArrayList<>();

    if (customer == null) {
      throw new NotFoundException("Customer not found");
    }

    validateNotEmpty(customer.firstName(), "first name", errorList);
    validateNotEmpty(customer.lastName(), "last name", errorList);

    if (customer.dateOfBirth() == null ) {
      errorList.add("No dateOfBirth given");
    }
    if (customer.email() == null) {
      errorList.add("No email given");
    }

    if (customer.firstName().length() > 256) {
      errorList.add("The customer's first name can only have 256 characters.");
    }

    if (customer.lastName().length() > 256) {
      errorList.add("The customer's last name can only have 256 characters.");
    }

    if (!(isValidName(customer.firstName()))){
      errorList.add("The customer's first name is invalid.");
    }

    if ((!isValidName(customer.lastName()))){
      errorList.add("The customer's last name is invalid.");
    }

    if ((!isValidEmail(customer.email()))){
      errorList.add("The customer's email is invalid.");
    }

    if (customer.dateOfBirth() != null) {
      if ((!isValidDateFormat(customer.dateOfBirth()))){
        errorList.add("The customer's date of birth is invalid.");
      }
      if (customer.dateOfBirth().isAfter(LocalDate.now())){
        errorList.add("The customer's date of birth is invalid.");
      }
      if (customer.dateOfBirth().isBefore(LocalDate.now().minusYears(150))) {
        errorList.add("The customer's date of birth is invalid.");
      }
    }
    return errorList;
  }

  private boolean isValidName(String name){
    String nameRegex = "^[a-zA-ZÀ-ÿ '-]+$";
    Pattern pattern = Pattern.compile(nameRegex);
    return pattern.matcher(name).matches();
  }

  //obtained via https://www.baeldung.com/java-email-validation-regex

  private boolean isValidEmail(String email){
    //strict vs simple email validation
   // String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
    String emailRegex = "^(.+)@(\\S+)$";
    Pattern pattern = Pattern.compile(emailRegex);
    return pattern.matcher(email).matches();
  }


  private boolean isValidDateFormat(LocalDate date) {
    String dateToCheck = date.toString();
    System.out.println("Here is the dateToCheck");
    System.out.println(dateToCheck);
    String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    return Pattern.matches(regex, dateToCheck);
  }

  private void validateNotEmpty(String value, String fieldName, List<String> errors){
    if (value == null || value.isEmpty()) {
      errors.add("No " + fieldName + " given");
    }
  }

}
