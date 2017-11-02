package seedu.address.logic.commands.commandmode;

import java.io.IOException;

import seedu.address.logic.commands.CommandResult;
import seedu.address.storage.SecurityManager;

//@@author syy94
/**
 * Represents the modes that PasswordCommand is able to do
 */
public abstract class PasswordMode {
    private String pass;

    public PasswordMode(String pass) {
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    protected boolean passExists() {
        return SecurityManager.passExists();
    }

    public abstract CommandResult execute() throws IOException;
}

