package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SearchOwnerListingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code SearchOwnerListingCommandParser} object.
 */
public class SearchOwnerListingCommandParser implements Parser<SearchOwnerListingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SearchOwnerListingCommand
     * and returns a SearchOwnerListingCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SearchOwnerListingCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args);
        return new SearchOwnerListingCommand(index);
    }

}
