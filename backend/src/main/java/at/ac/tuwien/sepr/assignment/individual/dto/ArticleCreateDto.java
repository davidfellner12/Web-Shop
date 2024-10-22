package at.ac.tuwien.sepr.assignment.individual.dto;

import java.awt.*;
import java.sql.Blob;

public record ArticleCreateDto(
        String designation,
        String description,
        Integer price,
        String image,
        String imageType
) {

}
