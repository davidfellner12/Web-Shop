package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;

import at.ac.tuwien.sepr.assignment.individual.mapper.ArticleMapper;
import at.ac.tuwien.sepr.assignment.individual.mapper.OrderMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.ArticleDao;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;
import at.ac.tuwien.sepr.assignment.individual.persistence.OrderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the ArticleService
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderDao orderDao;
    private final OrderMapper orderMapper;


    public OrderServiceImpl(OrderDao orderDao, OrderMapper orderMapper) {
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
    }


    @Override
    public OrderDetailDto create(OrderCreateDto dto) throws ConflictException, ValidationException, NotFoundException {
        LOG.trace("create({})", dto);
        return orderMapper.entityToDetailDto(orderDao.create(dto));

    }
}