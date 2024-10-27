package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Dto used to transfer detailed Article information
 */
public record ArticleDetailDto(
        Long id,
        String designation,
        String description,
        Integer price,
        String image,
        String imageType) {
}

