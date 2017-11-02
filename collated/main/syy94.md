# syy94
###### /java/seedu/address/commons/events/ui/PasswordAcceptedEvent.java
``` java
/**
 * Represents the password is accepted.
 */
public class PasswordAcceptedEvent extends BaseEvent {
    /**
     * All Events should have a clear unambiguous custom toString message so that feedback message creation
     * stays consistent and reusable.
     * <p>
     * For example, the event manager post method will call any posted event's toString and print it in the console.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### /java/seedu/address/logic/commands/commandmode/PasswordMode.java
``` java
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

```
###### /java/seedu/address/logic/commands/PasswordCommand.java
``` java
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
```
###### /java/seedu/address/logic/LogicManager.java
``` java
        if (isLocked) {
            try {
                isLocked = !SecurityManager.unlock(commandText);
            } catch (IOException e) {
                throw new CommandException("Unable to open password file");
            }
            if (isLocked) {
                return new CommandResult("Wrong Password");
            } else {
                return new CommandResult("Welcome");
            }
        }
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case PasswordCommand.COMMAND_WORD:
        case PasswordCommand.COMMAND_ALIAS:
            return new PasswordCommandParser().parse(arguments);
```
###### /java/seedu/address/logic/parser/CliSyntax.java
``` java
    public static final Prefix PREFIX_ADD_TAG = new Prefix("+t/");
    public static final Prefix PREFIX_REMOVE_TAG = new Prefix("-t/");
    public static final Prefix PREFIX_CUSTOM_FIELD = new Prefix("c/");
    public static final Prefix PREFIX_CLEAR_TAG = new Prefix("clearTag/");
    public static final Prefix PREFIX_PASS = new Prefix("pwd/");
    public static final Prefix PREFIX_NEW_PASS = new Prefix("new/");
    public static final Prefix PREFIX_CLEAR_PASS = new Prefix("clearPwd/");
```
###### /java/seedu/address/logic/parser/PasswordCommandParser.java
``` java
/**
 * Parses input arguments and creates a new PasswordCommand object
 */
public class PasswordCommandParser implements Parser<PasswordCommand> {
    /**
     * Parses {@code userInput} into a command and returns it.
     *
     * @param args
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    @Override
    public PasswordCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASS, PREFIX_NEW_PASS, PREFIX_CLEAR_PASS);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PasswordCommand.MESSAGE_USAGE));
        }

        if (arePrefixesPresent(argMultimap, PREFIX_NEW_PASS)) {
            final String newPass = argMultimap.getValue(PREFIX_NEW_PASS).get();

            requireNonNull(newPass);

            if (newPass.length() == 0) {
                throw new ParseException("New Password Cannot be empty!");
            }

            return new PasswordCommand(new PasswordCommand.ChangePass(
                    argMultimap.getValue(PREFIX_PASS).get(),
                    newPass
            ));
        } else if (arePrefixesPresent(argMultimap, PREFIX_CLEAR_PASS)) {
            return new PasswordCommand(new PasswordCommand.ClearPass(argMultimap.getValue(PREFIX_PASS).get()));
        } else {
            return new PasswordCommand(new PasswordCommand.SetPass(argMultimap.getValue(PREFIX_PASS).get()));
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### /java/seedu/address/storage/SecurityManager.java
``` java
/**
 * Accesses and edits the password.
 */
public class SecurityManager {
    private static String path = "data/pass";

    public static void setPath(String path) {
        SecurityManager.path = path;
    }

    /**
     * Saves password
     */
    public static void savePass(String pass) throws IOException {
        requireNonNull(pass);
        final File passFile = new File(path);

        final String trimmedPass = pass.trim();
        if (trimmedPass.length() != 0) {
            FileUtil.createIfMissing(passFile);
            FileUtil.writeToFile(passFile, trimmedPass);
        }
    }

    /**
     * Checks if given password is correct and posts and event to enable the application features.
     */
    public static boolean unlock(String pass) throws IOException {
        boolean isUnlocked = checkPass(pass);
        if (isUnlocked) {
            EventsCenter.getInstance().post(new PasswordAcceptedEvent());
        }
        return isUnlocked;
    }

    /**
     * Checks if given password is correct.
     */
    public static boolean checkPass(String pass) throws IOException {
        return getPass().equals(pass);
    }

    /**
     * removes the need for password on application start.
     */
    public static void removePass() {
        final File passFile = new File(path);
        if (FileUtil.isFileExists(passFile)) {
            passFile.delete();
        }
    }

    /**
     * Checks if there is a password.
     */
    public static boolean passExists() {
        final File passFile = new File(path);

        return FileUtil.isFileExists(passFile);
    }

    /**
     * Retrieves the Password.
     *
     * @throws IOException
     */
    private static String getPass() throws IOException {
        final File file = new File(path);
        return FileUtil.readFromFile(file);
    }
}
```
###### /java/seedu/address/ui/PersonListPanel.java
``` java
        this.personList = personList;
        if (!SecurityManager.passExists()) {
            init();
        }
```
###### /java/seedu/address/ui/PersonListPanel.java
``` java
    private void init() {
        setConnections(personList);
        registerAsAnEventHandler(this);
    }
```
###### /java/seedu/address/ui/PersonListPanel.java
``` java
    @Subscribe
    private void handlePasswordAcceptedEvent(PasswordAcceptedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Password Accepted. Showing Persons"));
        init();
    }
```
###### /java/seedu/address/ui/ResultDisplay.java
``` java
        if (SecurityManager.passExists()) {
            displayed.setValue("Enter Password:");
        }
```
