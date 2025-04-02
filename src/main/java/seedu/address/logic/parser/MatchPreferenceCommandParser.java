package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ARGUMENTS_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_EXPECTED_TWO_INDICES;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MatchPreferenceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MatchPreferenceCommandParser} object.
 */
public class MatchPreferenceCommandParser implements Parser<MatchPreferenceCommand> {

    private static final String WHITESPACE_REGEX = "\\s+";
    private static final int EXPECTED_PREAMBLE_PARTS = 2;
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;

    /**
     * Parses the given {@code String} of arguments in the context of the MatchPreferenceCommandParser
     * and returns a MatchPreferenceCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MatchPreferenceCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        checkCommandFormat(argMultimap, args);

        List<Index> multipleIndices = ParserUtil.parseMultipleIndices(args);
        return new MatchPreferenceCommand(multipleIndices.get(FIRST_INDEX), multipleIndices.get(SECOND_INDEX));
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap, String args) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ARGUMENTS_EMPTY,
                    MatchPreferenceCommand.MESSAGE_USAGE));
        }

        if (preamble.isEmpty() || preamble.split(WHITESPACE_REGEX).length != EXPECTED_PREAMBLE_PARTS) {
            throw new ParseException(String.format(MESSAGE_EXPECTED_TWO_INDICES,
                    MatchPreferenceCommand.MESSAGE_USAGE));
        }
    }

}
