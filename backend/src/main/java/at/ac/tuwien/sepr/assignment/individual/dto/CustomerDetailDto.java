package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

/**
 * Dto used to transfer detailed customerId information.
 */
public record CustomerDetailDto(
    Long id,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String email,
    Integer age
) {
}
