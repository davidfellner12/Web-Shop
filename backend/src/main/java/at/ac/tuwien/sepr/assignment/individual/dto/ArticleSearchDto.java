package at.ac.tuwien.sepr.assignment.individual.dto;

public record ArticleSearchDto(
        String name,
        String description,
        Integer minPrice,
        Integer maxPrice
) {
}
