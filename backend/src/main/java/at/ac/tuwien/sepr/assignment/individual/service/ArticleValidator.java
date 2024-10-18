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

    public void validateForCreate (ArticleCreateDto dto) throws ValidationException, NotFoundException, ConflictException {
        List<String> validationErrors = validate(dto);
        List<String> conflictErrors = new ArrayList<>();
        if (articleDao.designationExists(dto.designation())){
            conflictErrors.add("Article with given designation already exists");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of article for creation failed", validationErrors);
        }
        if (!conflictErrors.isEmpty()) {
            throw new ConflictException("Creation of article contains conflicts", conflictErrors);
        }
    }

    public void validateForUpdate (ArticleUpdateDto dto ) throws ValidationException, NotFoundException, ConflictException {
        ArticleCreateDto toCheck = new ArticleCreateDto(
                dto.designation(),
                dto.description(),
                dto.price());
        List<String> validationErrors = validate(toCheck);
        List<String> conflictErrors = new ArrayList<>();
        if (dto.id() == null) {
            validationErrors.add("No ID for updating given");
        }
        if (!(articleDao.designationExists(dto.designation()))){
            conflictErrors.add("Designation for specific article does not exist");
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of customer for update failed", validationErrors);
        }
        if (!conflictErrors.isEmpty()) {
            throw new ConflictException("Update for customer contains conflicts", conflictErrors);
        }
    }

    private List<String> validate (ArticleCreateDto dto) throws NotFoundException {
        List<String> errorList = new ArrayList<>();

        if (dto == null) {
            throw new NotFoundException("Article not found");
        }
        if (dto.designation() == null || dto.designation().isEmpty()) {
            errorList.add("designation is required");
        }
        if (dto.description() == null || dto.description().isEmpty()) {
            errorList.add("description is required");
        }
        if (dto.price() == null || dto.price().describeConstable().isEmpty()){
            errorList.add("price is required");
        }
        if (dto.designation().length() > 256){
            errorList.add("the designation can only have 256 characters");
        }
        if (dto.description().length() > 256){
            errorList.add("the description can only have 255 characters");
        }
        if (dto.price() < 0){
            errorList.add("price can not be negative");
        }
        if (!(isValidDesignation(dto.designation()))){
            errorList.add("designation can only have alphanumeric characters");
        }
        if ((!isValidDescription(dto.description()))){
            errorList.add("description can only have alphanumeric characters");
        }

        if (!(dto.price() instanceof Integer)){
            errorList.add("price can only be in valid integer format");
        }
        if (!isValidPrice(dto.price())){
            errorList.add("The format of the price is not correct or exceeds the limit of 999999999.99");
        }

        //TODO optional image validation
        return errorList;
    }

    private boolean isValidDesignation(String designation){
        String nameRegex = "^[a-zA-Z0-9À-ÿ '-]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        return pattern.matcher(designation).matches();
    }

    private boolean isValidDescription(String description){
        String nameRegex = "^[a-zA-Z0-9À-ÿ '-]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        return pattern.matcher(description).matches();
    }

    private boolean isValidPrice(Integer price){
        String nameRegex = "^(?!0\\d)\\d{1,9}(\\.\\d{1,2})?$";
        Pattern pattern = Pattern.compile(nameRegex);
        String priceStr = String.valueOf(price);
        return pattern.matcher(priceStr).matches();
    }
}
