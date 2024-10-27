package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDateTime;

/**
 * Dto used to create an order
 */
public record OrderCreateDto(
        Long customerId,
        String name,
        LocalDateTime dateOfPurchase,
        Integer totalPrice
) {

}
