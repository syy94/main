package seedu.address.model.person;

import java.util.Comparator;

public class ReadOnlyPersonComparator implements Comparator<ReadOnlyPerson> {

    @Override
    public int compare(ReadOnlyPerson a, ReadOnlyPerson b) {
        return a.getName().compareTo(b.getName());
    }

}
