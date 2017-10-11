package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that any of {@code ReadOnlyPerson}'s Name, Phone, Email or Address matches any of the keywords given.
 */
public class PersonFieldContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<String> keywords;

    public PersonFieldContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        return ( keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword)
                || StringUtil.containsWordIgnoreCase(person.getPhone().toString(), keyword)
                || StringUtil.containsWordIgnoreCase(person.getEmail().toString(), keyword)
                || StringUtil.containsWordIgnoreCase(person.getAddress().toString(), keyword) ) );

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonFieldContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((PersonFieldContainsKeywordsPredicate) other).keywords)); // state check
    }

}
