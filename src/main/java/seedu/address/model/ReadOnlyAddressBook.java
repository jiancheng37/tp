package seedu.address.model;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the listing list.
     * This list will not contain any duplicate listing.
     */
    ObservableList<Listing> getListingList();

    /**
     * Returns an unmodifiable view of the tag map.
     * This map will not contain any duplicate tag.
     */
    ObservableMap<String, Tag> getTagMap();

}
