package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.PriceRange;
import seedu.address.model.search.SearchContext;
import seedu.address.model.search.SearchType;
import seedu.address.model.tag.Tag;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Comparator<Person> COMPARATOR_SHOW_ALL_PERSONS = Comparator.comparing(p -> p.getName().fullName);

    Predicate<Listing> PREDICATE_SHOW_ALL_LISTINGS = unused -> true;
    Comparator<Listing> COMPARATOR_SHOW_ALL_LISTINGS = Comparator.comparing(l -> l.getPostalCode().postalCode);

    Predicate<Tag> PREDICATE_SHOW_ALL_TAGS = unused -> true;
    Comparator<Tag> COMPARATOR_SHOW_ALL_TAGS = Comparator.comparing(t -> t.tagName);

    Predicate<PropertyPreference> PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES = unused -> true;


    /**
     * Returns the current search context.
     */
    SearchContext getSearchContext();

    /**
     *  Sets the search parameters
     *
     *  @param tags the tags to search for
     *  @param priceRange the price range to search for
     *  @param searchType the type of search to perform
     */
    void setSearch(Set<Tag> tags, PriceRange priceRange, SearchType searchType,
                   Predicate<PropertyPreference> propertyPreferencePredicate);

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /**
     * Returns the AddressBook
     */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Returns an unmodifiable view of the filtered person list
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns an unmodifiable view of the sorted filtered person list
     */
    ObservableList<Person> getSortedFilteredPersonList();

    /**
     * Returns an unmodifiable view of the filtered listing list
     */
    ObservableList<Listing> getFilteredListingList();

    /**
     * Returns an unmodifiable view of the sorted filtered listing list
     */
    ObservableList<Listing> getSortedFilteredListingList();

    /**
     * Returns an unmodifiable view of the tag map
     */
    ObservableMap<String, Tag> getTagMap();

    /**
     * Returns an unmodifiable view of the filtered tag list
     */
    ObservableList<Tag> getFilteredTagList();

    /**
     * Returns an unmodifiable view of the sorted filtered tag list
     */
    ObservableList<Tag> getSortedFilteredTagList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Updates the comparator of the sorted filtered person list to sort by the given {@code comparator}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateSortedFilteredPersonList(Comparator<Person> comparator);

    /**
     * Updates the filter of the filtered listing list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredListingList(Predicate<Listing> predicate);

    /**
     * Updates the comparator of the sorted filtered listing list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code comparator} is null.
     */
    void updateSortedFilteredListingList(Comparator<Listing> comparator);

    /**
     * Updates the filter of the filtered tag list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTagList(Predicate<Tag> predicate);

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasListing(Listing listing);

    /**
     * Replaces the given listing {@code target} with {@code editedListing}.
     * {@code target} must exist in the address book.
     * The listing identity of {@code editedListing} must not be the same as another existing listing in the
     * address book.
     */
    void setListing(Listing target, Listing editedListing);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addListing(Listing listing);

    /**
     * Deletes the given listing.
     * The listing must exist in the address book.
     */
    void deleteListing(Listing target);

    boolean hasTag(String tag);

    boolean hasTags(Set<String> tags);

    boolean hasNewTags(Set<String> tags);

    void addTags(Set<String> tags);

    /**
     * Deletes the given tag.
     * The tag must exist in the address book.
     */
    void deleteTag(Tag tagToDelete);

    /**
     * Returns the tag with the given tag name.
     *
     * @param tagName
     * @return The tag with the given tag name.
     */
    Tag getTag(String tagName);

    /**
     * Sets the given tag {@code target} with {@code editedTag}.
     * @param target The tag to be replaced.
     * @param editedTag The tag to replace with.
     */
    void setTag(Tag target, Tag editedTag);

    /**
     * Resets all lists to show all items.
     */
    void resetAllLists();
}
