package at.ac.tuwien.sepr.assignment.individual.persistence;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;

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
     * @param designation
     * @return true if is so, else false
     */
    boolean designationExists(String designation);
}
