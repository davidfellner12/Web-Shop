package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.ArticleCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleUpdateDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.persistence.ArticleDao;
import at.ac.tuwien.sepr.assignment.individual.persistence.CustomerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ArticleValidator {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArticleDao articleDao;
    private final View error;

    public ArticleValidator(ArticleDao articleDao, View error) {
        this.articleDao = articleDao;
        this.error = error;
    }

    public void validateForCreate(ArticleCreateDto dto) throws ValidationException, NotFoundException, ConflictException {
        List<String> validationErrors = validate(dto);
        List<String> conflictErrors = new ArrayList<>();
        List<String> validationErrorsForImage = new ArrayList<>();
        if (dto.image() != null) {
            validationErrorsForImage = isValidImageType(dto.imageType(), dto.image());
        }

        if (articleDao.designationExists(dto.designation())) {
            LOG.warn("Article with given designation already exists");
            conflictErrors.add("Article with given designation already exists");
        }

        if (!validationErrors.isEmpty() || !validationErrorsForImage.isEmpty()) {
            LOG.warn("Validation of article for creation failed");
            throw new ValidationException("Validation of article for creation failed", validationErrors);
        }
        if (!conflictErrors.isEmpty()) {
            LOG.warn("Creation of article contains conflicts");
            throw new ConflictException("Creation of article contains conflicts", conflictErrors);
        }
    }

    public void validateForUpdate(ArticleUpdateDto dto) throws ValidationException, NotFoundException, ConflictException {
        ArticleCreateDto toCheck = new ArticleCreateDto(
                dto.designation(),
                dto.description(),
                dto.price(),
                dto.image(),
                dto.imageType());
        List<String> validationErrors = validate(toCheck);
        List<String> conflictErrors = new ArrayList<>();
        if (dto.id() == null) {
            LOG.warn("NO ID for updating given");
            validationErrors.add("No ID for updating given");
        }
        if (!(articleDao.designationExists(dto.designation()))) {
            LOG.warn("Article with given designation does not exist");
            conflictErrors.add("Designation for specific article does not exist");
        }
        if (!validationErrors.isEmpty()) {
            LOG.warn("Validation of article for creation failed");
            throw new ValidationException("Validation of article for update failed", validationErrors);
        }
        if (!conflictErrors.isEmpty()) {
            LOG.warn("Validation of article contains conflicts");
            throw new ConflictException("Update for article contains conflicts", conflictErrors);
        }
    }

    private List<String> validate(ArticleCreateDto dto) throws NotFoundException {
        List<String> errorList = new ArrayList<>();

        if (dto == null) {
            LOG.warn("Article not found");
            throw new NotFoundException("Article not found");
        }
        if (dto.designation() == null || dto.designation().isEmpty()) {
            LOG.warn("designation is required");
            errorList.add("designation is required");
        }
        if (dto.description() == null || dto.description().isEmpty()) {
            LOG.warn("description is required");
            errorList.add("description is required");
        }
        if (dto.price() == null || dto.price().describeConstable().isEmpty()) {
            LOG.warn("price is required");
            errorList.add("price is required");
        }
        if (dto.designation().length() > 256) {
            LOG.warn("the designation can only have 256 charaterecs");
            errorList.add("the designation can only have 256 characters");
        }
        if (dto.description().length() > 256) {
            LOG.warn("the description can only have 256 charaterecs");
            errorList.add("the description can only have 255 characters");
        }
        if (dto.price() < 0) {
            LOG.warn("price can not be negative");
            errorList.add("price can not be negative");
        }
        if (!(isValidDesignation(dto.designation()))) {
            LOG.warn("designation can only have alphanumeric characters");
            errorList.add("designation can only have alphanumeric characters");
        }
        if ((!isValidDescription(dto.description()))) {
            LOG.warn("description can only have alphanumeric characters");
            errorList.add("description can only have alphanumeric characters");
        }

        if (!(dto.price() instanceof Integer)) {
            LOG.warn("price can only be in valid integer format");
            errorList.add("price can only be in valid integer format");
        }
        if (!isValidPrice(dto.price())) {
            LOG.warn("The format of the price is not correct or exceeds the limit of 999999999.99");
            errorList.add("The format of the price is not correct or exceeds the limit of 999999999.99");
        }

        //TODO optional image validation
        return errorList;
    }

    private ArrayList<String> isValidImageType(String imageType, String image) {
        ArrayList<String> validationErrors = new ArrayList<>();
        if (imageType == null && image != null) {
            validationErrors.add("Image type is null although image exists.");
        } else if (image != null && !imageType.equals("png")) {
            validationErrors.add("Image type '" + imageType + "' is not supported. Use PNG Image type instead.");
        }
        return validationErrors;
    }

    private boolean isValidDesignation(String designation) {
        String nameRegex = "^[a-zA-Z0-9À-ÿ '-]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        return pattern.matcher(designation).matches();
    }

    private boolean isValidDescription(String description) {
        String nameRegex = "^[a-zA-Z0-9À-ÿ '-]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        return pattern.matcher(description).matches();
    }

    private boolean isValidPrice(Integer price) {
        String nameRegex = "^(?!0\\d)\\d{1,9}(\\.\\d{1,2})?$";
        Pattern pattern = Pattern.compile(nameRegex);
        String priceStr = String.valueOf(price);
        return pattern.matcher(priceStr).matches();
    }
}
