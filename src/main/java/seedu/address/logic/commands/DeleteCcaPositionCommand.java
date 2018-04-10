package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_POSITION;
import static seedu.address.logic.parser.ParserUtil.parseCcaPosition;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Cca;
import seedu.address.model.person.CcaPosition;
import seedu.address.model.person.InjuriesHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameOfKin;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.subject.Subject;
import seedu.address.model.tag.Tag;

//@@author chuakunhong

/**
 * Edits the details of an existing person in the address book.
 */
public class DeleteCcaPositionCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteccapos";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": delete cca position from the student that you want. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CCA_POSITION + "CCA_POSITION\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CCA_POSITION + "Captain" + "\n";

    public static final String MESSAGE_CCA_POSITION_SUCCESS = "Cca position deleted: %1$s\nPerson: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public DeleteCcaPositionCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_CCA_POSITION_SUCCESS, editPersonDescriptor
                        .getCcaPosition().get(), personToEdit.getName()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToEdit = lastShownList.get(index.getZeroBased());
        editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
        throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Nric updatedNric = editPersonDescriptor.getNric().orElse(personToEdit.getNric());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Set<Subject> updatedSubjects = editPersonDescriptor.getSubjects().orElse(personToEdit.getSubjects());
        String[] ccaPositionArray = personToEdit.getCcaPosition().toString().split("\n");
        String  updateCcaPosition = "";
        NameOfKin updatedNameOfKin = editPersonDescriptor.getNameOfKin().orElse(personToEdit.getNameOfKin());
        Remark updatedRemark = editPersonDescriptor.getRemark()
                .orElse(personToEdit.getRemark());
        boolean ccaPositionIsFound = false;
        for (String ccaPosition:ccaPositionArray) {
            if (!ccaPosition.contains(editPersonDescriptor.getCcaPosition().get().toString())) {
                updateCcaPosition = updateCcaPosition + ccaPosition + "\n";
            } else {
                editPersonDescriptor.setCcaPosition(parseCcaPosition(ccaPosition));
                ccaPositionIsFound = true;
            }
        }
        if (ccaPositionIsFound) {
            CcaPosition updatedCcaPosition = parseCcaPosition(updateCcaPosition);
            Cca updatedCca = editPersonDescriptor.getCca().orElse(personToEdit.getCca());
            InjuriesHistory updatedInjuriesHistory = editPersonDescriptor.getInjuriesHistory()
                    .orElse(personToEdit.getInjuriesHistory());

            return new Person(updatedName, updatedNric, updatedTags, updatedSubjects, updatedRemark, updatedCca,
                    updatedInjuriesHistory, updatedNameOfKin, updatedCcaPosition);
        } else {
            throw new CommandException("The target cca position cannot be missing.");
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCcaPositionCommand)) {
            return false;
        }

        // state check
        DeleteCcaPositionCommand e = (DeleteCcaPositionCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }
    //@@author
}
