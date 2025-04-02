package seedu.address.model.search.comparators;

import java.util.Comparator;
import java.util.HashMap;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Compares two listings based on how well they match a given preference.
 */
public class ListingPreferenceScoreComparator implements Comparator<Listing> {
    private static final int INITIAL_SCORE = 0;
    private static final int PRICE_MATCH_SCORE = 1;
    private static final int TAG_MATCH_SCORE = 1;
    private static final int NO_MATCH_SCORE = 0;

    private static PropertyPreference preferenceToScore;

    public ListingPreferenceScoreComparator(PropertyPreference preferenceToScore) {
        this.preferenceToScore = preferenceToScore;
    }

    @Override
    public int compare(Listing o1, Listing o2) {
        HashMap<Listing, Integer> listingScores = new HashMap<>();

        listingScores.put(o1, INITIAL_SCORE);
        listingScores.put(o2, INITIAL_SCORE);

        for (Listing listing : listingScores.keySet()) {
            int score = INITIAL_SCORE;

            if (preferenceToScore.getPriceRange().doPriceRangeOverlap(listing.getPriceRange())) {
                score += PRICE_MATCH_SCORE;
            }

            for (Tag tag : preferenceToScore.getTags()) {
                if (listing.getTags().contains(tag)) {
                    score += TAG_MATCH_SCORE;
                }
            }

            if (score == NO_MATCH_SCORE) {
                continue;
            }

            listingScores.put(listing, score);
        }

        return Integer.compare(listingScores.get(o2), listingScores.get(o1));
    }
}
