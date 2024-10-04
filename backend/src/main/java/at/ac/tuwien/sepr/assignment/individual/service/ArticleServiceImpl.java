package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.persistence.ArticleDao;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the ArticleService
 */
@Service
public class ArticleServiceImpl implements ArticleService {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final ArticleDao articleDao;
  public ArticleServiceImpl(ArticleDao articleDao) {
    this.articleDao = articleDao;
  }
}
