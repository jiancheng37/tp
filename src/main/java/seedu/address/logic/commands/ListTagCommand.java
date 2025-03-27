package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TAGS;

import java.util.ArrayList;

import seedu.address.model.Model;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Lists all tags in the address book to the user.
 */
public class ListTagCommand extends Command {

    public static final String COMMAND_WORD = "listTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all Tags\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all tags";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        PriceRange.setFilteredAgainst(null);
        Tag.setActiveSearchTags(new ArrayList<>());

        model.updateFilteredTagList(PREDICATE_SHOW_ALL_TAGS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
