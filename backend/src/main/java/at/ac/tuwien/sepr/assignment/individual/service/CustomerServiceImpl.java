package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.mapper.CustomerMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.CustomerDao;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the CustomerService
 */
@Service
public class CustomerServiceImpl implements CustomerService {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final CustomerDao customerDao;
  private final CustomerMapper customerMapper;
  private final CustomerValidator customerValidator;

  /**
   * Constructor
   */
  public CustomerServiceImpl(CustomerDao customerDao, CustomerMapper customerMapper, CustomerValidator customerValidator) {
    this.customerDao = customerDao;
    this.customerMapper = customerMapper;
    this.customerValidator = customerValidator;
  }

  @Override
  public Stream<CustomerDetailDto> search(CustomerSearchDto searchParameters) {
    LOG.trace("search({})", searchParameters);
    return customerDao.search(searchParameters)
        .stream()
        .map(customerMapper::entityToDetailDto);
  }


  @Override
  public CustomerDetailDto update(CustomerUpdateDto dto) throws NotFoundException, ValidationException, ConflictException {
    LOG.trace("update({})", dto);
    customerValidator.validateForUpdate(dto);
    return customerMapper.entityToDetailDto(customerDao.update(dto));
  }

  @Override
  public CustomerDetailDto create(CustomerCreateDto dto) throws  ValidationException, ConflictException, NotFoundException {
    LOG.trace("create({})", dto);
    customerValidator.validateForCreate(dto);
    return customerMapper.entityToDetailDto(customerDao.create(dto));
  }

}
