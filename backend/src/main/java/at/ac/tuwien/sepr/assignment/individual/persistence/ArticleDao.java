package at.ac.tuwien.sepr.assignment.individual.persistence;

import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;

import java.util.Collection;

/**
 * DAO interface used to access the {@link Article} entity.
 */
public interface ArticleDao {

    /**
     * Register and creates an Article with
     * the data given in {@code dto}
     * in the persistent data store.
     *
     * @param dto dto containing data to create the new Article
     * @return the created article
     * @throws ConflictException thrown if the input data types do not match the expected input
     */
    Article create(ArticleCreateDto dto) throws ConflictException;

    /**
     * Checks if a designation with
     * the data given in {@code designation}
     * exists in the persistent data store
     *
     * @param designation is the designation of the article
     * @return true if is so, else false
     */
    boolean designationExists(String designation);

    /**
     * Fetch the articles that match the given search parameters.
     * Parameters that are {@code null} are ignored.
     *
     * @param dto the parameters to use for searching.
     * @return the articles where all given parameters match.
     */

    Collection<Article> search(ArticleSearchDto dto);

    /**
     * Get the object for the id, which
     *
     * @param id the id for getting the article
     * @return the article where the given id of the parameter matches
     * @throws NotFoundException thrown if there is no entry with the specified id in the database
     */

    Article get(Long id) throws NotFoundException;

    /**
     * Update the article with the ID given in {@code dto}
     * with the data given in {@code dto}
     * in the persistent data store.
     *
     * @param dto dto containing data with which to update existing article
     * @return the updated article
     * @throws NotFoundException thrown if the article with the given ID does not exist in the persistent data store
     */
    Article update(ArticleUpdateDto dto) throws NotFoundException;
}
