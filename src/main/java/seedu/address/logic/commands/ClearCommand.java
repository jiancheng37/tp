package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.tag.TagRegistry;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears the data of the whole application.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        TagRegistry tagRegistry = TagRegistry.of();
        tagRegistry.setTags(new ArrayList<>());

        model.resetAllLists();

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
