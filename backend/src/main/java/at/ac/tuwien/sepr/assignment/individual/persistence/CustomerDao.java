package at.ac.tuwien.sepr.assignment.individual.persistence;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import java.util.Collection;

/**
 * DAO used to access the {@link Customer} entity.
 */
public interface CustomerDao {

  /**
   * Fetch the customers that match the given search parameters.
   * Parameters that are {@code null} are ignored.
   * The firstName, lastName, email is considered a match, if the given parameter is a substring of the field.
   *
   * @param searchParameters the parameters to use for searching.
   * @return the customers where all given parameters match.
   */
  Collection<Customer> search(CustomerSearchDto searchParameters);


  /**
   * Update the customer with the ID given in {@code dto}
   * with the data given in {@code dto}
   * in the persistent data store.
   *
   * @param dto dto containing data with which to update existing customer
   * @return the updated customer
   * @throws NotFoundException thrown if the customer with the given ID does not exist in the persistent data store
   */
  Customer update(CustomerUpdateDto dto) throws NotFoundException;

  /**
   * Register and creates with
   * the data given in {@code dto}
   * in the persistent data store.
   *
   * @param dto dto containing data to create the new user
   * @return the created customer
   * @throws ConflictException thrown if the input data types do not math the expected input
   */
  Customer create(CustomerCreateDto dto) throws ConflictException;


  /**
   * Checks if a customer with the
   * data given in {@code email}
   * exists in the data store
   * @param email email for searching in the database
   * @return boolean if is so
   */
  boolean emailExists(String email);

  /**
   * Gets a customer with the id
   * given in {@code id}
   * when it exists in the data store
   * @param id id for searching the customer
   * @return this specific customer if is so
   * @throws NotFoundException thrown if there exists no customer with the specified id
   */
  Customer getOneById(Long id) throws NotFoundException;
}
