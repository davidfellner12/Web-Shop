package at.ac.tuwien.sepr.assignment.individual.rest;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;
import at.ac.tuwien.sepr.assignment.individual.service.CustomerService;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
   * @param dto Dto containing search parameters
   * @return Stream of customers matching search parameters
   */
  @GetMapping()
  public Stream<CustomerDetailDto> search(CustomerSearchDto dto) {
    LOG.info("GET " + BASE_PATH);
    LOG.debug("request parameters: {}", dto);
    return customerService.search(dto);
  }


  private void logClientError(HttpStatus status, String message, Exception e) {
    LOG.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
  }
}
