package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

/**
 * Dto used to transfer customerId information used for customerId creation.
 */
public record CustomerCreateDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String email
) {
}
