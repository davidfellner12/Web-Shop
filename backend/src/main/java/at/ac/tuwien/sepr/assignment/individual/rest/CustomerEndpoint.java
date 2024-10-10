package at.ac.tuwien.sepr.assignment.individual.rest;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.service.CustomerService;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Customer Endpoint Implementation
 */
@RestController
@RequestMapping(CustomerEndpoint.BASE_PATH)
public class CustomerEndpoint {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  public static final String BASE_PATH = "/customers";

  private final CustomerService customerService;

  public CustomerEndpoint(CustomerService customerService) {
    this.customerService = customerService;
  }
  /**
   * Interface to search for customers by search parameters.
   *
   *
   * @return Stream of customers matching search parameters
   */
  @GetMapping(value = "")
  public Stream<CustomerDetailDto> search(CustomerSearchDto dto) {
    LOG.info("GET " + BASE_PATH);
    LOG.debug("request parameters: {}", dto);
    return customerService.search(dto);
  }

  /**
   * Interface to create a new customer in the database
   * @param dto Dto containing information for creation of a new persistence entry
   * @return
   * @throws ValidationException thrown if the input does not match the expected one
   * @throws ConflictException thrown if there already exists an entry for the specific parameter
   * @throws NotFoundException thrown if
   */

  //TODO: Check for throwing of ConflictException, thats not true
  @PostMapping()
  public CustomerDetailDto create(@RequestBody CustomerCreateDto dto) throws ValidationException, ConflictException, NotFoundException {
    LOG.info("POST " + BASE_PATH);
    LOG.debug("request parameters: {}", dto);
    return customerService.create(dto);
  }

  @PatchMapping()
  public CustomerDetailDto patch(@RequestBody CustomerUpdateDto dto) throws ValidationException, ConflictException, NotFoundException {
    LOG.info("PATCH " + BASE_PATH + "/{}", dto);
    LOG.debug("request parameters: {}", dto);
    return customerService.update(dto);
  }

  @GetMapping("/{id}")
  public CustomerDetailDto get(@PathVariable("id") Long id) throws NotFoundException {
    LOG.info("GET " + BASE_PATH + "/{}", id);
    LOG.debug("request parameters: {}", id);
    System.out.println("here is the" + id);
    return customerService.get(id);
  }

  private void logClientError(HttpStatus status, String message, Exception e) {
    LOG.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
  }
}
