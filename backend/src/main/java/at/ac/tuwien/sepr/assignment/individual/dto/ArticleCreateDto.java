package at.ac.tuwien.sepr.assignment.individual.dto;

import java.awt.*;

public record ArticleCreateDto(
        String designation,
        String description,
        Integer price
) {
}
