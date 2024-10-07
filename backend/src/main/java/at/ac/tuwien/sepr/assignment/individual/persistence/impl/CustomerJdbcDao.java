package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.CustomerDao;
import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
      + " FROM " + TABLE_NAME
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

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate jdbcNamed;


  public CustomerJdbcDao(
      NamedParameterJdbcTemplate jdbcNamed,
      JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcNamed = jdbcNamed;
  }


  @Override
  public Collection<Customer> search(CustomerSearchDto searchParameters) {
    LOG.trace("search({})", searchParameters);
    return jdbcTemplate.query(SQL_SELECT_SEARCH, this::mapRow);
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
        ps.setDate(3, java.sql.Date.valueOf(customerCreateDto.dateOfBirth()));
        ps.setString(4, customerCreateDto.email());
        return ps;
      }, keyHolder);
    } catch (DataAccessException e) {
        throw new ConflictException("Customer could not be created in the database", List.of(e.getMessage()));
    }
    Long id = keyHolder.getKey().longValue();
    return new Customer(id, customerCreateDto.firstName(), customerCreateDto.lastName(), customerCreateDto.dateOfBirth(), customerCreateDto.email());
  }

  @Override
  public boolean emailExists(String email) {
    List<Customer> customers = jdbcTemplate.query(SQL_SELECT_BY_EMAIL, this::mapRow, email);
    return !customers.isEmpty();
  }


  @Override
  public Customer update(CustomerUpdateDto dto) throws NotFoundException {
    LOG.trace("update({})", dto);
    int updated = jdbcTemplate.update(SQL_UPDATE,
        dto.firstName(),
        dto.lastName(),
        dto.dateOfBirth(),
        dto.email(),
        dto.id());
    if (updated == 0) {
      throw new NotFoundException(("Could not update customer with ID %d,"
          + "because it does not exist").formatted(dto.id()));
    }
    return new Customer(
        dto.id(),
        dto.lastName(),
        dto.firstName(),
        dto.dateOfBirth(),
        dto.email()
    );
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
