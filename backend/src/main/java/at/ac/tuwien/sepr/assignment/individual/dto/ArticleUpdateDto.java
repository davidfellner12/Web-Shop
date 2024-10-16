package at.ac.tuwien.sepr.assignment.individual.dto;

public record ArticleUpdateDto(
        Long id,
        String designation,
        String description,
        Integer price
){
}
