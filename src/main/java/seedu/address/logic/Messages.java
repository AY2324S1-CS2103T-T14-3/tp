package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_ID = "Please provide a valid ID";
    public static final String MESSAGE_INVALID_NAME = "Please provide a valid Name";
    public static final String MESSAGE_INVALID_ID_AND_NAME = "Please provide either a valid ID or Name";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The patient index provided is invalid";
    public static final String MESSAGE_PATIENTS_LISTED_OVERVIEW = "%1$d patients listed!";
    public static final String MESSAGE_NO_PATIENT_FOUND = "No such patient found!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_EMPTY_FIND_RESULT =
            "There are no FindCommand results. There is nothing to be saved to the logger tab.";

    public static final String MESSAGE_EMPTY_LOG =
            "No previous log to undo.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nName: ")
                .append(person.getName())
                .append("; ID: ")
                .append(person.getId())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append(";\n")
                .append("Appointment: ")
                .append(person.getAppointment().isPresent() ? person.getAppointment().get() : "")
                .append("; Medical Histories: ");

        person.getMedicalHistories().forEach(builder::append);
        return builder.toString();
    }
}
