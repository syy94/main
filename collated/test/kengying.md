# kengying
###### \java\seedu\address\logic\commands\ListCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_zeroKeyword_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        ListCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_oneTags_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        ListCommand command = prepareCommand("owesMoney");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_oneGroup_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        ListCommand command = prepareCommand("Car");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ELLE));
    }

    @Test
    public void execute_twoTags_sevenPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        ListCommand command = prepareCommand("owesMoney friend");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    @Test
    public void execute_twoGroups_sevenPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        ListCommand command = prepareCommand("Car Savings");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    @Test
    public void execute_oneTagGroup_sevenPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        ListCommand command = prepareCommand("Car owesMoney");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON, ELLE));
    }

    @Test
    public void execute_wrongKeyword_zeroPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        ListCommand command = prepareCommand("sdfsdf");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    /**
     * Parses {@code userInput} into a {@code ListCommand}.
     */
    private ListCommand prepareCommand(String userInput) {
        ListCommand command =
                new ListCommand(new PersonContainsTagsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(ListCommand command, String expectedMessage, List<ReadOnlyPerson> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }

}
```
###### \java\seedu\address\logic\parser\AddCommandParserTest.java
``` java
        // invalid field
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + GROUP_DESC_SAVING + INVALID_FIELD_DESC
                + VALID_TAG_FRIEND, CustomField.MESSAGE_FIELD_CONSTRAINTS);

        // invalid group
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + INVALID_GROUP_DESC + VALID_TAG_FRIEND, Group.MESSAGE_GROUP_CONSTRAINTS);
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_list() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        ListCommand command = (ListCommand) parser.parseCommand(
                ListCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new ListCommand(new PersonContainsTagsPredicate(keywords)), command);
    }

    @Test
    public void parseCommandAlias_list() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        ListCommand command = (ListCommand) parser.parseCommand(
                ListCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new ListCommand(new PersonContainsTagsPredicate(keywords)), command);
    }
```
###### \java\seedu\address\model\AddressBookTest.java
``` java
    @Test
    public void getGroupList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getGroupList().remove(0);
    }
```
###### \java\seedu\address\model\AddressBookTest.java
``` java
        @Override
        public ObservableList<Group> getGroupList() {
            return groups;
        }
```
###### \java\seedu\address\testutil\EditPersonDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Group} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withGroup(String groupHealth) {
        try {
            ParserUtil.parseGroup(Optional.of(groupHealth)).ifPresent(descriptor::setGroup);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("group is expected to be unique.");
        }
        return this;
    }
```
###### \java\seedu\address\testutil\PersonBuilder.java
``` java
    /**
     * Sets the {@code Group} of the {@code Person} that we are building.
     */
    public PersonBuilder withGroup(String group) {
        try {
            this.person.setGroup(new Group(group));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("group is expected to be unique.");
        }
        return this;
    }
```
###### \java\systemtests\EditCommandSystemTest.java
``` java
        /* Case: invalid group -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + INVALID_GROUP_DESC,
                Group.MESSAGE_GROUP_CONSTRAINTS);
```
