package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

/**
 * Dto used for search parameters by which to filter customers.
 */
public record CustomerSearchDto(
    String firstName,
    String lastName,
    String email,
    LocalDate dateOfBirthEarliest,
    LocalDate dateOfBirthLatest,
    Integer minAge,
    Integer maxAge
) {
}
