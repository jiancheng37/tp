package seedu.address.model.person.predicates;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s property preferences matches a {@code Listing}.
 */
public class PersonMatchesPropertyPredicate implements Predicate<Person> {
    private final Listing listingToMatch;

    public PersonMatchesPropertyPredicate(Listing listingToMatch) {
        this.listingToMatch = listingToMatch;
    }

    @Override
    public boolean test(Person person) {
        List<PropertyPreference> propertyPreferences = person.getPropertyPreferences();
        if (propertyPreferences.isEmpty()) {
            return false;
        }

        if (listingToMatch.getOwners().contains(person)) {
            return false;
        }

        if (!listingToMatch.getAvailability()) {
            return false;
        }

        Set<Tag> tagsToMatch = listingToMatch.getTags();
        if (tagsToMatch.isEmpty()) {
            return true;
        }

        for (PropertyPreference pref : propertyPreferences) {
            //Checks if any tag matches
            if (!Collections.disjoint(pref.getTags(), tagsToMatch)) {
                return true;
            }

            //Checks if price range matches
            if (pref.getPriceRange().doPriceRangeOverlap(listingToMatch.getPriceRange())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PersonMatchesPropertyPredicate
                && listingToMatch.equals(((PersonMatchesPropertyPredicate) other).listingToMatch));
    }
}
