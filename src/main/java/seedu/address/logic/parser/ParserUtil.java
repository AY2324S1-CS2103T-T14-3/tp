package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String nric} into a {@code Nric}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code nric} is invalid.
     */
    public static Nric parseNric(String nric) throws ParseException {
        requireNonNull(nric);
        String trimmedNric = nric.trim();
        if (!Nric.isValidNric(trimmedNric)) {
            throw new ParseException(Nric.MESSAGE_CONSTRAINTS);
        }
        return new Nric(trimmedNric);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into a {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */

    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String appointment} into an {@code Appointment}, if it exists.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code appointment} is invalid.
     */
    public static Appointment parseAppointmentIfExists(Optional<String> appointment) throws ParseException {
        if (appointment.isEmpty()) {
            return null;
        }
        return parseAppointment(appointment.get());
    }

    /**
     * Parses a {@code String appointment} into an {@code Appointment}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code appointment} is invalid.
     */
    public static Appointment parseAppointment(String appointment) throws ParseException {
        requireNonNull(appointment);
        String trimmedAppointment = appointment.trim();
        if (!Appointment.isValidAppointment(trimmedAppointment)) {
            throw new ParseException(Appointment.MESSAGE_CONSTRAINTS);
        }
        return new Appointment(trimmedAppointment);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String medicalHistory} into a {@code medicalHistory}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static MedicalHistory parseMedicalHistory(String medicalHistory) throws ParseException {
        requireNonNull(medicalHistory);
        String trimmedMedicalHistory = medicalHistory.trim();
        if (!MedicalHistory.isValidMedicalHistory(medicalHistory)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new MedicalHistory(trimmedMedicalHistory);
    }

    /**
     * Parses a collection of medical history strings and returns a Set of MedicalHistory objects.
     * @param medicalHistoryStrings The collection of medical history strings.
     * @return A Set of MedicalHistory.
     * @throws ParseException if any medical history format is invalid.
     */
    public static Set<MedicalHistory> parseMedicalHistories(Collection<String> medicalHistoryStrings)
            throws ParseException {
        requireNonNull(medicalHistoryStrings);

        final Set<MedicalHistory> medicalHistories = new HashSet<>();
        for (String medicalHistoryString : medicalHistoryStrings) {
            if (medicalHistoryString.isBlank()) {
                throw new ParseException("Medical history cannot be empty.");
            }
            medicalHistories.add(new MedicalHistory(medicalHistoryString));
        }
        return medicalHistories;

    /**
     * Parses a {@code String medHistory} into a {@code MedicalHistory}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code medHistory} is invalid.
     */
    public static MedicalHistory parseMedical(String medHistory) throws ParseException {
        requireNonNull(medHistory);
        String trimmedHistory = medHistory.trim();
        if (!MedicalHistory.isValidMedicalHistory(trimmedHistory)) {
            throw new ParseException(MedicalHistory.MESSAGE_CONSTRAINTS);
        }
        return new MedicalHistory(trimmedHistory);
    }

    /**
     * Parses {@code Collection<String> medHistories} into {@code Set<MedicalHistory>}.
     */
    public static Set<MedicalHistory> parseMedicals(Collection<String> medHistories) throws ParseException {
        requireNonNull(medHistories);
        final Set<MedicalHistory> historiesSet = new HashSet<>();
        for (String historyName : medHistories) {
            historiesSet.add(parseMedical(historyName));
        }
        return historiesSet;
    }
}
