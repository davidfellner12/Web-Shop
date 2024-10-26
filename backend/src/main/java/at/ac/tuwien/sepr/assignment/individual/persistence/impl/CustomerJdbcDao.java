package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.CustomerDao;
import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


/**
 * Implementation of the CustomerDao interface using JDBC.
 */
@Repository
public class CustomerJdbcDao implements CustomerDao {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String TABLE_NAME = "customers";
  private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
  private static final String SQL_SELECT_SEARCH = "SELECT  *"
      + " FROM " + TABLE_NAME + " WHERE 1=1"
      ;
  private static final String SQL_REGISTER_CUSTOMER = "INSERT INTO " + TABLE_NAME
          + " SET first_name = ?"
          + "  , last_name = ?"
          + "  , date_of_birth = ?"
          + "  , email = ?";

  private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME
      + " SET first_name = ?"
      + "  , last_name = ?"
      + "  , date_of_birth = ?"
      + "  , email = ?"
      + " WHERE id = ?";

  private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
  private static final String SQL_DELETE_BY_ID = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate jdbcNamed;


  public CustomerJdbcDao(
      NamedParameterJdbcTemplate jdbcNamed,
      JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcNamed = jdbcNamed;
  }


  //TODO  give it a try with positional arguments
  @Override
  public Collection<Customer> search(CustomerSearchDto searchParameters) {
    LOG.trace("search({})", searchParameters);
    StringBuilder sqlQuery = new StringBuilder(SQL_SELECT_SEARCH);

    if (searchParameters.firstName() != null) {
      sqlQuery.append(" AND LOWER(first_name) = LOWER('" + searchParameters.firstName() + "')");
    }
    if (searchParameters.lastName() != null) {
      sqlQuery.append(" AND LOWER(last_name) =  LOWER('" + searchParameters.lastName() + "')");
    }
    if (searchParameters.email() != null) {
      sqlQuery.append(" AND LOWER(email) = LOWER('" + searchParameters.email() + "')");
    }
    if (searchParameters.dateOfBirthEarliest() != null && searchParameters.dateOfBirthLatest() != null) {
      sqlQuery.append(" AND date_of_birth  > '" + searchParameters.dateOfBirthEarliest() + "'" + " AND date_of_birth < '" + searchParameters.dateOfBirthLatest() + "'");
    } else if (searchParameters.dateOfBirthEarliest() != null && searchParameters.dateOfBirthLatest() == null) {
      sqlQuery.append(" AND date_of_birth > '" + searchParameters.dateOfBirthEarliest() + "'");
    } else if (searchParameters.dateOfBirthEarliest() == null && searchParameters.dateOfBirthLatest() != null) {
      sqlQuery.append(" AND date_of_birth  < '" + searchParameters.dateOfBirthLatest() + "'");
    }

    LocalDate currentDate = LocalDate.now();
    if (searchParameters.minAge() != null && searchParameters.maxAge() != null) {
      LocalDate newMinDate = currentDate.minusYears(searchParameters.maxAge());
      LocalDate newMaxDate = currentDate.minusYears(searchParameters.minAge());
      sqlQuery.append(" AND date_of_birth > '" + newMinDate + "'" + " AND date_of_birth < '" + newMaxDate + "'");
    } else if (searchParameters.minAge() != null && searchParameters.maxAge() == null) {
      LocalDate newMaxDate = currentDate.minusYears(searchParameters.minAge());

      sqlQuery.append(" AND date_of_birth < '" + newMaxDate + "'");
    } else if (searchParameters.minAge() == null && searchParameters.maxAge() != null) {
      LocalDate newMaxDate = currentDate.minusYears(searchParameters.maxAge());
      sqlQuery.append(" AND date_of_birth > '" + newMaxDate + "'");
      System.out.println(newMaxDate);
    }
    String sql = sqlQuery.toString();

    try{
      return jdbcTemplate.query(sql, this::mapRow);
    } catch(DataAccessException e) {
      LOG.error("Database access error occurred while searching for customer: {}", e.getMessage(), e);
      throw new FatalException("Database access error occurred while searching for customer: {} " + e.getMessage());
    }

  }

  @Override
  public Customer create(CustomerCreateDto customerCreateDto) throws ConflictException {
    LOG.trace("create({})", customerCreateDto);
    KeyHolder keyHolder = new GeneratedKeyHolder();

    try {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(SQL_REGISTER_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, customerCreateDto.firstName());
        ps.setString(2, customerCreateDto.lastName());
        ps.setDate(3, Date.valueOf(customerCreateDto.dateOfBirth()));
        ps.setString(4, customerCreateDto.email());
        return ps;
      }, keyHolder);
    } catch (DataAccessException e) {
        LOG.error("Database access error occurred while creating customer: {}", e.getMessage(), e);
        throw new FatalException("Database access error occurred while creating customer: {} " + e.getMessage());
    }
    Long id = keyHolder.getKey().longValue();
    return new Customer(id, customerCreateDto.firstName(), customerCreateDto.lastName(), customerCreateDto.dateOfBirth(), customerCreateDto.email());
  }

  @Override
  public boolean emailExists(String email) {
    List<Customer> customers = List.of();
    try{
      customers = jdbcTemplate.query(SQL_SELECT_BY_EMAIL, this::mapRow, email);
    } catch (DataAccessException e) {
      LOG.error("Database access error while checking for email: {}", e.getMessage(), e);
      throw new FatalException("Database access error while checkign for email: {} " + e.getMessage());
    }

    return !customers.isEmpty();
  }

  @Override
  public Customer getOneById(Long id) throws NotFoundException{
    LOG.trace("getOneById({})", id);

    List<Customer> result = null;
    try{
      result = jdbcTemplate.query(SQL_SELECT_BY_ID, this::mapRow, id);
    } catch(DataAccessException e) {
      LOG.error("Database access error while getting customer by id: {}", e.getMessage(), e);
      throw new FatalException("Database access error while getting customer by id: {} " + e.getMessage());
    }

    if (result == null || result.isEmpty()) {
      LOG.warn("Customer with id {} not found", id);
      throw new NotFoundException(("Could not found customer with ID %d," + "because it does not exist").formatted(id));
    }
    if (result.size() > 1) {
      LOG.error("To many customers with ID %d found");
      throw new FatalException("To many customers with ID %d found".formatted(id));
    }
    return result.getFirst();
  }


  @Override
  public Customer update(CustomerUpdateDto dto) throws NotFoundException {
    LOG.trace("update({})", dto);

    try{
      int updated = jdbcTemplate.update(SQL_UPDATE,
              dto.firstName(),
              dto.lastName(),
              dto.dateOfBirth(),
              dto.email(),
              dto.id());
      if (updated == 0) {
        LOG.warn("Could not update customer with ID %d");
        throw new NotFoundException(("Could not update customer with ID %d,"
                + "because it does not exist").formatted(dto.id()));
      }
    } catch (DataAccessException e) {
      LOG.error("Database access error while updating customer: {}", e.getMessage(), e);
      throw new FatalException("Database access error while updating customer: {} " + e.getMessage());
    }

    return new Customer(
        dto.id(),
        dto.lastName(),
        dto.firstName(),
        dto.dateOfBirth(),
        dto.email()
    );
  }

  @Override
  public void delete(Long id) throws NotFoundException {
    LOG.trace("delete({})", id);

    List<Customer> customers = List.of();
    try{
      customers = jdbcTemplate.query(SQL_SELECT_BY_ID, this::mapRow, id);
    } catch (DataAccessException e) {
      LOG.error("Database access error while deleting customer: {}", e.getMessage(), e);
    }

    if (customers.isEmpty() || customers == null) {
      LOG.warn("Customer with id {} not found", id);
      throw new NotFoundException("Could not delete customer with ID %d".formatted(id) + "because it does not exist");
    }
    //jdbcTemplate.update(SQL_DELETE_BY_ID, id);
  }

  private Customer mapRow(ResultSet result, int rowNum) throws SQLException {
    return new Customer(
        result.getLong("id"),
        result.getString("first_name"),
        result.getString("last_name"),
        result.getDate("date_of_birth").toLocalDate(),
        result.getString("email")
    );
  }
}
