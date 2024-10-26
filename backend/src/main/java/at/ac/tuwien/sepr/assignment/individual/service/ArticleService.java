package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;

import java.util.stream.Stream;

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

    /**
     * Searches for an article in the database
     * with the data given in {@code dto}
     *
     * @param dto the article to search for
     * @return a Stream of found ArticleDetailDtos
     */
    Stream<ArticleDetailDto> search(ArticleSearchDto dto);

    /**
     * Searches for an article in the database, specifically via the id
     * with the data given in {@code dto}
     *
     * @param id the id for the article to search
     * @return a Stream of found ArticleDetailDtos
     * @throws NotFoundException if the article with the specified id is not found
     */
    ArticleDetailDto get(Long id) throws NotFoundException;

    /**
     * Updates an article in the database, given by the data of the
     * DTO
     * @param dto
     * @return the updated Article
     * @throws NotFoundException if the article with given ID does not exist in the persistent data store
     * @throws ValidationException if the update data given for the article is in itself incorrect (no price, name too long …)
     *  @throws ConflictException   if the update data given for the article is in conflict the data currently in the system (designation already exists, …)
     **/

    ArticleDetailDto update(ArticleUpdateDto dto) throws NotFoundException, ValidationException, ConflictException;
}
