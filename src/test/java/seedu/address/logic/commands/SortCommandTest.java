package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
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
        SortCommand sortCommand = prepareCommand(model);
        String expectedMessage = SortCommand.MESSAGE_SORT_SUCCESS;
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.sortFilteredPersonList(model.getFilteredPersonList());

        assertCommandSuccess(sortCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_twoNamesList_success() throws Exception {
        AddressBook ab = new AddressBookBuilder().withPerson(getTypicalPersons().get(1))
                .withPerson(getTypicalPersons().get(0)).build();
        Model modelWithTwoPersons = new ModelManager(ab, new UserPrefs());
        SortCommand sortCommand = prepareCommand(modelWithTwoPersons);
        String expectedMessage = SortCommand.MESSAGE_SORT_SUCCESS;
        Model expectedModel = new ModelManager(new AddressBook(modelWithTwoPersons.getAddressBook()), new UserPrefs());
        expectedModel.sortFilteredPersonList(model.getFilteredPersonList());

        assertCommandSuccess(sortCommand, modelWithTwoPersons, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        String expectedMessage = Messages.MESSAGE_EMPTY_PERSON_LIST;
        assertCommandFailure(prepareCommand(emptyModel), model, expectedMessage);
    }

    /**
     * Generates a new {@code SortCommand} with the Model given.
     */
    private SortCommand prepareCommand(Model model) {
        SortCommand sortCommand = new SortCommand();
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return sortCommand;
    }
}
