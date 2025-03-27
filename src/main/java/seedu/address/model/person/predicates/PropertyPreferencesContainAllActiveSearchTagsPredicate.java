package seedu.address.model.person.predicates;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s property preferences contain all the specified tags.
 */
public class PropertyPreferencesContainAllActiveSearchTagsPredicate implements Predicate<PropertyPreference> {

    @Override
    public boolean test(PropertyPreference preference) {
        Set<Tag> tagsToMatch = new HashSet<>(Tag.getActiveSearchTags().values());

        if (tagsToMatch.isEmpty()) {
            return true;
        }

        Set<Tag> preferenceTags = preference.getTags();
        return preferenceTags.containsAll(tagsToMatch);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof PropertyPreferencesContainAllActiveSearchTagsPredicate);
    }
}
