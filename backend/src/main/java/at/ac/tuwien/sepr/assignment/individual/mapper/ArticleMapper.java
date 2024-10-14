package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

/**
 * Mapper used to convert article entities to DTOs.
 */
@Component
public class ArticleMapper {
    /**
     * Method used to map the entity to a detailed dto.
     */
    public ArticleDetailDto entityToDetailDto(Article article) {
        return new ArticleDetailDto(
                article.id(),
                article.designation(),
                article.description(),
                article.price()
        );
    }
}
