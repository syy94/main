# sofarsophie
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
        @Override
        public ObservableList<ReadOnlyPerson> sortFilteredPersonList(
                ObservableList<ReadOnlyPerson> unsortedList, Prefix prefix) {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void removeTag(Tag tag) {
            fail("This method should not be called.");
        }
```
###### \java\seedu\address\logic\commands\CommandTestUtil.java
``` java
        PersonContainsKeywordsPredicate.FindFields fieldsToFind = new FindFieldsBuilder().withName(splitName[0])
                .build();
        model.updateFilteredPersonList(new PersonContainsKeywordsPredicate(fieldsToFind));
```
###### \java\seedu\address\logic\commands\FindCommandTest.java
``` java
        PersonContainsKeywordsPredicate.FindFields fields = new FindFieldsBuilder().withName(userInput
                .split("\\s+")).build();
        FindCommand command =
                new FindCommand(new PersonContainsKeywordsPredicate(fields));
```
###### \java\seedu\address\logic\commands\SortCommandTest.java
``` java
package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.AddressBookBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SortCommand.
 */
public class SortCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_sort_success() throws Exception {
        Prefix prefix = PREFIX_ADDRESS;
        SortCommand sortCommand = prepareCommand(model, prefix);
        String expectedMessage = SortCommand.MESSAGE_SORT_SUCCESS;
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.sortFilteredPersonList(model.getFilteredPersonList(), prefix);

        assertCommandSuccess(sortCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_twoPersonsList_success() throws Exception {
        Prefix prefix = PREFIX_NAME;
        AddressBook ab = new AddressBookBuilder().withPerson(getTypicalPersons().get(1))
                .withPerson(getTypicalPersons().get(0)).build();
        Model modelWithTwoPersons = new ModelManager(ab, new UserPrefs());
        SortCommand sortCommand = prepareCommand(modelWithTwoPersons, prefix);
        String expectedMessage = SortCommand.MESSAGE_SORT_SUCCESS;
        Model expectedModel = new ModelManager(new AddressBook(modelWithTwoPersons.getAddressBook()), new UserPrefs());
        expectedModel.sortFilteredPersonList(model.getFilteredPersonList(), prefix);

        assertCommandSuccess(sortCommand, modelWithTwoPersons, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        Prefix prefix = PREFIX_EMAIL;
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        String expectedMessage = Messages.MESSAGE_EMPTY_PERSON_LIST;
        assertCommandFailure(prepareCommand(emptyModel, prefix), model, expectedMessage);
    }

    /**
     * Generates a new {@code SortCommand} with the Model and prefix given.
     */
    private SortCommand prepareCommand(Model model, Prefix prefix) {
        SortCommand sortCommand = new SortCommand(prefix);
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return sortCommand;
    }
}
```
###### \java\seedu\address\logic\parser\FindCommandParserTest.java
``` java
        FindCommand expectedFindCommand =
                new FindCommand(new PersonContainsKeywordsPredicate(fieldsToFind));
        assertParseSuccess(parser, FindCommand.COMMAND_WORD + " " + PREFIX_NAME + "Alice"
                + " " + PREFIX_NAME + "Bob", expectedFindCommand);

        // multiple whitespaces before and after keyword
        assertParseSuccess(parser, FindCommand.COMMAND_WORD + " " + PREFIX_NAME + "\n Alice \t"
                + " " + PREFIX_NAME + "   \t \t Bob", expectedFindCommand);
```
###### \java\seedu\address\logic\parser\SortCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.SortCommand;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_noArgs_returnSortCommand() {
        assertParseSuccess(parser, "", new SortCommand());
    }

    @Test
    public void parse_namePrefixArg_returnSortCommand() {
        assertParseSuccess(parser, PREFIX_NAME.toString(), new SortCommand(PREFIX_NAME));
    }

    @Test
    public void parse_validArg_returnSortCommand() {
        assertParseSuccess(parser, PREFIX_ADDRESS.toString(), new SortCommand(PREFIX_ADDRESS));
    }

    @Test
    public void parse_invalidArg_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleArgs_throwsParseException() {
        assertParseFailure(parser, PREFIX_EMAIL.toString() + PREFIX_GROUP.toString(),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

}
```
###### \java\seedu\address\model\person\PersonContainsKeywordsPredicateTest.java
``` java
        // Keywords match phone, email and address, but does not match name
        PersonContainsKeywordsPredicate.FindFields differentMatchFields = new FindFieldsBuilder().withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main").withAddress("Street").build();
        predicate =
                new PersonContainsKeywordsPredicate(differentMatchFields);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main Street").build()));
```
###### \java\seedu\address\testutil\FindFieldsBuilder.java
``` java
package seedu.address.testutil;

import java.util.Arrays;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.person.PersonContainsKeywordsPredicate.FindFields;

/**
 * A utility class to help with building FindFields objects.
 */
public class FindFieldsBuilder {

    private FindFields fieldsToFind;

    public FindFieldsBuilder() {
        fieldsToFind = new FindFields();
    }

    public FindFieldsBuilder(FindFields fieldsToFind) {
        this.fieldsToFind = fieldsToFind;
    }

    /**
     * Sets the {@code Name} of the {@code FindFields} that we are building.
     */
    public FindFieldsBuilder withName(String... names) {
        try {
            fieldsToFind.setNameKeywords(ParserUtil.parseNames(Arrays.asList(names)));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("names are expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code FindFields} that we are building.
     */
    public FindFieldsBuilder withPhone(String... phones) {
        try {
            fieldsToFind.setPhoneKeywords(ParserUtil.parsePhones(Arrays.asList(phones)));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("phone is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code FindFields} that we are building.
     */
    public FindFieldsBuilder withEmail(String... emails) {
        try {
            fieldsToFind.setEmailKeywords(ParserUtil.parseEmails(Arrays.asList(emails)));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("email is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code FindFields} that we are building.
     */
    public FindFieldsBuilder withAddress(String... addresses) {
        try {
            fieldsToFind.setAddressKeywords(ParserUtil.parseAddresses(Arrays.asList(addresses)));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("address is expected to be unique.");
        }
        return this;
    }

    public FindFields build() {
        return fieldsToFind;
    }
}
```
