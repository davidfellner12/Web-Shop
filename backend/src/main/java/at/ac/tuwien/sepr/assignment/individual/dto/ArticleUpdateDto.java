package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Dto for updating an article
 */
public record ArticleUpdateDto(
        Long id,
        String designation,
        String description,
        Integer price,
        String image,
        String imageType
) {
}
