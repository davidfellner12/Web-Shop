package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;

/**
 * Mapper used to convert customer entities to DTOs.
 */
@Component
public class CustomerMapper {

  /**
   * Method used to map the entity to a detailed dto.
   */
  public CustomerDetailDto entityToDetailDto(Customer customer) {
    return new CustomerDetailDto(
        customer.id(),
        customer.firstName(),
        customer.lastName(),
        customer.dateOfBirth(),
        customer.email(),
        Period.between(customer.dateOfBirth(), LocalDate.now()).getYears()
    );
  }
}
