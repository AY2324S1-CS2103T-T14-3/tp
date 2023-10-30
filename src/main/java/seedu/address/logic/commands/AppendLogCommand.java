package seedu.address.logic.commands;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Appends the results of a FindCommand to the logbook.
 */
public class AppendLogCommand extends Command {

    public static final String COMMAND_WORD = "alog";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Appends the results of the most recent FindCommand to the logbook.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Results of the FindCommand have been appended to the logbook.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        // Ensure that there are results from the most recent FindCommand
        if (model.getFoundPersonsList().isEmpty()) {
            throw new CommandException(Messages.MESSAGE_EMPTY_FIND_RESULT);
        }

        for (Person person : model.getFoundPersonsList()) {
            model.getLogBook().addPerson(person);
        }

        return new CommandResult(MESSAGE_SUCCESS);

    }
}