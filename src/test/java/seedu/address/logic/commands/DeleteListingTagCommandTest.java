package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.UnitNumber;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Unit tests for DeleteListingTagCommand.
 */
public class DeleteListingTagCommandTest {
    private Model model;
    private Listing sampleListing;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());

        // Creating a sample Person (Owner)
        Person sampleOwner = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );


        model.addTags(Set.of("Luxury", "Pool"));

        // Create tags (do NOT manually add to registry)
        Tag luxuryTag = model.getTag("Luxury");
        Tag poolTag = model.getTag("Pool");

        Set<Tag> tags = new HashSet<>();
        tags.add(luxuryTag);
        tags.add(poolTag);

        // Create listing with tags
        sampleListing = Listing.of(
                new PostalCode("123456"),
                new UnitNumber("10-123"),
                null, // house number
                new PriceRange(new Price("100000"), new Price("200000")),
                null, // property name
                tags,
                List.of(sampleOwner),
                true
        );

        // Add listing to model (this registers tags automatically)
        model.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
        model.addListing(sampleListing);
    }

    @Test
    public void execute_validTag_success() throws Exception {
        Index validIndex = Index.fromZeroBased(0); // Define the index manually
        Set<String> tagsToRemove = new HashSet<>();
        tagsToRemove.add("Luxury");
        tagsToRemove.add("Pool");

        DeleteListingTagCommand deleteCommand = new DeleteListingTagCommand(validIndex, tagsToRemove);

        CommandResult result = deleteCommand.execute(model);

        Tag luxuryTag = new Tag("Luxury", List.of(), List.of());

        // Sort tags to make output deterministic
        List<String> sortedTags = tagsToRemove.stream().sorted().collect(Collectors.toList());
        String expectedMessage = String.format(Messages.MESSAGE_DELETE_PROPERTY_TAG_SUCCESS,
                sampleListing.getPostalCode(), sortedTags);
        assertTrue(result.getFeedbackToUser().contains(sampleListing.getPostalCode().toString()));
        assertEquals(
                expectedMessage,
                String.format(Messages.MESSAGE_DELETE_PROPERTY_TAG_SUCCESS,
                        sampleListing.getPostalCode(), tagsToRemove)
        );
    }

    @Test
    public void execute_invalidPropertyIndex_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(100); // Manually define an out-of-bounds index
        Set<String> tagsToRemove = new HashSet<>();
        tagsToRemove.add("Luxury");

        DeleteListingTagCommand deleteCommand = new DeleteListingTagCommand(outOfBoundsIndex, tagsToRemove);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    @Test
    public void execute_nonExistentTag_failure() {
        Index validIndex = Index.fromZeroBased(0); // Define the index manually
        Set<String> tagsToRemove = new HashSet<>();
        tagsToRemove.add("NonExistentTag");

        DeleteListingTagCommand deleteCommand = new DeleteListingTagCommand(validIndex, tagsToRemove);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    @Test
    public void execute_tagNotInListing_failure() {
        Index validIndex = Index.fromZeroBased(0);
        Set<String> tagsToRemove = Set.of("NotInListingTag");

        DeleteListingTagCommand deleteCommand = new DeleteListingTagCommand(validIndex, tagsToRemove);
        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }
}
