package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.LogBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class LogCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private Name defaultName = new Name(PersonBuilder.DEFAULT_NAME);

    private Nric defaultNric = new Nric(PersonBuilder.DEFAULT_NRIC);

    private final ObservableList<Person> foundPersonsList = FXCollections.observableArrayList();
    private final List<UndoableCommand> commandHistory = new ArrayList<>();


    private class ModelStub implements Model {

        @Override
        public LogBook getLogBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setLogBook(LogBook logBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFoundPersonsList() {
            // Return an empty list or a list with some persons if needed for your test
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFoundPersonsList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getUnfilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getLoggedFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addToHistory(UndoableCommand command) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isCommandHistoryEmpty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public UndoableCommand popCommandFromHistory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public int getCommandHistorySize() {
            throw new AssertionError("This method should not be called.");
        }

    }

    private class ModelStubLoggingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();
        private LogBook logBook = new LogBook();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
            logBook.addPerson(person);
        }

        @Override
        public LogBook getLogBook() {
            return logBook;
        }

        @Override
        public ObservableList<Person> getFoundPersonsList() {
            return foundPersonsList;
        }

        @Override
        public void addToHistory(UndoableCommand undoableCommand) {
            commandHistory.add(undoableCommand);
        }

        @Override
        public boolean isCommandHistoryEmpty() {
            return false;
        }

        @Override
        public void setLogBook(LogBook logBook) {
            this.logBook.resetData(logBook);
        }
    }

    private class ModelStubNoPersonsFound extends ModelStubLoggingPersonAdded {
        @Override
        public ObservableList<Person> getFoundPersonsList() {
            return FXCollections.observableArrayList(); // Return an empty list
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(); // Return an empty list
        }

        @Override
        public boolean isCommandHistoryEmpty() {
            return false;
        }
    }

    @Test
    public void execute_resultsLoggedToLogBook_logSuccessful() throws Exception {
        ModelStubLoggingPersonAdded model = new ModelStubLoggingPersonAdded();
        Person personToLog = new PersonBuilder().withName("Amy Bee").build();
        model.addPerson(personToLog);
        foundPersonsList.add(personToLog);

        LogCommand logCommand = new LogCommand();

        CommandResult commandResult = logCommand.execute(model);

        assertEquals(LogCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
        assertEquals(model.getLogBook().getPersonList().size(), 1);
    }

    @Test
    public void execute_noPersonsFound_logNotExecuted() throws AssertionError {
        ModelStubNoPersonsFound model = new ModelStubNoPersonsFound();
        LogCommand logCommand = new LogCommand();

        assertThrows(AssertionError.class, () -> logCommand.execute(model));

        assertEquals(0, model.getLogBook().getPersonList().size());
    }

    @Test
    public void undo_executeCommand_resultsUndone() throws Exception {
        ModelStubLoggingPersonAdded model = new ModelStubLoggingPersonAdded();
        Person personToLog = new PersonBuilder().withName("Amy Bee").build();

        foundPersonsList.add(personToLog);

        LogCommand logCommand = new LogCommand();

        logCommand.execute(model);

        CommandResult undoResult = logCommand.undo(model);

        assertEquals(LogCommand.MESSAGE_UNDO_LOG_SUCCESS, undoResult.getFeedbackToUser());

        assertEquals(model.getLogBook().getPersons().asUnmodifiableObservableList().size(), 0);
    }


}
