package at.ac.tuwien.sepr.assignment.individual.rest;

public record ErrorRestDto(
/**
 * Dto describing a not found error.
 *
 * @param message Message attached to the not found error
 */
String message
) {
}
