package at.ac.tuwien.sepr.assignment.individual.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import at.ac.tuwien.sepr.assignment.individual.TestBase;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;

import java.time.LocalDate;

import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Service-Tests for the CustomerService
 */

    @ActiveProfiles({"test", "datagen"})
    @SpringBootTest
    @Disabled
    public class ArticleServiceTest extends TestBase {

        @Autowired
        ArticleService articleService;

        @Test
        public void searchByDesignation() {
            var searchDto = new ArticleSearchDto("Smartphone", null, null, null, null, null);
            var articles = articleService.search(searchDto);
            assertNotNull(articles);
            assertThat(articles)
                    .extracting(ArticleDetailDto::id, ArticleDetailDto::designation,
                            ArticleDetailDto::description, ArticleDetailDto::price, ArticleDetailDto::image, ArticleDetailDto::imageType)
                    .containsExactlyInAnyOrder(
                            tuple("-2", "Smartphone", "Latest model smartphone with 128GB storage", 79999, null, "png")
                    );
        }
    }

