package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

/**
 * Dto used to transfer customerId information used for updates.
 */
public record CustomerUpdateDto(
    Long id,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String email
) {

  /**
   * Constructor used to create a new dto with a new id.
   */
  public CustomerUpdateDto withId(long newId) {
    return new CustomerUpdateDto(
        newId,
        firstName,
        lastName,
        dateOfBirth,
        email);
  }
}
