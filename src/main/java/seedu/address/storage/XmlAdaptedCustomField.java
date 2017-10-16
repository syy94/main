package seedu.address.storage;

import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.customFields.CustomField;

public class XmlAdaptedCustomField {
    @XmlValue
    private String customField;

    public XmlAdaptedCustomField(CustomField field) {
        this.customField = field.toString();
    }

    public CustomField toModelType() throws IllegalValueException{
        return new CustomField(customField);
    }
}
