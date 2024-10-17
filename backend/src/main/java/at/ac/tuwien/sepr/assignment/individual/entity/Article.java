package at.ac.tuwien.sepr.assignment.individual.entity;

import java.awt.*;
import java.io.File;

/**
 * Entity describing an article.
 *
 */
public record Article(
        Long id,
        String designation,
        String description,
        Integer price
        /*TODO: String imagePath*/
) {
}
