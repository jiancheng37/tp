package seedu.address.model.search.predicates;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s property preferences contain all the specified tags.
 */
public class PropertyPreferencesContainAllActiveSearchTagsPredicate implements Predicate<PropertyPreference> {

    private final Set<Tag> tagsToMatch;

    public PropertyPreferencesContainAllActiveSearchTagsPredicate(Set<Tag> tagsToMatch) {
        this.tagsToMatch = tagsToMatch;
    }

    @Override
    public boolean test(PropertyPreference preference) {

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
