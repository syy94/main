package seedu.address.model.person;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Comparator;
import seedu.address.logic.parser.Prefix;

/**
 * A comparator for comparing two ReadOnlyPerson objects by a given field identified the prefix.
 * Compares by Name by default.
 */
public class ReadOnlyPersonComparator implements Comparator<ReadOnlyPerson> {

    private static Prefix compareByPrefix;

    public ReadOnlyPersonComparator(Prefix compareByPrefix) {
        this.compareByPrefix = compareByPrefix;
    }

    public ReadOnlyPersonComparator compareByPrefix(Prefix compareByPrefix) {
        this.compareByPrefix = compareByPrefix;
        return this;
    }

    @Override
    public int compare(ReadOnlyPerson a, ReadOnlyPerson b) {
        if(compareByPrefix.equals(PREFIX_ADDRESS))
            return a.getAddress().compareTo(b.getAddress());
        else if(compareByPrefix.equals(PREFIX_EMAIL))
            return a.getEmail().compareTo(b.getEmail());
        else if(compareByPrefix.equals(PREFIX_PHONE))
            return a.getPhone().compareTo(b.getPhone());
        else if(compareByPrefix.equals(PREFIX_GROUP))
            return a.getGroup().compareTo(b.getGroup());
        else if(compareByPrefix.equals(PREFIX_NAME))
            return a.getName().compareTo(b.getName());

        return 0;
    }

}
