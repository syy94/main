package seedu.address.model.customFields;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.exceptions.IllegalValueException;

public class CustomField {
    private String field;
    private String value;

    public static final String MESSAGE_FIELD_CONSTRAINTS =
            "Custom fields should be 2 alphanumeric strings separated by ':'";
    public static final String FIELD_VALIDATION_REGEX = "[\\w]+:[\\w]+";

    public CustomField(String field) throws IllegalValueException{
        requireAllNonNull(field);
        final String trimmedField = field.trim();
        if(!isValidField(trimmedField)){
            throw new IllegalValueException(MESSAGE_FIELD_CONSTRAINTS);
        }
        final String[] fieldData = trimmedField.split(":", 2);
        requireAllNonNull(fieldData[0], fieldData[1]);
        this.field = fieldData[0];
        this.value = fieldData[1];
    }

    /**
     * Returns if a given string is a valid custom field.
     */
    public static boolean isValidField(String test) {
        return test.matches(FIELD_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object obj) {
        final CustomField other = (CustomField) obj;
        return field.equals(other.field) && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, value);
    }

    @Override
    public String toString() {
        return field + ":" + value;
    }
}
