package at.ac.tuwien.sepr.assignment.individual.dto;

import java.awt.*;
import java.sql.Blob;
import java.time.LocalDate;

/**
 * Dto used to transfer detailed Article information
 */

public record ArticleDetailDto(
        Long id,
        String designation,
        String description,
        Integer price
        ) {
}

