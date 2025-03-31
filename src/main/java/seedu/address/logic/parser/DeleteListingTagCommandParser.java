package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ARGUMENTS_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_ONE_INDEX_EXPECTED;
import static seedu.address.logic.Messages.MESSAGE_PROPERTY_TAG_REQUIRED_FOR_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteListingTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code DeleteListingTagCommandParser} object.
 */
public class DeleteListingTagCommandParser implements Parser<DeleteListingTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteListingTagCommand
     * and returns a DeleteListingTagCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteListingTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        String preamble = argMultimap.getPreamble();
        checkCommandFormat(argMultimap, args);
        Index index = ParserUtil.parseIndex(preamble);
        Set<String> tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new DeleteListingTagCommand(index, tags);
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap, String args) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        boolean hasTags = !(argMultimap.getAllValues(PREFIX_TAG).isEmpty());

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ARGUMENTS_EMPTY,
                    DeleteListingTagCommand.MESSAGE_USAGE));
        }

        if (!hasTags) {
            throw new ParseException(String.format(MESSAGE_PROPERTY_TAG_REQUIRED_FOR_DELETE,
                    DeleteListingTagCommand.MESSAGE_USAGE));
        }

        if (preamble.isEmpty() || preamble.split("\\s+").length != 1) {
            throw new ParseException(String.format(MESSAGE_ONE_INDEX_EXPECTED,
                    DeleteListingTagCommand.MESSAGE_USAGE));

        }
    }
}
