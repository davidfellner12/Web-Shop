package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDateTime;

public record OrderCreateDto(
        Long customerId,
        String name,
        LocalDateTime dateOfPurchase,
        Integer totalPrice
) {

}
