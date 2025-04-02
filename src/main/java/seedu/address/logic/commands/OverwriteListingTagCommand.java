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
        Listing property = getPropertyFromIndex(model);
        validateTags(model);
        updatePropertyTags(model, property);
        return generateCommandResult(property);
    }

    /**
     * Retrieves the property from the model using the stored index.
     */
    private Listing getPropertyFromIndex(Model model) throws CommandException {
        List<Listing> lastShownList = model.getSortedFilteredListingList();
        if (propertyIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX, MESSAGE_USAGE));
        }
        return lastShownList.get(propertyIndex.getZeroBased());
    }

    /**
     * Validates that all existing tags exist and new tags don't exist.
     */
    private void validateTags(Model model) throws CommandException {
        if (!model.hasTags(tagSet)) {
            throw new CommandException(String.format(MESSAGE_INVALID_TAGS, MESSAGE_USAGE));
        }
        if (model.hasNewTags(newTagSet)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS, MESSAGE_USAGE));
        }
    }

    /**
     * Updates the property's tags by removing existing ones and adding new ones.
     */
    private void updatePropertyTags(Model model, Listing property) {
        // Create new tags
        model.addTags(newTagSet);

        Set<String> tagNames = new HashSet<>(tagSet);
        Set<Tag> newTags = prepareNewTags(model, tagNames);

        removeExistingTags(model, property);
        addNewTags(model, property, newTags);

        model.setListing(property, property);
        model.resetAllLists();
    }

    /**
     * Prepares the set of new tags to be added to the property.
     */
    private Set<Tag> prepareNewTags(Model model, Set<String> tagNames) {
        tagNames.addAll(newTagSet);
        Set<Tag> newTags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = model.getTag(tagName);
            newTags.add(tag);
        }
        return newTags;
    }

    /**
     * Removes all existing tags from the property.
     */
    private void removeExistingTags(Model model, Listing property) {
        Set<Tag> existingTags = new HashSet<>(property.getTags());
        for (Tag tag : existingTags) {
            tag.removeListing(property);
            model.setTag(tag, tag);
            property.removeTag(tag);
        }
    }

    /**
     * Adds new tags to the property.
     */
    private void addNewTags(Model model, Listing property, Set<Tag> newTags) {
        for (Tag tag : newTags) {
            tag.addListing(property);
            model.setTag(tag, tag);
            property.addTag(tag);
        }
    }

    /**
     * Generates the command result with formatted property details.
     */
    private CommandResult generateCommandResult(Listing property) {
        String propertyDetails = Messages.formatPropertyDetails(property);
        Set<Tag> newTags = property.getTags();
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
