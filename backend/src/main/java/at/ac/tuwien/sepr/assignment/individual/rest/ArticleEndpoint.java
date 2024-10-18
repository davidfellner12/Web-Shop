package at.ac.tuwien.sepr.assignment.individual.rest;

import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.service.ArticleService;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

  /**
   * Interface to search for articles by search parameters.
   * @return Stream of articles matching search parameters
   */
  @GetMapping(value = "")
  public Stream<ArticleDetailDto> search(ArticleSearchDto dto) {
    LOG.info("GET " + BASE_PATH);
    LOG.debug("request parameters: {}", dto);
    return articleService.search(dto);
  }

  @GetMapping("/{id}")
  public ArticleDetailDto get(@PathVariable("id") Long id) throws NotFoundException{
    LOG.info("GET " + BASE_PATH);
    LOG.info("GET /articles with id: {}", id);

    LOG.debug("request parameters: {}", id);
    return articleService.get(id);
  }

  @PatchMapping()
  public ArticleDetailDto patch(@RequestBody ArticleUpdateDto dto) throws ValidationException, ConflictException, NotFoundException {
    LOG.info("PATCH " + BASE_PATH + "/{}", dto);
    LOG.debug("request parameters: {}", dto);
    return articleService.update(dto);
  }

  private void logClientError(HttpStatus status, String message, Exception e) {
    LOG.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
  }
}
