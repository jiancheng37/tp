package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ARGUMENTS_EMPTY;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkAvailableCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MarkAvailableCommandParser} object.
 */
public class MarkAvailableCommandParser implements Parser<MarkAvailableCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the MarkAvailableCommand
     * and returns a MarkAvailableCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkAvailableCommand parse(String args) throws ParseException {
        checkCommandFormat(args);
        Index index = ParserUtil.parseIndex(args);
        return new MarkAvailableCommand(index);
    }

    private static void checkCommandFormat(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ARGUMENTS_EMPTY,
                    MarkAvailableCommand.MESSAGE_USAGE));
        }
    }
}
