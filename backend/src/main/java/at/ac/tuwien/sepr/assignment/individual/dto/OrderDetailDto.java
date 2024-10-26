package at.ac.tuwien.sepr.assignment.individual.dto;

public record OrderDetailDto(
        Long id,
        Long customerId,
        String name,
        java.sql.Date dateOfPurchase,
        Integer totalPrice
) {
}
