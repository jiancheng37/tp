package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.listing.Listing;

/**
 * Represents a Person in the real estate system.
 * Guarantees: details are present and not null, field values are validated, immutable. Associations are mutable.
 */
public class Person {

    // Identity fields
    private final Phone phone;

    // Data fields
    private final Name name;
    private final Email email;

    // Associations
    private final List<PropertyPreference> propertyPreferences = new ArrayList<>();
    private final List<Listing> listings = new ArrayList<>();

    /**
     * Constructs an {@code Person}.
     * Every field  must be present and not null.
     *
     * @param name A valid name.
     * @param phone A valid phone number.
     * @param email A valid email.
     * @param propertyPreferences A valid list of property preferences.
     * @param listings A valid list of listings.
     */
    public Person(Name name, Phone phone, Email email, List<PropertyPreference> propertyPreferences,
                  List<Listing> listings) {
        requireAllNonNull(name, phone, email, propertyPreferences, listings);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.propertyPreferences.addAll(propertyPreferences);
        this.listings.addAll(listings);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable property preferences list which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<PropertyPreference> getPropertyPreferences() {
        return Collections.unmodifiableList(propertyPreferences);
    }

    /**
     * Adds a property preference of the person.
     *
     * @param toAdd A valid property preference
     */
    public void addPropertyPreference(PropertyPreference toAdd) {
        requireNonNull(toAdd);

        this.propertyPreferences.add(toAdd);
    }

    /**
     * Removes a property preference of the person.
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
     * Adds a listing as being owned by the person.
     *
     * @param toAdd A valid listing.
     */
    public void addListing(Listing toAdd) {
        requireNonNull(toAdd);

        this.listings.add(toAdd);
    }

    /**
     * Removes a listing as being owned by the person.
     *
     * @param toDelete A valid listing.
     */
    public void removeListing(Listing toDelete) {
        requireNonNull(toDelete);

        this.listings.remove(toDelete);
    }

    /**
     * Checks if two persons have the same unique identifiers.
     * This defines a weaker notion of equality between two persons.
     *
     * @param otherPerson Person to be compared with.
     * @return true if both persons have the same phone number. false otherwise.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.phone.equals(this.phone);
    }

    /**
     * Checks if two persons have the same identity and data fields and associations.
     * This defines a stronger notion of equality between two persons.
     *
     * @param other Object to be compared with.
     * @return true if both persons have the same identity and data fields and associations. false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("property preferences", propertyPreferences)
                .toString();
    }

}
