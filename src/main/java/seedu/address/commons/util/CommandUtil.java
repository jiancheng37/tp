package seedu.address.commons.util;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
/**
 * Utility class for common command operations.
 */
public class CommandUtil {
    /**
     * Gets and validates the target person from the model using the given index.
     * @param model The model to get the person from
     * @param targetPersonIndex The index of the person to get
     * @param messageUsage The usage message to show if the index is invalid
     * @return The validated person
     * @throws CommandException if the person index is invalid
     */
    public static Person getValidatedPerson(Model model, Index targetPersonIndex, String messageUsage)
            throws CommandException {
        List<Person> lastShownList = model.getSortedFilteredPersonList();
        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, messageUsage));
        }
        return lastShownList.get(targetPersonIndex.getZeroBased());
    }

    /**
     * Gets and validates the target preference from the person using the given index.
     * @param model The model containing the search context
     * @param targetPerson The person to get the preference from
     * @param targetPreferenceIndex The index of the preference to get
     * @param messageUsage The usage message to show if the index is invalid
     * @param shouldFilterBySearchContext Whether to filter preferences by search context
     * @return The validated preference
     * @throws CommandException if the preference index is invalid
     */
    public static PropertyPreference getValidatedPreference(Model model, Person targetPerson,
            Index targetPreferenceIndex, String messageUsage, boolean shouldFilterBySearchContext)
            throws CommandException {
        List<PropertyPreference> targetPreferenceList = shouldFilterBySearchContext
                ? targetPerson.getPropertyPreferences().stream()
                        .filter(preference -> model.getSearchContext().matches(preference))
                        .toList()
                : targetPerson.getPropertyPreferences();

        if (targetPreferenceIndex.getZeroBased() >= targetPreferenceList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX,
                    messageUsage));
        }
        return targetPreferenceList.get(targetPreferenceIndex.getZeroBased());
    }

    /**
     * Validates that all existing tags exist and new tags don't exist.
     * @param model The model to validate tags against
     * @param tagSet The set of existing tags to validate
     * @param newTagSet The set of new tags to validate
     * @param messageUsage The usage message to show if validation fails
     * @param invalidTagsMessage The message to show if existing tags are invalid
     * @param duplicateTagsMessage The message to show if new tags already exist
     * @throws CommandException if validation fails
     */
    public static void validateTags(Model model, Set<String> tagSet, Set<String> newTagSet,
            String messageUsage, String invalidTagsMessage, String duplicateTagsMessage)
            throws CommandException {
        if (!model.hasTags(tagSet)) {
            throw new CommandException(String.format(invalidTagsMessage, messageUsage));
        }
        if (model.hasNewTags(newTagSet)) {
            throw new CommandException(String.format(duplicateTagsMessage, messageUsage));
        }
    }
}
