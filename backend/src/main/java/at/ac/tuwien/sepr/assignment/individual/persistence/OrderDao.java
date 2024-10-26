package at.ac.tuwien.sepr.assignment.individual.persistence;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.OrderCreateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.entity.Order;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;

public interface OrderDao {
    /**
     * Creates and safes an Order with
     * the data given in {@code dto}
     * in the persistent data store.
     *
     * @param dto dto containing data to create the new Article
     * @return the created article
     * @throws ConflictException thrown if the input data types do not match the expected input
     */
    Order create(OrderCreateDto dto) throws ConflictException;
}
