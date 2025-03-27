package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.COMPARATOR_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES;

import java.util.ArrayList;

import seedu.address.model.Model;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Lists all persons in the address book to the user.
 */
public class ListPersonCommand extends Command {

    public static final String COMMAND_WORD = "listPerson";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all Persons\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        PriceRange.setFilteredAgainst(null);
        Tag.setActiveSearchTags(new ArrayList<>());

        PropertyPreference.setFilterPredicate(PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateSortedFilteredPersonList(COMPARATOR_SHOW_ALL_PERSONS); // Trigger re-render

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
