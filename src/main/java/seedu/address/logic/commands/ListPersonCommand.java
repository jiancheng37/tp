package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.COMPARATOR_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES;

import java.util.HashSet;

import seedu.address.model.Model;
import seedu.address.model.search.SearchContext;
import seedu.address.model.search.SearchType;

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

        SearchContext searchContext = model.getSearchContext();

        // If the current search type is person, reset the search type to none
        // and clear the search tags and price range.
        if (searchContext.getSearchType() == SearchType.PERSON) {
            searchContext.setSearchType(SearchType.NONE);
            searchContext.setActivePriceRange(null);
            searchContext.setActiveSearchTags(new HashSet<>());
        }

        // Reset the property preference predicate to show all property preferences.
        searchContext.setPropertyPreferencePredicate(PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES);

        // Reset the person predicate to show all persons and default sort.
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateSortedFilteredPersonList(COMPARATOR_SHOW_ALL_PERSONS); // Trigger re-render

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
