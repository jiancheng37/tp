package seedu.address.model.listing.predicates;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;


/**
 * Tests if a {@code Listing} matches a {@code PropertyPreference}.
 * Used for {@code MatchPersonCommand}.
 */
public class ListingMatchesPreferencePredicate implements Predicate<Listing> {
    private final PropertyPreference preferenceToMatch;

    public ListingMatchesPreferencePredicate(PropertyPreference preferenceToMatch) {
        this.preferenceToMatch = preferenceToMatch;
    }

    @Override
    public boolean test(Listing listing) {
        Set<Tag> tagsToMatch = preferenceToMatch.getTags();

        // If unavailable or listing is owned by the person in the preference, reject.
        if (!listing.getAvailability() || listing.getOwners().contains(preferenceToMatch.getPerson())) {
            return false;
        }

        // If price range overlaps, accept.
        boolean PriceRangeOverlaps = preferenceToMatch.getPriceRange().doPriceRangeOverlap(listing.getPriceRange());
        if (PriceRangeOverlaps) {
            return true;
        }

        // If any tags match and price range doesn't overlap, accept. Rejects otherwise.
        Set<Tag> listingTags = listing.getTags();
        return !Collections.disjoint(listingTags, tagsToMatch);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListingMatchesPreferencePredicate
                && preferenceToMatch.equals(((ListingMatchesPreferencePredicate) other).preferenceToMatch));
    }
}
