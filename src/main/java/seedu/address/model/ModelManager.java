package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
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
import seedu.address.model.search.SearchContext;
import seedu.address.model.search.SearchType;
import seedu.address.model.tag.Tag;


/**
 * Manages the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final SearchContext searchContext = new SearchContext();

    // Filtered and sorted lists
    private final FilteredList<Person> filteredPersons;
    private final SortedList<Person> sortedFilteredPersons;
    private final FilteredList<Listing> filteredListings;
    private final SortedList<Listing> sortedFilteredListings;
    private final FilteredList<Tag> filteredTags;
    private final SortedList<Tag> sortedFilteredTags;

    // Tag map and observable list
    private final ObservableMap<String, Tag> tagMap;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);
        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);

        // Initialize filtered and sorted lists
        this.filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.sortedFilteredPersons = new SortedList<>(filteredPersons);
        this.filteredListings = new FilteredList<>(this.addressBook.getListingList());
        this.sortedFilteredListings = new SortedList<>(this.filteredListings);

        // Initialize tag-related collections
        this.tagMap = this.addressBook.getTagMap();
        ObservableList<Tag> tagObservableList = createObservableTagList();
        this.filteredTags = new FilteredList<>(tagObservableList);
        this.sortedFilteredTags = new SortedList<>(this.filteredTags);

        resetAllFilters();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== Helper Methods ==============================================================

    /**
     * Creates an observable list of tags that stays in sync with the tag map.
     */
    private ObservableList<Tag> createObservableTagList() {
        ObservableList<Tag> tagList = FXCollections.observableArrayList(tagMap.values());
        tagMap.addListener((MapChangeListener<String, Tag>) change -> {
            if (change.wasAdded()) {
                tagList.add(change.getValueAdded());
            }

            if (change.wasRemoved()) {
                tagList.remove(change.getValueRemoved());
            }
        });
        return tagList;
    }

    /**
     * Resets all filters to show all items with default sorting.
     */
    private void resetAllFilters() {
        resetPersonList();
        resetListingList();
        resetTagList();
        searchContext.clear();
    }

    private void resetPersonList() {
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        updateSortedFilteredPersonList(COMPARATOR_SHOW_ALL_PERSONS);
    }

    private void resetListingList() {
        updateFilteredListingList(PREDICATE_SHOW_ALL_LISTINGS);
        updateSortedFilteredListingList(COMPARATOR_SHOW_ALL_LISTINGS);
    }

    private void resetTagList() {
        updateFilteredTagList(PREDICATE_SHOW_ALL_TAGS);
    }

    //=========== UserPrefs Operations ========================================================

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

    //=========== AddressBook Operations ======================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    // Person-related operations
    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
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
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    // Listing-related operations
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

    @Override
    public void deleteListing(Listing target) {
        addressBook.removeListing(target);
    }

    // Tag-related operations
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
        resetTagList();
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
    public void deleteTag(Tag target) {
        addressBook.removeTag(target);
    }

    //=========== Filtered List Accessors =====================================================

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Person> getSortedFilteredPersonList() {
        return sortedFilteredPersons;
    }

    @Override
    public ObservableList<Listing> getFilteredListingList() {
        return filteredListings;
    }

    @Override
    public ObservableList<Listing> getSortedFilteredListingList() {
        return sortedFilteredListings;
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
    public ObservableMap<String, Tag> getTagMap() {
        return tagMap;
    }

    //=========== Search Operations ===========================================================

    @Override
    public void setSearch(Set<Tag> tags, PriceRange priceRange, SearchType searchType,
                          Predicate<PropertyPreference> propertyPreferencePredicate) {
        searchContext.configureSearch(searchType, tags, priceRange, propertyPreferencePredicate);
    }

    @Override
    public SearchContext getSearchContext() {
        return searchContext;
    }

    @Override
    public void resetAllLists() {
        searchContext.clear();
        resetAllFilters();
    }

    //=========== List Update Operations ======================================================

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate.equals(filteredPersons.getPredicate())
                ? PREDICATE_SHOW_ALL_PERSONS : predicate);
    }

    @Override
    public void updateSortedFilteredPersonList(Comparator<Person> comparator) {
        requireNonNull(comparator);
        sortedFilteredPersons.setComparator(comparator.equals(sortedFilteredPersons.getComparator())
                ? COMPARATOR_SHOW_ALL_PERSONS : comparator);
    }

    @Override
    public void updateFilteredListingList(Predicate<Listing> predicate) {
        requireNonNull(predicate);
        filteredListings.setPredicate(predicate.equals(filteredListings.getPredicate())
                ? PREDICATE_SHOW_ALL_LISTINGS : predicate);
    }

    @Override
    public void updateSortedFilteredListingList(Comparator<Listing> comparator) {
        requireNonNull(comparator);
        sortedFilteredListings.setComparator(comparator.equals(sortedFilteredListings.getComparator())
                ? COMPARATOR_SHOW_ALL_LISTINGS : comparator);
    }

    @Override
    public void updateFilteredTagList(Predicate<Tag> predicate) {
        requireNonNull(predicate);
        filteredTags.setPredicate(predicate.equals(filteredTags.getPredicate())
                ? PREDICATE_SHOW_ALL_TAGS : predicate);
    }

    //=========== Utility Methods ============================================================

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModel = (ModelManager) other;
        return addressBook.equals(otherModel.addressBook)
                && userPrefs.equals(otherModel.userPrefs)
                && filteredPersons.equals(otherModel.filteredPersons)
                && filteredListings.equals(otherModel.filteredListings);
    }
}
