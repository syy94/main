package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_PASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_PASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASS;

import java.io.IOException;

import seedu.address.logic.commands.commandmode.PasswordMode;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.storage.SecurityManager;

/**
 * Adds, removes or edit password for the application.
 */
public class PasswordCommand extends Command {

    public static final String COMMAND_WORD = "password";
    public static final String COMMAND_ALIAS = "pwd";


    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds, removes or changes passwords.\n"
            + "Set Password: " + COMMAND_WORD + " " + PREFIX_PASS + "PASS\n"
            + "Change Password: " + COMMAND_WORD + " " + PREFIX_PASS + "PASS" + " " + PREFIX_NEW_PASS + "NEW_PASS\n"
            + "Clear Password: " + COMMAND_WORD + " " + PREFIX_PASS + "PASS" + " " + PREFIX_CLEAR_PASS;

    public static final String MESSAGE_FILE_NOT_FOUND = "Password file not found.";
    public static final String MESSAGE_WRONG_PASS = "Wrong password.";
    public static final String MESSAGE_CHANGED_PASS = "Password Changed";
    public static final String MESSAGE_PASS_EXISTS = "Password Exists. To change password, use " + COMMAND_WORD + " "
            + PREFIX_PASS + "[existing password] "
            + PREFIX_NEW_PASS + "[new password] ";
    public static final String MESSAGE_CLEARED_PASS = "Password Cleared";
    public static final String MESSAGE_SET_PASS = "Password Set";
    public static final String MESSAGE_PASS_NOT_CHANGED = "Password not changed.";
    public static final String MESSAGE_NO_PASS_TO_CLEAR = "No existing password to clear";
    public static final String MESSAGE_NO_PASS_TO_CHANGE = "No existing password to change";

    final PasswordMode mode;

    public PasswordCommand(PasswordMode mode) {
        this.mode = mode;
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute() throws CommandException {
        try {
            return mode.execute();
        } catch (IOException e) {
            throw new CommandException(MESSAGE_FILE_NOT_FOUND);
        }
    }

    /**
     * PasswordMode to set Password
     */
    public static class SetPass extends PasswordMode {
        public SetPass(String pass) {
            super(pass);
        }

        @Override
        public CommandResult execute() throws IOException {
            if (passExists()) {
                return new CommandResult(MESSAGE_PASS_EXISTS);
            } else {
                SecurityManager.savePass(getPass());
                return new CommandResult(MESSAGE_SET_PASS);
            }
        }
    }

    /**
     * PasswordMode to clear Password
     */
    public static class ClearPass extends PasswordMode {
        public ClearPass(String pass) {
            super(pass);
        }

        @Override
        public CommandResult execute() throws IOException {
            if (passExists()) {
                if (SecurityManager.checkPass(getPass())) {
                    SecurityManager.removePass();
                    return new CommandResult(MESSAGE_CLEARED_PASS);
                } else {
                    return new CommandResult(MESSAGE_WRONG_PASS);
                }
            } else {
                return new CommandResult(MESSAGE_NO_PASS_TO_CLEAR);
            }
        }
    }

    /**
     * PasswordMode to change Password
     */
    public static class ChangePass extends PasswordMode {

        private String newPass;

        public ChangePass(String pass, String newPass) {
            super(pass);
            this.newPass = newPass;
        }

        @Override
        public CommandResult execute() throws IOException {
            if (passExists()) {
                if (SecurityManager.checkPass(getPass())) {
                    SecurityManager.savePass(newPass);
                    return new CommandResult(MESSAGE_CHANGED_PASS);
                } else {
                    return new CommandResult(MESSAGE_WRONG_PASS + "\n" + MESSAGE_PASS_NOT_CHANGED);
                }
            } else {
                return new CommandResult(MESSAGE_NO_PASS_TO_CHANGE);
            }
        }
    }
}
