package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SearchPersonByName;
import seedu.address.logic.commands.exceptions.CommandException;

public class SearchPersonByNameParserTest {

    private SearchPersonByNameParser parser = new SearchPersonByNameParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        String expectedMessage = String.format(Messages.MESSAGE_MISSING_KEYWORD, SearchPersonByName.MESSAGE_USAGE);
        assertParseFailure(parser, "     ", expectedMessage);
    }

    @Test
    public void parse_validArgs_returnsFindCommand() throws CommandException {
        // no leading and trailing whitespaces
        List<String> expectedKeywords = Arrays.asList("Alice", "Bob");
        SearchPersonByName expectedSearchPersonByName = new SearchPersonByName(expectedKeywords);
        assertParseSuccess(parser, "Alice Bob", expectedSearchPersonByName);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedSearchPersonByName);
    }
}
