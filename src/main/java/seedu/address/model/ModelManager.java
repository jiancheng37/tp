package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final SortedList<Person> sortedFilteredPersons;
    private final FilteredList<Listing> filteredListings;
    private final SortedList<Listing> sortedFilteredListings;
    private final ObservableMap<String, Tag> tagMap;
    private final FilteredList<Tag> filteredTags;
    private final SortedList<Tag> sortedFilteredTags;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);

        sortedFilteredPersons = new SortedList<>(filteredPersons);
        sortedFilteredPersons.setComparator(COMPARATOR_SHOW_ALL_PERSONS);

        filteredListings = new FilteredList<>(this.addressBook.getListingList());
        filteredListings.setPredicate(PREDICATE_SHOW_ALL_LISTINGS);

        sortedFilteredListings = new SortedList<>(this.filteredListings);
        sortedFilteredListings.setComparator(COMPARATOR_SHOW_ALL_LISTINGS);

        tagMap = this.addressBook.getTagMap();

        // Convert ObservableMap values to ObservableList
        ObservableList<Tag> tagObservableList = FXCollections.observableArrayList(tagMap.values());
        // Add a listener to the ObservableMap to update the ObservableList dynamically
        tagMap.addListener((MapChangeListener<String, Tag>) change -> {
            if (change.wasAdded()) {
                tagObservableList.add(change.getValueAdded());
            }
            if (change.wasRemoved()) {
                tagObservableList.remove(change.getValueRemoved());
            }
        });


        // Create a FilteredList from the ObservableList
        filteredTags = new FilteredList<>(tagObservableList);
        filteredTags.setPredicate(PREDICATE_SHOW_ALL_TAGS);
        sortedFilteredTags = new SortedList<>(this.filteredTags);
        sortedFilteredTags.setComparator(COMPARATOR_SHOW_ALL_TAGS);

    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void deleteListing(Listing target) {
        addressBook.removeListing(target);
    }

    @Override
    public void deleteTag(Tag target) {
        addressBook.removeTag(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasListing(Listing listing) {
        requireNonNull(listing);
        return addressBook.hasListing(listing);
    }

    @Override
    public void addListing(Listing listing) {
        requireNonNull(listing);
        addressBook.addListing(listing);
    }

    @Override
    public void setListing(Listing listing, Listing editedListing) {
        requireNonNull(listing);
        addressBook.setListing(listing, editedListing);
    }

    private void resetTagList() {
        updateFilteredTagList(PREDICATE_SHOW_ALL_TAGS);
    }

    private void resetPersonList() {
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        updateSortedFilteredPersonList(COMPARATOR_SHOW_ALL_PERSONS);

        PropertyPreference.setFilterPredicate(PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES);
    }

    private void resetListingList() {
        updateFilteredListingList(PREDICATE_SHOW_ALL_LISTINGS);
        updateSortedFilteredListingList(COMPARATOR_SHOW_ALL_LISTINGS);
    }

    @Override
    public void resetAllLists() {
        setSearch(new ArrayList<>(), null, SearchType.NONE);
        resetPersonList();
        resetListingList();
        resetTagList();
    }

    @Override
    public void setSearch(List<Tag> tags, PriceRange priceRange, SearchType searchType) {
        Tag.setActiveSearchTags(tags);
        Tag.setSearch(searchType);
        PriceRange.setFilteredAgainst(priceRange);
        PriceRange.setSearch(searchType);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Person> getSortedFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Listing> getFilteredListingList() {
        return filteredListings;
    }

    @Override
    public ObservableList<Listing> getSortedFilteredListingList() {
        return sortedFilteredListings;
    }

    public ObservableMap<String, Tag> getTagMap() {
        return tagMap;
    }

    @Override
    public ObservableList<Tag> getFilteredTagList() {
        return filteredTags;
    }

    @Override
    public ObservableList<Tag> getSortedFilteredTagList() {
        return sortedFilteredTags;
    }


    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        if (predicate == null) {
            filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);
            return;
        }

        if (predicate.equals(filteredPersons.getPredicate())) {
            filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);
        }

        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void updateSortedFilteredPersonList(Comparator<Person> comparator) {
        if (comparator == null) {
            sortedFilteredPersons.setComparator(COMPARATOR_SHOW_ALL_PERSONS);
            return;
        }

        if (comparator.equals(sortedFilteredPersons.getComparator())) {
            sortedFilteredPersons.setComparator(COMPARATOR_SHOW_ALL_PERSONS);
        }

        sortedFilteredPersons.setComparator(comparator);
    }

    @Override
    public void updateFilteredListingList(Predicate<Listing> predicate) {
        if (predicate == null) {
            filteredListings.setPredicate(PREDICATE_SHOW_ALL_LISTINGS);
            return;
        }

        if (predicate.equals(filteredListings.getPredicate())) {
            filteredListings.setPredicate(PREDICATE_SHOW_ALL_LISTINGS);
        }

        filteredListings.setPredicate(predicate);
    }

    @Override
    public void updateSortedFilteredListingList(Comparator<Listing> comparator) {
        if (comparator == null) {
            sortedFilteredListings.setComparator(COMPARATOR_SHOW_ALL_LISTINGS);
            return;
        }

        if (comparator.equals(sortedFilteredListings.getComparator())) {
            sortedFilteredListings.setComparator(COMPARATOR_SHOW_ALL_LISTINGS);
        }

        sortedFilteredListings.setComparator(comparator);
    }

    @Override
    public void updateFilteredTagList(Predicate<Tag> predicate) {
        if (predicate == null) {
            filteredTags.setPredicate(PREDICATE_SHOW_ALL_TAGS);
            return;
        }
        if (predicate.equals(filteredTags.getPredicate())) {
            filteredTags.setPredicate(PREDICATE_SHOW_ALL_TAGS);
        }
        filteredTags.setPredicate(predicate);
    }

    @Override
    public boolean hasTag(String tag) {
        requireNonNull(tag);
        return addressBook.hasTag(tag);
    }

    @Override
    public boolean hasTags(Set<String> tags) {
        requireNonNull(tags);
        return addressBook.hasTags(tags);
    }

    @Override
    public boolean hasNewTags(Set<String> tags) {
        requireNonNull(tags);
        return addressBook.hasNewTags(tags);
    }

    @Override
    public void addTags(Set<String> tags) {
        requireNonNull(tags);
        addressBook.addTags(tags);
        updateFilteredTagList(PREDICATE_SHOW_ALL_TAGS);
    }

    @Override
    public Tag getTag(String tagName) {
        requireNonNull(tagName);
        return tagMap.get(tagName.toUpperCase());
    }

    @Override
    public void setTag(Tag target, Tag editedTag) {
        addressBook.setTag(target, editedTag);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredListings.equals(otherModelManager.filteredListings);
    }

}
