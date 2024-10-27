package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Dto used to delete a customer
 */
public record CustomerDeleteDto(
        Long id,
        String message
) {
}
