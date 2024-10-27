package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Dto used to create an order
 */
public record OrderDetailDto(
        Long id,
        Long customerId,
        String name,
        java.sql.Date dateOfPurchase,
        Integer totalPrice
) {
}
