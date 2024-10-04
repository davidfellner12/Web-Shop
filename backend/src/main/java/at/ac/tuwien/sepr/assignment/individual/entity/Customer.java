package at.ac.tuwien.sepr.assignment.individual.entity;

import java.time.LocalDate;

/**
 * Entity describing a single customerId.
 */
public record Customer(
    Long id,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String email
) {
}
