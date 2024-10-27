package at.ac.tuwien.sepr.assignment.individual.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import at.ac.tuwien.sepr.assignment.individual.TestBase;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;

import java.time.LocalDate;

import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Service-Tests for the CustomerService
 */
@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
@Disabled
public class CustomerServiceTest extends TestBase {

    @Autowired
    CustomerService customerService;

    @Test
    public void searchByNameFindTwo() {
        var searchDto = new CustomerSearchDto(null, "ra", null, null, null, null, null);
        var customers = customerService.search(searchDto);
        assertNotNull(customers);
        assertThat(customers)
                .extracting(CustomerDetailDto::id, CustomerDetailDto::firstName,
                        CustomerDetailDto::lastName, CustomerDetailDto::dateOfBirth, CustomerDetailDto::email)
                .containsExactlyInAnyOrder(
                        tuple(-7L, "Sarah", "Davis", LocalDate.of(1995, 6, 22), "sarah.davis@example.com"),
                        tuple(-9L, "Laura", "Taylor", LocalDate.of(1991, 10, 4), "laura.taylor@example.com")
                );
    }

    @Test
    public void searchByAge() {
        var searchDto = new CustomerSearchDto(null, null, null, null, null, 40, null);
        var customers = customerService.search(searchDto);
        assertNotNull(customers);
        assertThat(customers)
                .extracting(CustomerDetailDto::id, CustomerDetailDto::firstName,
                        CustomerDetailDto::lastName, CustomerDetailDto::dateOfBirth, CustomerDetailDto::email)
                .containsExactlyInAnyOrder(
                        tuple(-8L, "David", "Wilson", LocalDate.of(1982, 3, 15), "david.wilson@example.com"),
                        tuple(-6L, "Michael", "Brown", LocalDate.of(1978, 12, 1), "michael.brown@example.com")
                );
    }

   /* @Test
    public void createCustomer() throws ValidationException, ConflictException, NotFoundException {
        var createDto = new CustomerCreateDto("Vincent", "Balau", LocalDate.of(1982, 3, 15), "vintischi@gmail.com");
        var customer = customerService.create(createDto);

        assertNotNull(customer);
        assertNotNull(customer.id()); // Ensure ID is not null (or you could check it is greater than zero)

        // Create a variable for expected values
        Long expectedId = customer.id(); // Use the actual generated ID for the expected assertion

        assertThat(customer)
                .extracting(CustomerDetailDto::id, CustomerDetailDto::firstName,
                        CustomerDetailDto::lastName,
                        CustomerDetailDto::dateOfBirth,
                        CustomerDetailDto::email)
                .containsExactlyInOrder(expectedId, "Vincent", "Balau", LocalDate.of(1982, 3, 15), "vintischi@gmail.com");
    }*/


}
