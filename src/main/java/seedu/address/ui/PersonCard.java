package seedu.address.ui;

import java.util.HashMap;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.util.ColorUtil;
import seedu.address.model.group.Group;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    //@@author syy94
    private static final HashMap<String, String> TAG_COLORS = new HashMap<String, String>();
    //@@author
    //@@author kengying
    private static final HashMap<String, String> GROUP_COLORS = new HashMap<String, String>();
    //@@author

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyPerson person;

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

    public PersonCard(ReadOnlyPerson person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        //@@author syy94
        initTags(person);
        //@@author
        //@@author kengying
        initGroups(person);
        //@@author
        bindListeners(person);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        group.textProperty().bind(Bindings.convert(person.groupProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
            initTags(person);
            initGroups(person);
        });
    }

    //@@author syy94
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
    //@@author

    //@@author kengying
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
    //@@author

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
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
