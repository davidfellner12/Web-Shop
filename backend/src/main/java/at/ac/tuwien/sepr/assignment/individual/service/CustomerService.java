package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import java.util.stream.Stream;

/**
 * Service for working with customers.
 */
public interface CustomerService {
  /**
   * Search for customers in the persistent data store matching all provided fields.
   * The firstName, lastName and email are considered a match, if the search string is a substring of the field in customer.
   *
   * @param searchParameters the search parameters to use in filtering.
   * @return the customers where the given fields match.
   */
  Stream<CustomerDetailDto> search(CustomerSearchDto searchParameters);


  /**
   * Updates the customer with the ID given in {@code customer}
   * with the data given in {@code customer}
   * in the persistent data store.
   *
   * @param customer the customer to update
   * @return the updated customer
   * @throws NotFoundException   if the customer with given ID does not exist in the persistent data store
   * @throws ValidationException if the update data given for the customer is in itself incorrect (no name, name too long …)
   * @throws ConflictException   if the update data given for the customer is in conflict the data currently in the system (email already exists, …)
   */
  CustomerDetailDto update(CustomerUpdateDto customer) throws NotFoundException, ValidationException, ConflictException;

  /**
   * Creates a new customer in the database
   * with the data given in {@code customer}
   * in the persistent data store
   *
   * @param customer the customer to create
   * @return
   * @throws ValidationException if the data given for the customer is in itself incorrect(no name, name too long)
   * @throws ConflictException if the data given for the customer is in conflict with the data currently in the system (email already exists, …)
   * @throws NotFoundException if the customer parameter is null
   */
  CustomerDetailDto create(CustomerCreateDto customer) throws ValidationException, ConflictException, NotFoundException;

  /**
   * Gets a customer in the database
   * with the id given in  {@code id}
   * @param id the id for the customer
   * @return
   * @throws NotFoundException if there exists no customer for the specific id
   */
  CustomerDetailDto get(Long id) throws NotFoundException;

  /**
   * Deletes a specific customer
   * in the database with the id
   * given in {@code id}
   * @param id the id for the customer to delete
   * @return
   * @throws NotFoundException if there exists no customer to delete with the specific id
   */
  void delete(Long id) throws NotFoundException;

}
