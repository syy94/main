package seedu.address.model.person;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.customfields.CustomField;

/**
 * Tests that any of {@code ReadOnlyPerson}'s Name, Phone, Email or Address matches any of the keywords given.
 */
public class PersonContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<String> keywords;

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        return (keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword)
                || StringUtil.containsWordIgnoreCase(person.getPhone().toString(), keyword)
                || StringUtil.containsWordIgnoreCase(person.getEmail().toString(), keyword)
                || StringUtil.containsWordIgnoreCase(person.getAddress().toString(), keyword)
                || customFieldContainsWordIgnoreCase(person.getFields(), keyword)));

    }

    private boolean customFieldContainsWordIgnoreCase(Set<CustomField> fields, String keyword) {
        return fields.stream().anyMatch(field -> StringUtil.containsWordIgnoreCase(field.asData(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((PersonContainsKeywordsPredicate) other).keywords)); // state check
    }

}
