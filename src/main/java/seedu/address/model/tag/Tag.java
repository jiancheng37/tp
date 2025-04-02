package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;

/**
 * Represents a Tag in the address book.
 * Guarantees: tag name is immutable; tag is valid as declared in {@link #isValidTagName(String)};
 * all details are present and not null. Associations are mutable.
 */
public class Tag {
    public static final String MESSAGE_CONSTRAINTS = "Tags must be between 2 and 30 characters long and can only "
            + "contain letters, numbers, apostrophes, spaces, periods, hyphens, underscores, plus, and ampersands. "
            + "The tag cannot be blank and must not already exist (unless for deleting).";
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9' ._+&-]{2,30}$";

    // Identity fields
    public final String tagName;

    // Associations
    private final List<PropertyPreference> propertyPreferences = new ArrayList<>();
    private final List<Listing> listings = new ArrayList<>();


    /**
     * Constructs a {@code Tag}.
     * The constructor is protected to ensure that tags cannot be created without using the registry.
     *
     * @param tagName             A valid tag name.
     * @param propertyPreferences A valid list of property preferences.
     * @param listings            A valid list of listings.
     */
    public Tag(String tagName, List<PropertyPreference> propertyPreferences, List<Listing> listings) {
        requireAllNonNull(tagName, propertyPreferences, listings);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);

        this.tagName = tagName.toUpperCase();
        this.propertyPreferences.addAll(propertyPreferences);
        this.listings.addAll(listings);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getTagName() {
        return tagName;
    }

    /**
     * Returns an immutable property preferences list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<PropertyPreference> getPropertyPreferences() {
        return Collections.unmodifiableList(propertyPreferences);
    }

    /**
     * Adds a property preference to the tracking list of property preferences that use the tag.
     *
     * @param toAdd A valid property preference.
     */
    public void addPropertyPreference(PropertyPreference toAdd) {
        requireNonNull(toAdd);

        this.propertyPreferences.add(toAdd);
    }

    /**
     * Removes a property preference from the tracking list of property preferences that use the tag.
     *
     * @param toDelete A valid property preference.
     */
    public void removePropertyPreference(PropertyPreference toDelete) {
        requireNonNull(toDelete);

        this.propertyPreferences.remove(toDelete);
    }

    /**
     * Returns an immutable listings list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Listing> getListings() {
        return Collections.unmodifiableList(listings);
    }

    /**
     * Adds a listing to the tracking list of listings that use the tag.
     *
     * @param toAdd A valid listing.
     */
    public void addListing(Listing toAdd) {
        requireNonNull(toAdd);

        this.listings.add(toAdd);
    }

    /**
     * Removes a listing from the tracking list of listings that use the tag.
     *
     * @param toDelete A valid listing.
     */
    public void removeListing(Listing toDelete) {
        requireNonNull(toDelete);

        this.listings.remove(toDelete);
    }

    /**
     * Returns the number of property preferences associated with the tag.
     */
    public int getNumPropertyPreferences() {
        return propertyPreferences.size();
    }

    /**
     * Returns the number of listings associated with the tag.
     */
    public int getNumListings() {
        return listings.size();
    }

    /**
     * Returns the number of times the tag is used in the real estate system.
     */
    public int getNumUsage() {
        return getNumPropertyPreferences() + getNumListings();
    }


    /**
     * * Checks if two tag have the same identity and data fields and associations.
     * This defines a stronger notion of equality between two tags.
     *
     * @param other Object to be compared with.
     * @return true if both tags have the same identity and data fields and associations. false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tagName);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .add("tag name", tagName)
                .toString();
    }

}
