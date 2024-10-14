package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;

/**
 * Service for working with articles.
 */
public interface ArticleService {

    /**
     * Creates a new article in the database
     * with the data given in {@code dto}
     * in the persistent data store
     *
     * @param dto the article to create
     * @return
     * @throws ValidationException if the data given for the article is in itself incorrect(no designation, designation too long)
     * @throws ConflictException if the data given for the article is in conflict with the data currently in the system (id already exists, …)
     */
    ArticleDetailDto create(ArticleCreateDto dto) throws ConflictException, ValidationException, NotFoundException;
}
