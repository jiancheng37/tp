package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private AddressBook addressBook;

    @BeforeEach
    public void setUp() {
        addressBook = new AddressBook();
    }

    @Test
    public void constructor_initializesEmptyAddressBook() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getListingList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        Person duplicatePerson = new PersonBuilder(ALICE).build();
        List<Person> newPersons = Arrays.asList(ALICE, duplicatePerson);
        AddressBookStub newData = new AddressBookStub(newPersons, Collections.emptyList(), new HashMap<>());

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void addPerson_validPerson_success() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.getPersonList().contains(ALICE));
    }

    @Test
    public void removePerson_existingPerson_success() {
        addressBook.addPerson(ALICE);
        addressBook.removePerson(ALICE);
        assertFalse(addressBook.getPersonList().contains(ALICE));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void hasTags_existingTags_returnTrue() {
        Set<String> tags = new HashSet<>(Collections.singletonList("Luxury"));
        addressBook.addTags(tags);
        assertTrue(addressBook.hasTags(tags));
    }

    @Test
    public void hasTags_nonExistingTags_returnFalse() {
        Set<String> tags = new HashSet<>(Collections.singletonList("NonExistent"));
        assertFalse(addressBook.hasTags(tags));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons;
        private final ObservableList<Listing> listings;
        private final ObservableMap<String, Tag> tags;

        AddressBookStub(List<Person> persons, List<Listing> listings, HashMap<String, Tag> tags) {
            this.persons = FXCollections.observableArrayList(persons);
            this.listings = FXCollections.observableArrayList(listings);
            this.tags = FXCollections.observableMap(tags);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Listing> getListingList() {
            return listings;
        }

        @Override
        public ObservableMap<String, Tag> getTagMap() {
            return tags;
        }
    }
}
