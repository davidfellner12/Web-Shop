package at.ac.tuwien.sepr.assignment.individual.rest;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.service.ArticleService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Article Endpoint Implementation
 */
@RestController
@RequestMapping(ArticleEndpoint.BASE_PATH)
public class ArticleEndpoint {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  public static final String BASE_PATH = "/articles";

  private final ArticleService articleService;

  public ArticleEndpoint(ArticleService articleService) {
    this.articleService = articleService;
  }

  @PostMapping()
  public ArticleDetailDto create(@RequestBody ArticleCreateDto dto) throws ValidationException, ConflictException, NotFoundException {
    LOG.info("POST " + BASE_PATH);
    LOG.debug("request parameters: {}", dto);
    return articleService.create(dto);
  }


  private void logClientError(HttpStatus status, String message, Exception e) {
    LOG.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
  }
}
