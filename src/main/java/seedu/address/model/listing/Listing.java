package seedu.address.model.listing;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Represents a Listing in the real estate system.
 * Guarantees: either unit number or house number is present and not null but not both,
 * rest of details are present and not null, field values are validated, immutable. Associations are mutable.
 */
public class Listing {

    // Identity fields
    private final PostalCode postalCode;
    private final UnitNumber unitNumber;
    private final HouseNumber houseNumber;

    // Data fields
    private final PriceRange priceRange;
    private final PropertyName propertyName;

    // Associations
    private final Set<Tag> tags = new HashSet<>();
    private final List<Person> owners = new ArrayList<>();

    // Status field
    private boolean isAvailable;

    /**
     * Constructs an {@code Listing}.
     * Every field except for unit number must be present and not null.
     * Unit number must be null.
     *
     * @param postalCode A valid postal code.
     * @param unitNumber A valid unit number.
     * @param priceRange A valid price range.
     * @param tags A valid set of tags.
     * @param owners A valid list of owners.
     */
    public Listing(PostalCode postalCode, UnitNumber unitNumber, PriceRange priceRange,
                   Set<Tag> tags, List<Person> owners, boolean isAvailable) {
        requireAllNonNull(postalCode, unitNumber, priceRange, tags, owners, isAvailable);
        this.postalCode = postalCode;
        this.unitNumber = unitNumber;
        this.houseNumber = null;
        this.priceRange = priceRange;
        this.propertyName = null;
        this.tags.addAll(tags);
        this.owners.addAll(owners);
        this.isAvailable = isAvailable;
    }

    /**
     * Constructs an {@code Listing}.
     * Every field except for house number must be present and not null.
     * House number must be null.
     *
     * @param postalCode A valid postal code.
     * @param houseNumber A valid house number.
     * @param priceRange A valid price range.
     * @param tags A valid set of tags.
     * @param owners A valid list of owners.
     */
    public Listing(PostalCode postalCode, HouseNumber houseNumber, PriceRange priceRange,
                   Set<Tag> tags, List<Person> owners, boolean isAvailable) {
        requireAllNonNull(postalCode, houseNumber, priceRange, tags, owners, isAvailable);
        this.postalCode = postalCode;
        this.unitNumber = null;
        this.houseNumber = houseNumber;
        this.priceRange = priceRange;
        this.propertyName = null;
        this.tags.addAll(tags);
        this.owners.addAll(owners);
        this.isAvailable = isAvailable;
    }

    /**
     * Constructs an {@code Listing}.
     * Every field except for house number must be present and not null.
     * House number must be null.
     *
     * @param postalCode A valid postal code.
     * @param unitNumber A valid unit number.
     * @param priceRange A valid price range.
     * @param propertyName A valid property name.
     * @param tags A valid set of tags.
     * @param owners A valid list of owners.
     */
    public Listing(PostalCode postalCode, UnitNumber unitNumber, PriceRange priceRange,
                   PropertyName propertyName, Set<Tag> tags, List<Person> owners, boolean isAvailable) {
        requireAllNonNull(postalCode, unitNumber, priceRange, propertyName, tags, owners, isAvailable);
        this.postalCode = postalCode;
        this.unitNumber = unitNumber;
        this.houseNumber = null;
        this.priceRange = priceRange;
        this.propertyName = propertyName;
        this.tags.addAll(tags);
        this.owners.addAll(owners);
        this.isAvailable = isAvailable;
    }

    /**
     * Constructs an {@code Listing}.
     * Every field except for unit number must be present and not null.
     * Unit number must be null.
     *
     * @param postalCode A valid postal code.
     * @param houseNumber A valid house number.
     * @param priceRange A valid price range.
     * @param propertyName A valid property name.
     * @param tags A valid set of tags.
     * @param owners A valid list of owners.
     */
    public Listing(PostalCode postalCode, HouseNumber houseNumber, PriceRange priceRange,
                   PropertyName propertyName, Set<Tag> tags, List<Person> owners, boolean isAvailable) {
        requireAllNonNull(postalCode, houseNumber, priceRange, propertyName, tags, owners, isAvailable);

        this.postalCode = postalCode;
        this.unitNumber = null;
        this.houseNumber = houseNumber;
        this.priceRange = priceRange;
        this.propertyName = propertyName;
        this.tags.addAll(tags);
        this.owners.addAll(owners);
        this.isAvailable = isAvailable;
    }

    /**
     * Selects the appropriate constructor given the parameters.
     * Every field except for property name and either house number or unit number must be present and not null.
     *
     * @param postalCode A valid postal code.
     * @param unitNumber A valid unit number.
     * @param houseNumber A valid house number.
     * @param priceRange A valid price range.
     * @param propertyName A valid property name.
     * @param tags A valid set of tags.
     * @param owners A valid list of owners.
     * @return A Listing object.
     */
    public static Listing of(PostalCode postalCode, UnitNumber unitNumber, HouseNumber houseNumber,
                             PriceRange priceRange, PropertyName propertyName, Set<Tag> tags, List<Person> owners,
                             boolean isAvailable) {

        requireAllNonNull(postalCode, priceRange, tags, owners);

        if (unitNumber == null && propertyName == null) {
            return new Listing(postalCode, houseNumber, priceRange, tags, owners, isAvailable);
        } else if (houseNumber == null && propertyName == null) {
            return new Listing(postalCode, unitNumber, priceRange, tags, owners, isAvailable);
        } else if (unitNumber == null) {
            return new Listing(postalCode, houseNumber, priceRange, propertyName, tags, owners, isAvailable);
        } else if (houseNumber == null) {
            return new Listing(postalCode, unitNumber, priceRange, propertyName, tags, owners, isAvailable);
        }

        return null;
    }


    public PostalCode getPostalCode() {
        return postalCode;
    }

    public UnitNumber getUnitNumber() {
        return unitNumber;
    }

    public HouseNumber getHouseNumber() {
        return houseNumber;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public PropertyName getPropertyName() {
        return propertyName;
    }

    public boolean getAvailability() {
        return isAvailable;
    }

    public void markUnavailable() {
        this.isAvailable = false;
    }

    public void markAvailable() {
        this.isAvailable = true;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Adds a tag to the listing.
     *
     * @param toAdd A valid tag.
     */
    public void addTag(Tag toAdd) {
        requireNonNull(toAdd);
        this.tags.add(toAdd);
    }

    /**
     * Removes a tag from the listing.
     *
     * @param toDelete A valid tag.
     */
    public void removeTag(Tag toDelete) {
        requireNonNull(toDelete);
        this.tags.remove(toDelete);
    }

    /**
     * Returns an immutable owners list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Person> getOwners() {
        return Collections.unmodifiableList(owners);
    }

    /**
     * Adds a person as the owner of the listing.
     *
     * @param toAdd A valid person.
     */
    public void addOwner(Person toAdd) {
        requireNonNull(toAdd);
        this.owners.add(toAdd);
    }

    /**
     * Removes a person from the owner of the listing.
     *
     * @param toDelete A valid person.
     */
    public void removeOwner(Person toDelete) {
        requireNonNull(toDelete);
        this.owners.remove(toDelete);
    }

    /**
     * Checks if two listings have the same unique identifiers.
     * This defines a weaker notion of equality between two listings.
     *
     * @param otherListing Listing to be compared with.
     * @return true if both listings have the same postal code, unit number and house number. false otherwise.
     */
    public boolean isSameListing(Listing otherListing) {
        if (otherListing == this) {
            return true;
        }

        return otherListing != null
                && otherListing.postalCode.equals(postalCode)
                && Objects.equals(unitNumber, otherListing.unitNumber)
                && Objects.equals(houseNumber, otherListing.houseNumber);
    }

    /**
     * Checks if two listings have the same identity and data fields and associations.
     * This defines a stronger notion of equality between two listings.
     *
     * @param other Object to be compared with.
     * @return true if both listings have the same identity and data fields and associations. false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Listing)) {
            return false;
        }

        Listing otherListing = (Listing) other;
        return postalCode.equals(otherListing.postalCode)
                && Objects.equals(unitNumber, otherListing.unitNumber)
                && Objects.equals(houseNumber, otherListing.houseNumber)
                && priceRange.equals(otherListing.priceRange)
                && Objects.equals(propertyName, otherListing.propertyName)
                && tags.equals(otherListing.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postalCode, unitNumber, houseNumber, priceRange, propertyName);
    }

    @Override
    public String toString() {

        ToStringBuilder stringBuilder = new ToStringBuilder(this)
                .add("postal code", postalCode);

        // Guarantee ensures that one of them is null, include the non-null field
        if (houseNumber == null) {

            stringBuilder.add("unit number", unitNumber);

        } else if (unitNumber == null) {

            stringBuilder.add("house number", houseNumber);

        }

        stringBuilder.add("price range", priceRange)
                .add("property name", propertyName)
                .add("tags", tags)
                .add("owners", owners);

        return stringBuilder.toString();
    }

}
