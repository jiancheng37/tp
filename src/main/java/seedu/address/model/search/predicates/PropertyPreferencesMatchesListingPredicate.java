package seedu.address.model.search.predicates;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s property preferences contain all the specified tags.
 */
public class PropertyPreferencesMatchesListingPredicate implements Predicate<PropertyPreference> {

    private final Listing listingToMatch;

    public PropertyPreferencesMatchesListingPredicate(Listing listingToMatch) {
        this.listingToMatch = listingToMatch;
    }

    @Override
    public boolean test(PropertyPreference preference) {
        Set<Tag> tagsToMatch = listingToMatch.getTags();

        if (tagsToMatch.isEmpty()) {
            return true;
        }

        if (listingToMatch.getPriceRange().doPriceRangeOverlap(preference.getPriceRange())) {
            return true;
        }

        Set<Tag> preferenceTags = preference.getTags();
        return !Collections.disjoint(preferenceTags, tagsToMatch);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof PropertyPreferencesMatchesListingPredicate);
    }
}
