package at.ac.tuwien.sepr.assignment.individual.entity;

import org.springframework.jdbc.support.lob.LobCreator;

import java.awt.*;
import java.io.File;
import java.sql.Blob;

/**
 * Entity describing an article.
 *
 */
public record Article(
        Long id,
        String designation,
        String description,
        Integer price,
        String imageBase64
) {
}
