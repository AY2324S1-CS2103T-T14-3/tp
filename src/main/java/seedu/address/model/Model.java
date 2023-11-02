package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {

    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Adds an {@code UndoableCommand} to the command history stack.
     *
     * @param command The undoable command to be added to the command history stack.
     */
    void addToHistory(UndoableCommand command);

    /**
     * Checks if the command history stack is empty.
     *
     * @return {@code true} if the command history stack is empty, {@code false} otherwise.
     */
    boolean isCommandHistoryEmpty();

    /**
     * Pops an {@code UndoableCommand} from the command history stack.
     *
     * @return The {@code UndoableCommand} popped from the command history stack.
     */
    UndoableCommand popCommandFromHistory();

    /**
     * Gets the size of the stack of commandHistory.
     *
     * @return The size of the stack of commandHistory.
     */
    int getCommandHistorySize();

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the unfiltered person list */
    ObservableList<Person> getUnfilteredPersonList();

    /** Returns an unmodifiable view of the logged filtered person list saved when user execute Log Command*/
    ObservableList<Person> getLoggedFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    LogBook getLogBook();

    void updateFoundPersonsList(Predicate<Person> predicate);

    List<Person> getFoundPersonsList();

    void setLogBook(LogBook logBook);

}
