package seedu.address.model.search.predicates;

import java.util.function.Predicate;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;


/**
 * Tests if a {@code Listing} contains the specified owner.
 */
public class ListingContainsOwnerPredicate implements Predicate<Listing> {
    private final Person personToMatch;

    public ListingContainsOwnerPredicate(Person personToMatch) {
        this.personToMatch = personToMatch;
    }

    @Override
    public boolean test(Listing listing) {
        return listing.getOwners().contains(personToMatch);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListingContainsOwnerPredicate
                && personToMatch.equals(((ListingContainsOwnerPredicate) other).personToMatch));
    }
}
