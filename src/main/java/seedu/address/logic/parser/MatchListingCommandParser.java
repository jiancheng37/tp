package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ARGUMENTS_EMPTY;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MatchListingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MatchListingCommandParser} object.
 */
public class MatchListingCommandParser implements Parser<MatchListingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MatchListingCommand
     * and returns a MatchListingCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MatchListingCommand parse(String args) throws ParseException {
        checkCommandFormat(args);
        Index index = ParserUtil.parseIndex(args);
        return new MatchListingCommand(index);
    }

    private static void checkCommandFormat(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ARGUMENTS_EMPTY,
                    MatchListingCommand.MESSAGE_USAGE));
        }
    }

}
