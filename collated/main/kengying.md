# kengying
###### /java/seedu/address/logic/commands/EditCommand.java
``` java
        public void setGroup(Group group) {
            this.group = group;
        }

        public Optional<Group> getGroup() {
            return Optional.ofNullable(group);
        }
```
###### /java/seedu/address/logic/commands/ListCommand.java
``` java
/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all persons whose tags "
            + "contain any of the specified keywords\n"
            + "or list all persons with or without tags"
            + "(case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: TAG [TAG]\n"
            + "Example: " + COMMAND_WORD + " friends\n"
            + COMMAND_WORD + " all";;

    private Predicate<ReadOnlyPerson> predicate = PREDICATE_SHOW_ALL_PERSONS;

    public ListCommand(PersonContainsTagsPredicate predicate) {
        this.predicate = predicate;
    }

    public ListCommand() {
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListCommand // instanceof handles nulls
                && this.predicate.equals(((ListCommand) other).predicate)); // state check
    }
}
```
###### /java/seedu/address/logic/parser/AddCommandParser.java
``` java
    /**
     * Returns null if prefixes contains empty values in the given
     * {@value}.
     */
    private static Optional<String> areValuePresent(Optional<String> value) {
        value = Optional.of(value.orElse(""));
        return value;
    }
```
###### /java/seedu/address/logic/parser/ListCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        } else if (trimmedArgs.equalsIgnoreCase("all")) {
            return new ListCommand();
        }

        String[] keywords = trimmedArgs.split("\\s+");

        return new ListCommand(new PersonContainsTagsPredicate(Arrays.asList(keywords)));
    }

}
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> group} into an {@code Optional<Group>} if {@code group} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Group> parseGroup(Optional<String> group) throws IllegalValueException {
        requireNonNull(group);
        return group.isPresent() ? Optional.of(new Group(group.get())) : Optional.empty();
    }
```
###### /java/seedu/address/model/AddressBook.java
``` java
    public void setGroups(List<? extends Group> persons) {
        this.groups.setGroups(groups);
    }
```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Ensures that every group in this person:
     *  - exists in the master list {@link #groups}
     *  - points to a Group object in the master list
     */
    private void syncMasterGroupListWith(Person person) {
        final UniqueGroupList personGroup = new UniqueGroupList(person.getGroup());
        groups.mergeFrom(personGroup);
    }

    /**
     * Ensures that every group in these persons:
     *  - exists in the master list {@link #tags}
     *  - points to a Group object in the master list
     *  @see #syncMasterGroupListWith(Person)
     */
    private void syncMasterGroupListWith(UniquePersonList persons) {
        persons.forEach(this::syncMasterGroupListWith);
    }
    //@@ author

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(ReadOnlyPerson key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

```
###### /java/seedu/address/model/AddressBook.java
``` java
    //// group-level operations
    public void addGroup(Group g) throws UniqueGroupList.DuplicateGroupException {
        groups.add(g);
    }
```
###### /java/seedu/address/model/AddressBook.java
``` java
    @Override
    public ObservableList<Group> getGroupList() {
        return groups.asObservableList();
    }
```
###### /java/seedu/address/model/group/Group.java
``` java
/**
 * Represents a Person's group in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGroup(String)}
 */
public class Group implements Comparable<Group>, Comparator<Group> {

    public static final String MESSAGE_GROUP_CONSTRAINTS =
            "Person groups should only contain alphanumeric characters and spaces, and it should not be blank";
    public static final String GROUP_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String groupName;

    /**
     * Validates given group.
     *
     * @throws IllegalValueException if given group address string is invalid.
     */
    public Group(String group) throws IllegalValueException {
        requireNonNull(group);
        String trimmedGroup = group.trim();
        if (!isValidGroup(trimmedGroup)) {
            throw new IllegalValueException(MESSAGE_GROUP_CONSTRAINTS);
        }
        this.groupName = trimmedGroup;
    }

    public Group(Group group) throws IllegalValueException {
        this(group.toString());
    }

    /**
     * Returns if a given string is a valid person group.
     */
    public static boolean isValidGroup(String test) {
        return test.matches(GROUP_VALIDATION_REGEX);
    }

    public int compareTo(Group g) {
        return this.groupName.compareTo(g.groupName);
    }

    public int compare(Group a, Group b) {
        return a.groupName.compareTo(b.groupName);
    }

    @Override
    public String toString() {
        return groupName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Group // instanceof handles nulls
                && this.groupName.equals(((Group) other).groupName)); // state check
    }

    @Override
    public int hashCode() {
        return groupName.hashCode();
    }

}
```
###### /java/seedu/address/model/group/UniqueGroupList.java
``` java
/**
 * A list of tags that enforces no nulls and uniqueness between its elements.
 *
 * Supports minimal set of list operations for the app's features.
 *
 * @see Group#equals(Object)
 */
public class UniqueGroupList implements Iterable<Group> {

    private final ObservableList<Group> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty TagList.
     */
    public UniqueGroupList() {}

    /**
     * Creates a UniqueTagList using given tags.
     * Enforces no nulls.
     */
    public UniqueGroupList(Group groups) {
        requireAllNonNull(groups);
        internalList.addAll(groups);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Replaces the Groups in this list with those in the argument tag list.
     */
    public void setGroups(UniqueGroupList groups) {
        requireAllNonNull(groups);
        internalList.setAll(groups.internalList);
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    public void setGroups(List<? extends Group> groups) throws DuplicateGroupException {
        final UniqueGroupList replacement = new UniqueGroupList();
        for (final Group group : groups) {
            replacement.add(group);
        }
        setGroups(replacement);
    }
    /**
     * Ensures every tag in the argument list exists in this object.
     */
    public void mergeFrom(UniqueGroupList from) {
        final UniqueGroupList alreadyInside = this;
        from.internalList.stream()
                .filter(group -> !alreadyInside.contains(group))
                .forEach(internalList::add);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns true if the list contains an equivalent Group as the given argument.
     */
    public boolean contains(Group toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a Tag to the list.
     *
     * @throws DuplicateGroupException if the Group to add is a duplicate of an existing Tag in the list.
     */
    public void add(Group toAdd) throws DuplicateGroupException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateGroupException();
        }
        internalList.add(toAdd);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    @Override
    public Iterator<Group> iterator() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.iterator();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Group> asObservableList() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        return other == this // short circuit if same object
                || (other instanceof UniqueGroupList // instanceof handles nulls
                        && this.internalList.equals(((UniqueGroupList) other).internalList));
    }

    @Override
    public int hashCode() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.hashCode();
    }

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateGroupException extends DuplicateDataException {
        protected DuplicateGroupException() {
            super("Operation would result in duplicate groups");
        }
    }

}
```
###### /java/seedu/address/model/person/Person.java
``` java
    public void setGroup(Group group) {
        this.group.set(requireNonNull(group));
    }

    @Override
    public ObjectProperty<Group> groupProperty() {
        return group;
    }

    @Override
    public Group getGroup() {
        return group.get();
    }
```
###### /java/seedu/address/model/person/PersonContainsTagsPredicate.java
``` java
/**
 * Tests that any of {@code ReadOnlyPerson}'s Name, Phone, Email or Address matches any of the keywords given.
 */
public class PersonContainsTagsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<String> keywords;

    public PersonContainsTagsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        return (keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getTags().toString()
                        .replace("[", "").replace("]", "")
                        .replace(",", ""), keyword)
                        || StringUtil.containsWordIgnoreCase(person.getGroup().groupName, keyword)));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonContainsTagsPredicate // instanceof handles nulls
                && this.keywords.equals(((PersonContainsTagsPredicate) other).keywords)); // state check
    }
```
###### /java/seedu/address/model/person/ReadOnlyPerson.java
``` java
    ObjectProperty<Group> groupProperty();
    Group getGroup();
```
###### /java/seedu/address/model/ReadOnlyAddressBook.java
``` java
    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Group> getGroupList();
```
###### /java/seedu/address/storage/XmlAdaptedGroup.java
``` java
/**
 * JAXB-friendly adapted version of the Group.
 */
public class XmlAdaptedGroup {

    @XmlValue
    private String groupName;

    /**
     * Constructs an XmlAdaptedGroup.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedGroup() {}

    /**
     * Converts a given Tag into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedGroup(Group source) {
        groupName = source.groupName;
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Tag object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Group toModelType() throws IllegalValueException {
        return new Group(groupName);
    }

}
```
###### /java/seedu/address/storage/XmlSerializableAddressBook.java
``` java
    @Override
    public ObservableList<Group> getGroupList() {
        final ObservableList<Group> tags = this.groups.stream().map(g -> {
            try {
                return g.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return FXCollections.unmodifiableObservableList(tags);
    }
```
###### /java/seedu/address/ui/PersonCard.java
``` java
    private void initGroups(ReadOnlyPerson person) {
        group.setStyle("-fx-background-color: " + getGroupColor(person.getGroup()));
    }

    private String getGroupColor(Group group) {
        //TODO store the group colors for consistent group colors
        if (!GROUP_COLORS.containsKey(group.groupName)) {
            GROUP_COLORS.put(group.groupName, ColorUtil.getTagColor());
        }

        return GROUP_COLORS.get(group.groupName);
    }
```
