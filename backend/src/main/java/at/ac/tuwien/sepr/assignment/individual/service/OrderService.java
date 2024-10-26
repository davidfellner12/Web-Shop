package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.OrderCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.OrderDetailDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;

public interface OrderService {
    /**
     * Creates a new order in the database
     * with the data given in {@code dto}
     * in the persistent data store
     *
     * @param dto the order to create
     * @return
     * @throws ValidationException if the data given for the order is in itself incorrect(no designation, designation too long)
     * @throws ConflictException if the data given for the order is in conflict with the data currently in the system (id already exists, …)
     */
    OrderDetailDto create(OrderCreateDto dto) throws ConflictException, ValidationException, NotFoundException;
}
