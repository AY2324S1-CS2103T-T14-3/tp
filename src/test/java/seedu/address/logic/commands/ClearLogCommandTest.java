package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.LogBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;


public class ClearLogCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private List<UndoableCommand> commandHistory = new ArrayList<>();

    private class ModelStub implements Model {

        private LogBook logBook = new LogBook();

        @Override
        public LogBook getLogBook() {
            return logBook;
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setLogBook(LogBook logBook) {
            this.logBook.resetData(logBook);
        }

        @Override
        public ObservableList<Person> getFoundPersonsList() {
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
            commandHistory.add(command);
        }

        @Override
        public boolean isCommandHistoryEmpty() {
            return false;
        }

        @Override
        public UndoableCommand popCommandFromHistory() {
            return commandHistory.remove(commandHistory.size() - 1);
        }

        @Override
        public int getCommandHistorySize() {
            return commandHistory.size();
        }
    }

    @Test
    public void execute_clearLogBook_logBookCleared() throws Exception {
        ModelStub model = new ModelStub();
        ClearLogCommand clearLogCommand = new ClearLogCommand();
        CommandResult commandResult = clearLogCommand.execute(model);

        assertEquals(ClearLogCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
        assertEquals(model.getLogBook().getPersons().asUnmodifiableObservableList().size(), 0);
    }

    @Test
    public void undo_executeCommand_resultsUndone() throws Exception {
        ModelStub model = new ModelStub();
        LogBook logBookBeforeClear = model.getLogBook();

        ClearLogCommand clearLogCommand = new ClearLogCommand();
        clearLogCommand.execute(model);

        CommandResult undoResult = clearLogCommand.undo(model);

        assertEquals(ClearLogCommand.MESSAGE_UNDO_CLOG_SUCCESS, undoResult.getFeedbackToUser());
        assertEquals(model.getLogBook(), logBookBeforeClear);
    }

}
