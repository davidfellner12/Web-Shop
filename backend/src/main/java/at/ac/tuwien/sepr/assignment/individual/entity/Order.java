package at.ac.tuwien.sepr.assignment.individual.entity;

public record Order(
        Long id,
        Long customerId,
        String name,
        java.sql.Date dateOfPurchase,
        Integer totalPrice
) {

}
