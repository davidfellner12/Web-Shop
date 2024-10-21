package at.ac.tuwien.sepr.assignment.individual.dto;

import java.sql.Blob;

public record ArticleUpdateDto(
        Long id,
        String designation,
        String description,
        Integer price,
        String image
){
}
