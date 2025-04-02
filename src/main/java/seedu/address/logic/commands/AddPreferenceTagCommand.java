package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CommandUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Adds {@code Tag} to a {@code PropertyPreference} identified using it's displayed index
 * from {@code Person} identified using it's displayed index in the address book.
 */
public class AddPreferenceTagCommand extends Command {

    public static final String COMMAND_WORD = "addPreferenceTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tags to a preference identified "
            + "by the index number used in the displayed preference list of\n"
            + " a specific person, identified by index number used in the displayed person list.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer) PREFERENCE_INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_NEW_TAG + "NEW_TAG]...\n"
            + "Example: " + COMMAND_WORD + " 2 1 "
            + PREFIX_TAG + "quiet "
            + PREFIX_TAG + "pet-friendly "
            + PREFIX_NEW_TAG + "family-friendly "
            + PREFIX_NEW_TAG + "spacious";

    public static final String MESSAGE_SUCCESS = "Adds tags to preferences: %1$s";
    public static final String MESSAGE_INVALID_TAGS = "At least one of the tags given does not exist.\n%1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At least one of the new tags given already exist.\n%1$s";
    public static final String MESSAGE_DUPLICATE_TAGS_IN_LISTING = "At least one of the "
            + "tags given already exist in the preference.\n%1$s";

    private final Index targetPersonIndex;
    private final Index targetPreferenceIndex;
    private final Set<String> tagSet;
    private final Set<String> newTagSet;

    /**
     * Creates an @{code AddPreferenceTagCommand} to add specified {@code Tag} to {@code Preference}.
     *
     * @param personIndex The index of the person from which the preference is located in.
     * @param preferenceIndex The index of the preference from which tags will be removed.
     * @param tagSet The set of existing tags to be added to the preference.
     * @param newTagSet The set of new tags to be added to the preference.
     */
    public AddPreferenceTagCommand(Index personIndex, Index preferenceIndex, Set<String> tagSet,
                                   Set<String> newTagSet) {
        requireAllNonNull(personIndex, preferenceIndex, tagSet, newTagSet);

        this.targetPersonIndex = personIndex;
        this.targetPreferenceIndex = preferenceIndex;
        this.tagSet = tagSet;
        this.newTagSet = newTagSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Get and validate person
        Person targetPerson = CommandUtil.getValidatedPerson(model, targetPersonIndex, MESSAGE_USAGE);

        // Get and validate preference
        PropertyPreference targetPreference = CommandUtil.getValidatedPreference(model, targetPerson,
                targetPreferenceIndex, MESSAGE_USAGE, true);

        // Validate and process tags
        CommandUtil.validateTags(model, tagSet, newTagSet, MESSAGE_USAGE,
                MESSAGE_INVALID_TAGS, MESSAGE_DUPLICATE_TAGS);
        Set<Tag> tags = processAndGetTags(model, targetPreference);

        // Apply changes
        applyTagsToPreference(model, targetPerson, targetPreference, tags);

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                Messages.format(targetPerson, targetPreference), Messages.format(tags)));
    }

    /**
     * Processes tags and checks for duplicates in the preference.
     * Returns the set of tags to be added.
     */
    private Set<Tag> processAndGetTags(Model model, PropertyPreference preference) throws CommandException {
        model.addTags(newTagSet);
        Set<String> tagNames = new HashSet<>(tagSet);
        Set<Tag> tags = new HashSet<>();
        tagNames.addAll(newTagSet);

        for (String tagName : tagNames) {
            Tag tag = model.getTag(tagName);
            if (preference.getTags().contains(tag)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS_IN_LISTING, MESSAGE_USAGE));
            }
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Applies the validated tags to the preference and updates the model.
     */
    private void applyTagsToPreference(Model model, Person targetPerson,
        PropertyPreference preference, Set<Tag> tags) {
        for (Tag tag : tags) {
            tag.addPropertyPreference(preference);
            model.setTag(tag, tag);
            preference.addTag(tag);
        }
        model.setPerson(targetPerson, targetPerson);
        model.resetAllLists();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddPreferenceTagCommand
                && targetPersonIndex.equals(((AddPreferenceTagCommand) other).targetPersonIndex)
                && targetPreferenceIndex.equals(((AddPreferenceTagCommand) other).targetPreferenceIndex)
                && tagSet.equals(((AddPreferenceTagCommand) other).tagSet))
                && newTagSet.equals(((AddPreferenceTagCommand) other).newTagSet);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", targetPersonIndex)
                .add("preferenceIndex", targetPreferenceIndex)
                .add("tags", tagSet)
                .add("newTags", newTagSet)
                .toString();
    }
}
