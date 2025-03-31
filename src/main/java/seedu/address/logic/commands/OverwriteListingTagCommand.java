package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.tag.Tag;

/**
 * Overwrites all {@code Tag}s in a {@code Listing} identified using it's displayed index in the address book.
 */
public class OverwriteListingTagCommand extends Command {

    public static final String COMMAND_WORD = "overwriteListingTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Replaces all tags in an existing listing "
            + "identified by the index number used in the displayed listing list.\n"
            + "Parameters: LISTING_INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_NEW_TAG + "NEW_TAG]...\n"
            + "Example: " + COMMAND_WORD + " 3 " + PREFIX_TAG + "2-bedrooms\n"
            + "Example: " + COMMAND_WORD + " 3 " + PREFIX_NEW_TAG + "2-toilets\n"
            + "Example: " + COMMAND_WORD + " 3 " + PREFIX_NEW_TAG + "pet-friendly "
            + PREFIX_NEW_TAG + "integrated cooling\n"
            + "Example: " + COMMAND_WORD + " 3 " + PREFIX_TAG + "4-bedrooms " + PREFIX_TAG
            + "2-toilets " + PREFIX_NEW_TAG + "seaside view " + PREFIX_NEW_TAG + "beach";

    public static final String MESSAGE_SUCCESS = "Tag in property%s overwritten with: %s";
    public static final String MESSAGE_INVALID_TAGS = "At least one of the tags given does not exist.\n%s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At least one of the new tags given already exist.\n%s";

    private final Index propertyIndex;
    private final Set<String> tagSet;
    private final Set<String> newTagSet;

    /**
     * Creates an @{code OverwriteListingTagCommand} to replace all {@code Tag}s in {@code Listing}.
     *
     * @param propertyIndex The index of the property in which tags will be overwritten.
     * @param tagSet The set of existing tags to be used in the property.
     * @param newTagSet The set of new tags to be created and used in the property.
     */
    public OverwriteListingTagCommand(Index propertyIndex, Set<String> tagSet, Set<String> newTagSet) {
        requireAllNonNull(propertyIndex, tagSet, newTagSet);

        this.propertyIndex = propertyIndex;
        this.tagSet = tagSet;
        this.newTagSet = newTagSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Listing> lastShownList = model.getSortedFilteredListingList();

        if (propertyIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Listing property = lastShownList.get(propertyIndex.getZeroBased());

        if (!model.hasTags(tagSet)) {
            throw new CommandException(String.format(MESSAGE_INVALID_TAGS, MESSAGE_USAGE));
        }

        if (model.hasNewTags(newTagSet)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS, MESSAGE_USAGE));
        }

        // Create new tags
        model.addTags(newTagSet);

        Set<String> tagNames = new HashSet<>(tagSet);
        Set<Tag> newTags = new HashSet<>();
        tagNames.addAll(newTagSet);

        // Prepare new tags to be added
        for (String tagName : tagNames) {
            Tag tag = model.getTag(tagName);
            newTags.add(tag);
        }

        // Remove all existing tags
        Set<Tag> existingTags = new HashSet<>(property.getTags());
        for (Tag tag : existingTags) {
            tag.removeListing(property);
            model.setTag(tag, tag);
            property.removeTag(tag);
        }

        // Add new tags
        for (Tag tag : newTags) {
            tag.addListing(property);
            model.setTag(tag, tag);
            property.addTag(tag);
        }

        model.setListing(property, property);

        // Format property details for success message
        String propertyDetails = Messages.formatPropertyDetails(property);

        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_SUCCESS, propertyDetails, Messages.formatTagsOnly(newTags)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof OverwriteListingTagCommand
                && propertyIndex.equals(((OverwriteListingTagCommand) other).propertyIndex)
                && tagSet.equals(((OverwriteListingTagCommand) other).tagSet))
                && newTagSet.equals(((OverwriteListingTagCommand) other).newTagSet);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("propertyIndex", propertyIndex)
                .add("tags", tagSet)
                .add("newTags", newTagSet)
                .toString();
    }
}
