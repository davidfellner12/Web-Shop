package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Dto used to search for articles
 */
public record ArticleSearchDto(
        String name,
        String description,
        Integer minPrice,
        Integer maxPrice,
        String image,
        String imageType
) {
}
