package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.mapper.ArticleMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.ArticleDao;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

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
  private final ArticleValidator articleValidator;
  private final ArticleMapper articleMapper;


  public ArticleServiceImpl(ArticleDao articleDao, ArticleValidator articleValidator, ArticleMapper articleMapper) {
    this.articleDao = articleDao;
    this.articleValidator = articleValidator;
    this.articleMapper = articleMapper;
  }
  @Override
  public ArticleDetailDto create(ArticleCreateDto dto) throws ValidationException, NotFoundException, ConflictException  {
    LOG.trace("create({})", dto);
    articleValidator.validateForCreate(dto);
    return articleMapper.entityToDetailDto(articleDao.create(dto));
  }

  @Override
  public Stream<ArticleDetailDto> search(ArticleSearchDto dto) {
    LOG.trace("search({})", dto);
    return articleDao.search(dto)
            .stream()
            .map(articleMapper::entityToDetailDto);
  }

  @Override
  public ArticleDetailDto get(Long id) throws NotFoundException {
    LOG.trace("get({})", id);
    //TODO: validation
    return  articleMapper.entityToDetailDto(articleDao.get(id));
  }

  @Override
  public ArticleDetailDto update(ArticleUpdateDto dto) throws NotFoundException, ValidationException, ConflictException {
    LOG.trace("update({})", dto);
    articleValidator.validateForUpdate(dto);
    return articleMapper.entityToDetailDto(articleDao.update(dto));
  }
}
