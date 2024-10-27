package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.OrderCreateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.entity.Order;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.persistence.OrderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class OrderJdbcDao implements OrderDao {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String TABLE_NAME = "articles";
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcNamed;
    private static final String SQL_REGISTER_ORDERS = "INSERT INTO " + TABLE_NAME
            + " SET customerId = ?"
            + "  , date  = ?"
            + "  , price = ?";

    public OrderJdbcDao(
            NamedParameterJdbcTemplate jdbcNamed,
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcNamed = jdbcNamed;
    }

    @Override
    public Order create(OrderCreateDto dto) throws ConflictException {
        /*LOG.trace("create({})", dto);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("Here is the dto before encoding" + dto);

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_REGISTER_ORDERS, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, dto.customerId());
                ps.setString(2, dto.name());
                ps.setString(3, String.valueOf(dto.dateOfPurchase()));
                ps.setInt(4, dto.totalPrice());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            LOG.error("Database access error occurred while creating order: {}", e.getMessage(), e);
            throw new ConflictException("Order could not be created in the database", List.of(e.getMessage()));
        }
        Long id = keyHolder.getKey().longValue();
        return new Order(id,dto.customerId(),dto.name(),dto.dateOfPurchase(), dto.totalPrice());*/
        return new Order(null, null, null, null, null);
    }


    private Order mapRow(ResultSet result, int rowNum) throws SQLException {
        return new Order(
                result.getLong("id"),
                result.getLong("customerId"),
                result.getString("name"),
                result.getDate("dateOfPurchase"),
                result.getInt("totalPrice")
        );
    }
}
