package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Article;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.ArticleDao;

import java.io.ByteArrayInputStream;
import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.Base64;
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

import javax.sql.rowset.serial.SerialBlob;
import javax.xml.crypto.Data;

/**
 * Implementation of the ArticleDao interface using JDBC.
 */
@Repository
public class ArticleJdbcDao implements ArticleDao {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String TABLE_NAME = "articles";
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcNamed;

    private static final String SQL_REGISTER_ARTICLE = "INSERT INTO " + TABLE_NAME
            + " SET designation = ?"
            + "  , description = ?"
            + "  , price = ?"
            + "  , image = ?"
            + "  , image_type = ?";

    private static final String SQL_SELECT_BY_DESIGNATION = "SELECT * FROM " + TABLE_NAME + " WHERE designation = ?";
    private static final String SQL_SELECT_ALL_ARTICLES = "SELECT * FROM " + TABLE_NAME + " WHERE 1=1";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String SQL_SELECT_IMAGE_BY_ID = "SELECT image FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME
            + " SET designation = ?"
            + "  , description = ?"
            + "  ,price =  ?"
            + " ,image = ?"
            + " WHERE id = ?";


    public ArticleJdbcDao(
            NamedParameterJdbcTemplate jdbcNamed,
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcNamed = jdbcNamed;
    }

    @Override
    public Article create(ArticleCreateDto dto) throws ConflictException {
        LOG.trace("create({})", dto);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("Here is the dto before encoding" + dto);
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_REGISTER_ARTICLE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, dto.designation());
                ps.setString(2, dto.description());
                ps.setInt(3, dto.price());
                ps.setString(4, dto.image());
                ps.setString(5, dto.imageType());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            LOG.error("Database access error occurred while creating article: {}", e.getMessage(), e);
            throw new FatalException("Database access error occurred while creating article: {} " + e.getMessage());
        }
        Long id = keyHolder.getKey().longValue();
        return new Article(id, dto.designation(), dto.description(), dto.price(), dto.image(), dto.imageType());
    }

    @Override
    public boolean designationExists(String designation) {
        List<Article> articles;
        try {
            articles = jdbcTemplate.query(SQL_SELECT_BY_DESIGNATION, this::mapRow, designation);
        } catch (DataAccessException e) {
            LOG.error("Database access error occurred while reading designation: {}", e.getMessage(), e);
            throw new FatalException("Database access error occurred while designating email for article: {} " + e.getMessage());
        }
        return !articles.isEmpty();
    }

    @Override
    public Collection<Article> search(ArticleSearchDto dto) {
        LOG.trace("search({})", dto);

        StringBuilder sqlQuery = new StringBuilder(SQL_SELECT_ALL_ARTICLES);
        if (dto.name() != null) {
            sqlQuery.append(" AND LOWER(designation) = LOWER('" + dto.name() + "')");
        }
        if (dto.description() != null) {
            sqlQuery.append(" AND LOWER(description) = LOWER('" + dto.description() + "')");
        }
        if (dto.minPrice() != null && dto.maxPrice() != null) {
            sqlQuery.append(" AND LOWER(price) >= LOWER('" + dto.minPrice() + "') AND LOWER(price) <= LOWER('" + dto.maxPrice() + "')");
        } else if (dto.minPrice() != null && dto.maxPrice() == null) {
            sqlQuery.append(" AND LOWER(price) >= LOWER('" + dto.minPrice() + "')");
        } else if (dto.maxPrice() != null && dto.minPrice() == null) {
            sqlQuery.append(" AND LOWER(price) <= LOWER('" + dto.maxPrice() + "')");
        }
        String sql = sqlQuery.toString();

        try {
            return jdbcTemplate.query(sql, this::mapRow);
        } catch (DataAccessException e) {
            LOG.error("Database access error occurred while searching for article: {}", e.getMessage(), e);
            throw new FatalException("Database access error occurred while searching for article: {} " + e.getMessage());
        }
    }


    @Override
    public Article get(Long id) throws NotFoundException {
        LOG.trace("get({})", id);
        List<Article> result = jdbcTemplate.query(SQL_SELECT_BY_ID, this::mapRow, id);

        if (result.size() > 1) {
            LOG.warn("To many articles with ID %d found".formatted(id));
            throw new FatalException("To many articles with ID %d found".formatted(id));
        }
        if (result == null || result.isEmpty()) {
            LOG.warn(("Could not found article with ID %d," + "because it does not exist").formatted(id));
            throw new NotFoundException(("Could not found article with ID %d," + "because it does not exist").formatted(id));
        }
        return result.getFirst();
    }

    @Override
    public Article update(ArticleUpdateDto dto) throws NotFoundException {
        LOG.trace("update({})", dto);
        int updated = 0;
        try {
            updated = jdbcTemplate.update(SQL_UPDATE,
                    dto.designation(), dto.description(), dto.price(), dto.image(), dto.id()
            );
        } catch (DataAccessException e) {
            LOG.error("Database access error occurred while updating article: {}", e.getMessage(), e);
            throw new FatalException("Database access error occurred while updating article: {}" + e.getMessage());
        }

        if (updated == 0) {
            throw new NotFoundException(("Could not update customer with ID %d,"
                    + "because it does not exist").formatted(dto.id()));
        }
        return new Article(dto.id(), dto.designation(), dto.description(), dto.price(), dto.image(), dto.imageType());
    }

    private Article mapRow(ResultSet result, int rowNum) throws SQLException {
        return new Article(
                result.getLong("id"),
                result.getString("designation"),
                result.getString("description"),
                result.getInt("price"),
                result.getString("image"),
                result.getString("image_type")
        );
    }
}
