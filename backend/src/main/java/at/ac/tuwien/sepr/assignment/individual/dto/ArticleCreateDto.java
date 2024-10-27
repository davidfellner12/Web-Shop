package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Data Transfer Object (DTO) for creating an article.
 *
 * @param designation the name of the article
 * @param description a brief description of the article
 * @param price the price of the article
 * @param image the image data of the article, typically encoded in Base64
 * @param imageType the MIME type of the image (e.g., "image/jpeg")
 */

public record ArticleCreateDto(
        String designation,
        String description,
        Integer price,
        String image,
        String imageType
) {
}
