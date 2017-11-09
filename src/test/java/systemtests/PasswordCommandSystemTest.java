package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_PASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_PASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASS;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PasswordCommand;
import seedu.address.model.Model;

public class PasswordCommandSystemTest extends AddressBookSystemTest {
    private static final String oldPass = "oldPass";
    private static final String newPass = "newPass";

    @Test
    public void passwordTest() {
        //Clear Password without pwd/ prefix
        String command = PasswordCommand.COMMAND_WORD + " " + PREFIX_CLEAR_PASS + oldPass;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PasswordCommand.MESSAGE_USAGE));

        //Edit Password without pwd/ prefix
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_NEW_PASS + oldPass;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PasswordCommand.MESSAGE_USAGE));

        //Set Password Success
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + oldPass;
        assertCommandSuccess(command, PasswordCommand.MESSAGE_SET_PASS);

        //Set Password again
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + oldPass;
        assertCommandFailure(command, PasswordCommand.MESSAGE_PASS_EXISTS);

        //Edit Password
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + oldPass + " " + PREFIX_NEW_PASS + newPass;
        assertCommandSuccess(command, PasswordCommand.MESSAGE_CHANGED_PASS);

        //Edit Password back to oldPass for convenience.
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + newPass + " " + PREFIX_NEW_PASS + oldPass;
        assertCommandSuccess(command, PasswordCommand.MESSAGE_CHANGED_PASS);

        //Edit Password with wrong existing password
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + newPass + " " + PREFIX_NEW_PASS + oldPass;
        assertCommandFailure(command, PasswordCommand.MESSAGE_WRONG_PASS
                + "\n" + PasswordCommand.MESSAGE_PASS_NOT_CHANGED);

        //Remove Password with wrong existing password
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + newPass + " " + PREFIX_CLEAR_PASS;
        assertCommandFailure(command, PasswordCommand.MESSAGE_WRONG_PASS);

        //Remove Password Success
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + oldPass + " " + PREFIX_CLEAR_PASS;
        assertCommandSuccess(command, PasswordCommand.MESSAGE_CLEARED_PASS);

        //No existing Password to remove
        command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + oldPass + " " + PREFIX_CLEAR_PASS;
        assertCommandFailure(command, PasswordCommand.MESSAGE_NO_PASS_TO_CLEAR);
    }

    private void createPasswordForUnlockTest() {
        String command = PasswordCommand.COMMAND_WORD + " " + PREFIX_PASS + oldPass;
        assertCommandSuccess(command, PasswordCommand.MESSAGE_SET_PASS);
    }

    private void assertCommandSuccess(String command, String expectedCommandMessage) {
        assertCommandSuccess(command, expectedCommandMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 5. Asserts that the status bar's sync status remains unchanged.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, String expectedCommandMessage, Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedCommandMessage, getModel());
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
