# syy94
###### \java\seedu\address\commons\events\ui\PasswordAcceptedEvent.java
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
###### \java\seedu\address\commons\util\ColorUtil.java
``` java
/**
 * From JavaFX docs, the CSS supports HSB color model instead of HSL color model.
 * <p>
 * HSL is used here as it is easier to randomise a certain color range as compared to
 * RGB.
 *
 * @see <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#introlimitations">
 * </a>
 */
public class ColorUtil {
    private static Random random = new Random();

    public static String getTagColor() {
        return "hsb(" + getHue() + "," + getSaturation() + "%,"
                + getBrightness() + "%)";
    }

    private static int getHue() {
        //full spectrum of colors (in Degrees)
        return random.nextInt(360);
    }

    private static int getSaturation() {
        //60 to 95%
        return random.nextInt(35) + 60;
    }

    private static int getBrightness() {
        //50 to 75%
        return random.nextInt(25) + 50;
    }
}
```
###### \java\seedu\address\commons\util\StringUtil.java
``` java
    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case and partial words are accepted.
     *   <br>examples:<pre>
     *       containsPartialTextIgnoreCase("ABc def", "abc") == true
     *       containsPartialTextIgnoreCase("ABc def", "DEF") == true
     *       containsPartialTextIgnoreCase("ABc def", "AB") == true
     *       containsPartialTextIgnoreCase("ABc def", "c de") == true
     *       containsPartialTextIgnoreCase("ABc def", "cde") == false
     *       </pre>
     * @param sentence cannot be null
     * @param text cannot be null, cannot be empty
     */
    public static boolean containsPartialTextIgnoreCase(String sentence, String text) {
        requireNonNull(sentence);
        requireNonNull(text);

        String preppedWord = text.trim().toLowerCase();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");

        String preppedSentence = sentence.trim().toLowerCase();

        return preppedSentence.contains(preppedWord);
    }
```
###### \java\seedu\address\logic\commands\commandmode\PasswordMode.java
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
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
        Set<CustomField> updatedFields = editPersonDescriptor.getFieldsList().orElse(personToEdit.getFields());

        final Set<Tag> updatedTags = new HashSet<>();

        if (!editPersonDescriptor.shouldClear()) {
            updatedTags.addAll(personToEdit.getTags());
        }

        editPersonDescriptor.getToAdd().ifPresent(updatedTags::addAll);
        editPersonDescriptor.getToRemove().ifPresent(updatedTags::removeAll);
```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
        public void setFieldsList(Set<CustomField> fieldsList) {
            this.fieldsList = fieldsList;
        }

        public Optional<Set<CustomField>> getFieldsList() {
            return Optional.ofNullable(fieldsList);
        }

        public Optional<Set<Tag>> getToAdd() {
            return Optional.ofNullable(toAdd);
        }

        public Optional<Set<Tag>> getToRemove() {
            return Optional.ofNullable(toRemove);
        }

        public boolean shouldClear() {
            return clearTags;
        }

        public void setClearTags(boolean clearTags) {
            this.clearTags = clearTags;
        }

        public void setToAdd(Set<Tag> toAdd) {
            this.toAdd = toAdd;
        }

        public void setToRemove(Set<Tag> toRemove) {
            this.toRemove = toRemove;
        }
```
###### \java\seedu\address\logic\commands\PasswordCommand.java
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
###### \java\seedu\address\logic\LogicManager.java
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
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case PasswordCommand.COMMAND_WORD:
        case PasswordCommand.COMMAND_ALIAS:
            return new PasswordCommandParser().parse(arguments);
```
###### \java\seedu\address\logic\parser\CliSyntax.java
``` java
    public static final Prefix PREFIX_ADD_TAG = new Prefix("+t/");
    public static final Prefix PREFIX_REMOVE_TAG = new Prefix("-t/");
    public static final Prefix PREFIX_CUSTOM_FIELD = new Prefix("c/");
    public static final Prefix PREFIX_CLEAR_TAG = new Prefix("clearTag/");
    public static final Prefix PREFIX_PASS = new Prefix("pwd/");
    public static final Prefix PREFIX_NEW_PASS = new Prefix("new/");
    public static final Prefix PREFIX_CLEAR_PASS = new Prefix("clearPwd/");
```
###### \java\seedu\address\logic\parser\FindCommandParser.java
``` java
    /**
     * Parses {@code List<String> fields} into a {@code List<CustomField>} if {@code fields} is non-empty.
     * If {@code fields} contain only one element which is an empty string, it will be parsed into a
     * {@code List<Address>} containing zero addresses.
     */
    private Optional<List<String>> parseCustomFieldsForSearch(List<String> fields) throws IllegalValueException {
        assert fields != null;

        if (fields.isEmpty()) {
            return Optional.empty();
        }
        List<String> fieldList = fields.size() == 1 && fields.contains("")
                ? Collections.emptyList() : fields;
        return Optional.of(fieldList);
    }
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     *
     * Parses {@code Collection<String> fields} into a {@code Set<{@link CustomField}>}.
     */
    public static Set<CustomField> parseCustomFields(Collection<String> fields) throws IllegalValueException {
        requireNonNull(fields);

        final Set<CustomField> fieldSet = new HashSet<>();
        for (String field : fields) {
            fieldSet.add(parseCustomField(field));
        }
        return fieldSet;
    }

    private static CustomField parseCustomField(String field) throws IllegalValueException {
        requireNonNull(field);
        return new CustomField(field);
    }
```
###### \java\seedu\address\logic\parser\PasswordCommandParser.java
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
###### \java\seedu\address\model\customfields\CustomField.java
``` java
/**
 * Represents a field as created by the user in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidField(String)}
 */
public class CustomField {
    public static final String MESSAGE_FIELD_CONSTRAINTS =
            "Custom fields should be 2 alphanumeric strings separated by ':'";
    public static final String FIELD_VALIDATION_REGEX = "[\\w]+:[\\w]+";

    public final String key;
    public final String value;

    public CustomField(String field) throws IllegalValueException {
        requireAllNonNull(field);
        final String trimmedField = field.trim();
        if (!isValidField(trimmedField)) {
            throw new IllegalValueException(MESSAGE_FIELD_CONSTRAINTS);
        }
        final String[] fieldData = trimmedField.split(":", 2);
        requireAllNonNull(fieldData[0], fieldData[1]);
        this.key = fieldData[0];
        this.value = fieldData[1];
    }

    /**
     * Returns if a given string is a valid custom field.
     */
    public static boolean isValidField(String test) {
        return test.matches(FIELD_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object obj) {
        final CustomField other = (CustomField) obj;
        return key.equals(other.key) && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }

    /**
     * returns a String representation of custom fields as a sentence.
     *
     * @see seedu.address.model.person.PersonContainsKeywordsPredicate#test(ReadOnlyPerson)
     */
    public String asData() {
        return key + " " + value;
    }
}
```
###### \java\seedu\address\model\person\Person.java
``` java
    public void setFields(Set<CustomField> replacement) {
        fieldsList.set(new CustomFieldsList(replacement));
    }

    @Override
    public ObjectProperty<CustomFieldsList> fieldsListProperty() {
        return fieldsList;
    }

    @Override
    public Set<CustomField> getFields() {
        return Collections.unmodifiableSet(fieldsList.get().toSet());
    }
```
###### \java\seedu\address\storage\SecurityManager.java
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
###### \java\seedu\address\storage\XmlAdaptedCustomField.java
``` java
/**
 * JAXB-friendly adapted version of the Field.
 */
public class XmlAdaptedCustomField {
    @XmlValue
    private String customField;

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedCustomField() {
    }

    public XmlAdaptedCustomField(CustomField field) {
        this.customField = field.toString();
    }

    public CustomField toModelType() throws IllegalValueException {
        return new CustomField(customField);
    }
}
```
###### \java\seedu\address\ui\PersonCard.java
``` java
    private static final HashMap<String, String> TAG_COLORS = new HashMap<String, String>();
```
###### \java\seedu\address\ui\PersonCard.java
``` java
        initTags(person);
```
###### \java\seedu\address\ui\PersonCard.java
``` java
    /**
     * Creates and add the tags belonging to the person
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            final Label label = new Label(tag.tagName);
            label.setStyle("-fx-background-color: " + getTagColor(tag));
            tags.getChildren().add(label);
        });
    }

    private String getTagColor(Tag tag) {
        //TODO store the tag colors for consistent tag colors
        if (!TAG_COLORS.containsKey(tag.tagName)) {
            TAG_COLORS.put(tag.tagName, ColorUtil.getTagColor());
        }

        return TAG_COLORS.get(tag.tagName);
    }
```
###### \java\seedu\address\ui\PersonDetailsPanel.java
``` java
/**
 * Panel containing the details of the selected person.
 */
public class PersonDetailsPanel extends UiPart<Region> {
    private static final String FXML = "PersonDetailsPanel.fxml";
    private static final HashMap<String, String> TAG_COLORS = new HashMap<String, String>();

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private ReadOnlyPerson person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label group;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private VBox fields;

    //TODO Empty View
    public PersonDetailsPanel() {
        super(FXML);
        registerAsAnEventHandler(this);
    }

    public PersonDetailsPanel(ReadOnlyPerson person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        initTags(person);
        initFields(person);
        bindListeners(person);
        registerAsAnEventHandler(this);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        group.textProperty().bind(Bindings.convert(person.groupProperty()));
        group.setStyle("-fx-background-color: Blue");
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));

        person.fieldsListProperty().addListener((observable, oldValue, newValue) -> {
            regenerateFields(person);
        });

        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            regenerateTags(person);
        });
    }

    /**
     * Creates and add the tags belonging to the person
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            final Label label = new Label(tag.tagName);
            label.setStyle("-fx-background-color: " + getTagColor(tag));
            tags.getChildren().add(label);
        });
    }

    /**
     * Creates and add the tags belonging to the person
     */
    private void initFields(ReadOnlyPerson person) {
        person.getFields().forEach(field -> {
            final Label label = new Label(field.key + ": " + field.value);
            fields.getChildren().add(label);
        });
    }

    private String getTagColor(Tag tag) {
        //TODO store the tag colors for consistent tag colors
        if (!TAG_COLORS.containsKey(tag.tagName)) {
            TAG_COLORS.put(tag.tagName, ColorUtil.getTagColor());
        }

        return TAG_COLORS.get(tag.tagName);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonDetailsPanel panel = (PersonDetailsPanel) other;
        return id.getText().equals(panel.id.getText())
                && person.equals(panel.person);
    }

    private void setPerson(ReadOnlyPerson person) {
        this.person = person;
        bindListeners(person);
        regenerateTags(person);
        regenerateFields(person);
    }

    private void regenerateTags(ReadOnlyPerson person) {
        tags.getChildren().clear();
        initTags(person);
    }

    private void regenerateFields(ReadOnlyPerson person) {
        fields.getChildren().clear();
        initFields(person);
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        setPerson(event.getNewSelection().person);
    }
}
```
###### \java\seedu\address\ui\PersonListPanel.java
``` java
        this.personList = personList;
        if (!SecurityManager.passExists()) {
            init();
        }
```
###### \java\seedu\address\ui\PersonListPanel.java
``` java
    private void init() {
        setConnections(personList);
        registerAsAnEventHandler(this);
    }
```
###### \java\seedu\address\ui\PersonListPanel.java
``` java
    @Subscribe
    private void handlePasswordAcceptedEvent(PasswordAcceptedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Password Accepted. Showing Persons"));
        init();
    }
```
###### \java\seedu\address\ui\ResultDisplay.java
``` java
        if (SecurityManager.passExists()) {
            displayed.setValue("Enter Password:");
        }
```
###### \resources\view\MainWindow.fxml
``` fxml
    <VBox>
        <StackPane fx:id="personDetailsPlaceholder" prefWidth="340">
          <padding>
            <Insets top="10" right="10" bottom="10" left="10" />
          </padding>
        </StackPane>

      <StackPane fx:id="browserPlaceholder" prefWidth="340" maxHeight="400">
        <padding>
          <Insets top="10" right="10" bottom="10" left="10" />
        </padding>
      </StackPane>
    </VBox>
```
###### \resources\view\PersonDetailsPanel.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ScrollPane?>
<?import javafx.scene.layout.FlowPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.VBox?>
<ScrollPane VBox.vgrow="ALWAYS" minHeight="250" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1"
            stylesheets="@DarkTheme.css">
    <content>
        <VBox stylesheets="@DarkTheme.css">
            <padding>
                <Insets top="5" right="5" bottom="5" left="15"/>
            </padding>
            <HBox spacing="5" alignment="CENTER_LEFT">
                <Label fx:id="name" text="\$first" styleClass="cell_big_label"/>
                <Label fx:id="group" text="\$group" styleClass="cell_big_label"/>
            </HBox>
            <FlowPane fx:id="tags"/>
            <Label fx:id="phone" styleClass="cell_small_label" text="\$phone"/>
            <Label fx:id="address" styleClass="cell_small_label" text="\$address"/>
            <Label fx:id="email" styleClass="cell_small_label" text="\$email"/>
            <!--fields are generated and added in the codes as they are of a variable number-->
            <VBox fx:id="fields" styleClass="cell_small_label"/>
        </VBox>
    </content>
</ScrollPane>
```
