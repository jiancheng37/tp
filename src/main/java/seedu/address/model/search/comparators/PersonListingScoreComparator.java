package seedu.address.model.search.comparators;

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
    private static final int INITIAL_SCORE = 0;
    private static final int PRICE_MATCH_SCORE = 1;
    private static final int TAG_MATCH_SCORE = 1;
    private static final int NO_MATCH_SCORE = 0;

    private static Listing listingToScore;

    public PersonListingScoreComparator(Listing listingToScore) {
        this.listingToScore = listingToScore;
    }

    @Override
    public int compare(Person o1, Person o2) {
        HashMap<Person, Integer> personScores = new HashMap<>();

        personScores.put(o1, INITIAL_SCORE);
        personScores.put(o2, INITIAL_SCORE);

        for (Person person : personScores.keySet()) {
            int score = INITIAL_SCORE;

            for (PropertyPreference preference : person.getPropertyPreferences()) {
                int preferenceScore = INITIAL_SCORE;

                if (listingToScore.getPriceRange().doPriceRangeOverlap(preference.getPriceRange())) {
                    preferenceScore += PRICE_MATCH_SCORE;
                }

                for (Tag tag : preference.getTags()) {
                    if (listingToScore.getTags().contains(tag)) {
                        preferenceScore += TAG_MATCH_SCORE;
                    }
                }

                if (preferenceScore == NO_MATCH_SCORE) {
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
