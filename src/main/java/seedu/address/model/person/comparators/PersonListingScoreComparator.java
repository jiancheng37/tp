package seedu.address.model.person.comparators;

import java.util.Comparator;
import java.util.HashMap;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Compares two persons based on their how well they match a given listing.
 */
public class PersonListingScoreComparator implements Comparator<Person> {
    private static Listing listingToScore;

    public PersonListingScoreComparator(Listing listingToScore) {
        this.listingToScore = listingToScore;
    }

    @Override
    public int compare(Person o1, Person o2) {

        HashMap<Person, Integer> personScores = new HashMap<>();

        personScores.put(o1, 0);
        personScores.put(o2, 0);

        for (Person person : personScores.keySet()) {
            int score = 0;

            for (PropertyPreference preference : person.getPropertyPreferences()) {
                int preferenceScore = 0;

                if (listingToScore.getPriceRange().doPriceRangeOverlap(preference.getPriceRange())) {
                    preferenceScore += 1;
                }

                for (Tag tag : preference.getTags()) {
                    if (listingToScore.getTags().contains(tag)) {
                        preferenceScore += 1;
                    }
                }

                if (preferenceScore == 0) {
                    continue;
                }

                if (preferenceScore > score) {
                    score = preferenceScore;
                }
            }

            personScores.put(person, score);
        }

        return Integer.compare(personScores.get(o2), personScores.get(o1));
    }
}
