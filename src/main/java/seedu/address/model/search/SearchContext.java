package seedu.address.model.search;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Centralized management of search state including active filters for tags, price ranges, and property preferences.
 */
public class SearchContext {
    private SearchType searchType = SearchType.NONE;
    private final Set<Tag> activeTags = new HashSet<>();
    private PriceRange activePriceRange;
    private Predicate<PropertyPreference> propertyPreferencePredicate = Model.PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES;

    /* Search Configuration Methods */

    /**
     * Configures the complete search state.
     *
     * @param type The type of search (PERSON, LISTING, or NONE)
     * @param tags The set of tags to filter by
     * @param range The price range to filter by
     * @param predicate The predicate for property preference filtering
     */
    public void configureSearch(SearchType type, Set<Tag> tags, PriceRange range,
                                Predicate<PropertyPreference> predicate) {
        setSearchType(type);
        setActiveSearchTags(tags);
        setActivePriceRange(range);
        setPropertyPreferencePredicate(predicate);
    }

    /**
     * Resets all search filters to their default state.
     */
    public void clear() {
        configureSearch(SearchType.NONE, new HashSet<>(), null,
                Model.PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES);
    }

    /* Filter Evaluation Methods */

    /**
     * Checks if a property preference matches the current active filters.
     */
    public boolean matches(PropertyPreference preference) {
        return propertyPreferencePredicate.test(preference);
    }

    /**
     * Checks if a tag is active for person searches.
     */
    public boolean isTagActiveForPerson(Tag tag) {
        return searchType == SearchType.PERSON && activeTags.contains(tag);
    }

    /**
     * Checks if a tag is active for listing searches.
     */
    public boolean isTagActiveForListing(Tag tag) {
        return searchType == SearchType.LISTING && activeTags.contains(tag);
    }

    /**
     * Checks if a price range matches the active filter range.
     */
    public boolean isPriceInRangeForPerson(PriceRange range) {
        if (activePriceRange == null || searchType != SearchType.PERSON) {
            return false;
        }
        return range.doPriceRangeOverlap(activePriceRange);
    }

    /**
     * Checks if a price range matches the active filter range.
     */
    public boolean isPriceInRangeForListing(PriceRange range) {
        if (activePriceRange == null || searchType != SearchType.LISTING) {
            return false;
        }
        return range.doPriceRangeOverlap(activePriceRange);
    }

    /* Getters and Setters */

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = requireNonNull(searchType);
    }

    public Set<Tag> getActiveTags() {
        return unmodifiableSet(activeTags);
    }

    public void setActiveSearchTags(Set<Tag> tags) {
        requireAllNonNull(tags);
        this.activeTags.clear();
        this.activeTags.addAll(tags);
    }

    public PriceRange getActivePriceRange() {
        return activePriceRange;
    }

    public void setActivePriceRange(PriceRange range) {
        this.activePriceRange = range;
    }

    public Predicate<PropertyPreference> getPropertyPreferencePredicate() {
        return propertyPreferencePredicate;
    }

    public void setPropertyPreferencePredicate(Predicate<PropertyPreference> predicate) {
        this.propertyPreferencePredicate = requireNonNull(predicate);
    }

    /* Helper Methods */

    /**
     * Creates a default search context with no active filters.
     */
    public static SearchContext createDefault() {
        SearchContext context = new SearchContext();
        context.clear();
        return context;
    }
}
