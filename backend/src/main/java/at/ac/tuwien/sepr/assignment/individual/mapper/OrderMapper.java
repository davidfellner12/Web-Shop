package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.OrderDetailDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.entity.Order;
import org.springframework.stereotype.Component;

/**
 * Mapper used to convert order entities to DTOs.
 */
@Component
public class OrderMapper {
    /**
     * Method used to map the entity to a detailed dto.
     */
    public OrderDetailDto entityToDetailDto(Order order) {
        return new OrderDetailDto(
                order.id(),
                order.customerId(),
                order.name(),
                order.dateOfPurchase(),
                order.totalPrice()
        );
    }
}
