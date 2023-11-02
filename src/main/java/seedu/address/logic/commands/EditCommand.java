package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String COMMAND_WORD_ALIAS = "e";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " or " + COMMAND_WORD_ALIAS
            + ": Edits the details of the Person identified "
            + "by the name or NRIC of the Patient.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Format: edit n/NAME or id/IC_NUMBER [Fields] ...\n"
            + "Example 1: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "91234567 \n"
            + "Example 2: " + COMMAND_WORD_ALIAS + " "
            + PREFIX_NAME + "Alex Yeoh "
            + PREFIX_PHONE + "82786151 \n";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Patient: %1$s";
    public static final String MESSAGE_UNDO_EDIT_PERSON_SUCCESS = "Undoing the Editing of Patient:  %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.\n"
            + "Fields include phone (p/), email (e/), "
            + "address (a/), appointment (ap/) and medical history (m/)\n"
            + "Name and NRIC cannot be edited\n";
    public static final String MESSAGE_PERSON_NOT_FOUND = "INVALID name and/or Nric!\n"
            + "The given combination of Name and/or NRIC does not match any person in the Patient list.";

    private static final Logger logger = Logger.getLogger(EditCommand.class.getName());

    /**
     * The original state of the person before it was edited by the command.
     */
    private Person originalPerson;

    /**
     * The edited state of the person after it was modified by the command.
     */
    private Person editedPerson;

    private final Name name;
    private final Nric nric;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param name of the person in the filtered person list to edit
     * @param nric of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Name name, Nric nric, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(editPersonDescriptor);
        this.name = name;
        this.nric = nric;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getUnfilteredPersonList();
        Optional<Person> personOptional = CommandUtil.findPersonByIdentifier(name, nric, lastShownList);

        if (personOptional.isEmpty()) {
            logger.log(Level.WARNING, "Person not found for editing");
            throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
        }

        Person personToEdit = personOptional.get();
        logger.log(Level.INFO, "Editing person: {0}", personToEdit);

        originalPerson = personToEdit;
        editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        logger.log(Level.INFO, "EditCommand executed successfully");

        model.addToHistory(this);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public CommandResult undo(Model model) {
        requireNonNull(model);
        model.setPerson(editedPerson, originalPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_UNDO_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    public static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name name = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Nric nric = editPersonDescriptor.getNric().orElse(personToEdit.getNric());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Appointment updatedAppointment = editPersonDescriptor.getAppointment()
                .orElse(personToEdit.getAppointment().orElse(null));
        Set<MedicalHistory> updatedMedicalHistories =
                editPersonDescriptor.getMedicalHistories().orElse((personToEdit.getMedicalHistories()));
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(name, nric, updatedPhone, updatedEmail, updatedAddress,
                updatedAppointment, updatedMedicalHistories, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;

        // Check if both the name and nric are equal for equality check
        boolean isNameEqual = Objects.equals(name, otherEditCommand.name);

        boolean isNricEqual = Objects.equals(nric, otherEditCommand.nric);


        return isNameEqual && isNricEqual;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("nric", nric)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;

        private Nric nric;
        private Phone phone;
        private Email email;
        private Address address;

        private Appointment appointment;

        private Set<MedicalHistory> medicalHistories;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setNric(toCopy.nric);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setAppointment(toCopy.appointment);
            setMedicalHistories(toCopy.medicalHistories);
            setTags(toCopy.tags);
        }


        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Nric> getNric() {
            return Optional.ofNullable(nric);
        }
        public void setNric(Nric nric) {
            this.nric = nric;
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setAppointment(Appointment appointment) {
            this.appointment = appointment;
        }

        public Optional<Appointment> getAppointment() {
            return Optional.ofNullable(appointment);
        }

        /**
         * Sets {@code medicalHistories} to this object's {@code medicalHistories}.
         * A defensive copy of {@code medicalHistories} is used internally.
         */
        public void setMedicalHistories(Set<MedicalHistory> medicalHistories) {
            this.medicalHistories = (medicalHistories != null) ? new HashSet<>(medicalHistories) : null;
        }

        /**
         * Returns an unmodifiable medicalHistories set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code medicalHistories} is null.
         */
        public Optional<Set<MedicalHistory>> getMedicalHistories() {
            return (medicalHistories != null)
                    ? Optional.of(Collections.unmodifiableSet(medicalHistories)) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(nric, otherEditPersonDescriptor.nric)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(appointment, otherEditPersonDescriptor.appointment)
                    && Objects.equals(medicalHistories, otherEditPersonDescriptor.medicalHistories)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(phone, email, address, medicalHistories, tags, appointment);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("nric", nric)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("appointment", appointment)
                    .add("medicalHistories", medicalHistories)
                    .add("tags", tags)
                    .toString();
        }
    }
}
