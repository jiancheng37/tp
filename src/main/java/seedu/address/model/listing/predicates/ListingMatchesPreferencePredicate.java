package seedu.address.model.listing.predicates;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;


/**
 * Tests if a {@code Listing} matches a {@code PropertyPreference}.
 */
public class ListingMatchesPreferencePredicate implements Predicate<Listing> {
    private final PropertyPreference preferenceToMatch;

    public ListingMatchesPreferencePredicate(PropertyPreference preferenceToMatch) {
        this.preferenceToMatch = preferenceToMatch;
    }

    @Override
    public boolean test(Listing listing) {
        Set<Tag> tagsToMatch = preferenceToMatch.getTags();

        if (!listing.getAvailability()) {
            return false;
        }

        if (listing.getOwners().contains(preferenceToMatch.getPerson())) {
            return false;
        }

        if (tagsToMatch.isEmpty() || listing.getPriceRange().doPriceRangeOverlap(preferenceToMatch.getPriceRange())) {
            return true;
        }

        Set<Tag> listingTags = listing.getTags();
        return listingTags.containsAll(tagsToMatch);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListingMatchesPreferencePredicate
                && preferenceToMatch.equals(((ListingMatchesPreferencePredicate) other).preferenceToMatch));
    }
}
