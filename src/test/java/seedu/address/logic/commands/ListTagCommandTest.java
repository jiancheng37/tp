package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

public class ListTagCommandTest {

    @Test
    public void execute_listTags_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        ListTagCommand listTagCommand = new ListTagCommand();

        // Execute command
        CommandResult commandResult = listTagCommand.execute(model);

        // Verify result
        assertEquals(ListTagCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredTagList(), model.getFilteredTagList());

        // Verify that filters are reset
        assertTrue(PriceRange.getFilteredAgainst() == null);
        assertTrue(Tag.getActiveSearchTags().isEmpty());
    }

    @Test
    public void equals() {
        ListTagCommand listTagCommand = new ListTagCommand();

        // same object -> returns true
        assertTrue(listTagCommand.equals(listTagCommand));

        // same type -> returns true
        assertTrue(listTagCommand.equals(new ListTagCommand()));

        // different types -> returns false
        assertFalse(listTagCommand.equals(1));

        // null -> returns false
        assertFalse(listTagCommand.equals(null));
    }

    @Test
    public void toStringMethod() {
        ListTagCommand listTagCommand = new ListTagCommand();
        String expected = ListTagCommand.class.getCanonicalName() + "{}";
        assertEquals(expected, listTagCommand.toString());
    }
}
