package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.CustomerSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Customer;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.CustomerDao;
import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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



  private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME
      + " SET first_name = ?"
      + "  , last_name = ?"
      + "  , date_of_birth = ?"
      + "  , email = ?"
      + " WHERE id = ?";

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
