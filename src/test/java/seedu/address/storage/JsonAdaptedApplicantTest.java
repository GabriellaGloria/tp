package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedApplicant.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.applicant.*;

public class JsonAdaptedApplicantTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_JOB_POSITION = "Front-end Developer&&";
    private static final String INVALID_STATUS = "&Accepted!";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_JOB_POSITION = BENSON.getJobPosition().toString();
    private static final String VALID_STATUS = BENSON.getStatus().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final LocalDateTime VALID_ADDED_TIME = BENSON.getAddedTime();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedApplicant person =
                new JsonAdaptedApplicant(INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_JOB_POSITION, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(null, VALID_PHONE, VALID_EMAIL,
                VALID_JOB_POSITION, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                VALID_JOB_POSITION, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, null, VALID_EMAIL,
                VALID_JOB_POSITION, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                VALID_JOB_POSITION, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, null,
                VALID_JOB_POSITION, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidJobPosition_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                INVALID_JOB_POSITION, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = JobPosition.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullJobPosition_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, JobPosition.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidStatus_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_JOB_POSITION, INVALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = Status.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullStatus_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_JOB_POSITION, null, VALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_JOB_POSITION, VALID_STATUS, INVALID_ADDRESS, VALID_ADDED_TIME, VALID_TAGS);
        System.out.println(VALID_STATUS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_JOB_POSITION, VALID_STATUS, null, VALID_ADDED_TIME, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedApplicant person = new JsonAdaptedApplicant(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_JOB_POSITION, VALID_STATUS, VALID_ADDRESS, VALID_ADDED_TIME, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
