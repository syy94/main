package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMOVE_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.customfields.CustomField;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
//TODO javadoc
public class EditTagCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "editTag";
    public static final String COMMAND_ALIAS = "tag";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_USAGE = "Edits the tags of the person identified "
            + "by the index number used in the last person listing.\n"
            + PREFIX_CLEAR + " : Clears all the tags of the person before processing the adding and removing of "
            + "tags.\n"
            + "[" + PREFIX_ADD_TAG + "TAG]..." + " : Adds tag to Person.\n"
            + "[" + PREFIX_REMOVE_TAG + "TAG]..." + " : Removes tag from Person.\n";

    public static final String MESSAGE_ADD_TAG_SUCCESS = "Edited tags of Person: %1$s";

    private Index index;
    private EditTagsDescriptor descriptor;

    /**
     * @param index      of the person in the filtered person list to edit
     * @param descriptor tags to edit the person with
     */
    public EditTagCommand(Index index, EditTagsDescriptor descriptor) {
        this.index = index;
        this.descriptor = descriptor;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, descriptor);

        try {
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_TAG_SUCCESS, editedPerson));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(ReadOnlyPerson personToEdit,
                                             EditTagsDescriptor descriptor) {
        assert personToEdit != null;

        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Email updatedEmail = personToEdit.getEmail();
        Address updatedAddress = personToEdit.getAddress();
        Set<CustomField> updatedFields = personToEdit.getFields();

        Set<Tag> updatedTags = createFinalTagList(personToEdit.getTags(), descriptor);

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedFields, updatedTags);
    }
    //TODO javadoc
    public static Set<Tag> createFinalTagList(Set<Tag> tagsToEdit, EditTagsDescriptor descriptor) {
        final Set<Tag> result = new HashSet<>();

        if (!descriptor.shouldClear()) {
            result.addAll(tagsToEdit);
        }

        descriptor.getToAdd().ifPresent(result::addAll);
        descriptor.getToRemove().ifPresent(result::removeAll);

        return result;
    }
    //TODO javadoc
    public static class EditTagsDescriptor {
        //takes 1st priority
        private boolean clear = false;
        //takes 2nd priority
        private Set<Tag> toAdd;
        //always happen last
        private Set<Tag> toRemove;

        public EditTagsDescriptor() {
        }

        public EditTagsDescriptor(EditTagsDescriptor toCopy) {
            this.clear = toCopy.clear;
            this.toAdd = toCopy.toAdd;
            this.toRemove = toCopy.toRemove;
        }

        public boolean isAnyTagsEdited() {
            return CollectionUtil.isAnyNonNull(toAdd, toRemove) || clear;
        }

        public Optional<Set<Tag>> getToAdd() {
            return Optional.ofNullable(toAdd);
        }

        public Optional<Set<Tag>> getToRemove() {
            return Optional.ofNullable(toRemove);
        }

        public boolean shouldClear() {
            return clear;
        }

        public void setClear(boolean clear) {
            this.clear = clear;
        }

        public void setToAdd(Set<Tag> toAdd) {
            this.toAdd = toAdd;
        }

        public void setToRemove(Set<Tag> toRemove) {
            this.toRemove = toRemove;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTagsDescriptor)) {
                return false;
            }

            // state check
            EditTagsDescriptor e = (EditTagsDescriptor) other;

            return clear == e.clear
                    && getToAdd().equals(e.getToAdd())
                    && getToRemove().equals(e.getToRemove());
        }
    }
}
