package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Tag;

public class TagTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void givenValidValues_whenCreatingTag_thenTagIsCreatedWithThoseValues() {
        String name = "Tag123";

        Tag tag = new Tag(name);

        assertEquals("Tag123", tag.getName());
    }

    @Test
    public void givenEmptyName_whenCreatingTag_thenThrowExecption() {
        String name = null;

        Tag tag = new Tag(name);

        Set<ConstraintViolation<Tag>> violations = validator.validate(tag);

        assertEquals(1, violations.size());
        ConstraintViolation<Tag> violation = violations.iterator().next();
        assertEquals("Name cannot be empty.", violation.getMessage());
    }

}
