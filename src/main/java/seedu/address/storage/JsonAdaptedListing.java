package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.listing.HouseNumber;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.PropertyName;
import seedu.address.model.listing.UnitNumber;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;


/**
 * Jackson-friendly version of {@link Listing}.
 */
class JsonAdaptedListing {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Listing's %s field is missing!";

    private final String postalCode;
    private final String unitNumber;
    private final String houseNumber;
    private final JsonAdaptedPriceRange priceRange;
    private final String propertyName;
    private final List<JsonAdaptedTag> tags;
    private final List<String> ownerKeys;
    private final boolean isAvailable;


    /**
     * Constructs a {@code JsonAdaptedListing} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedListing(@JsonProperty("postalCode") String postalCode,
                              @JsonProperty("unitNumber") String unitNumber,
                              @JsonProperty("houseNumber") String houseNumber,
                              @JsonProperty("priceRange") JsonAdaptedPriceRange priceRange,
                              @JsonProperty("propertyName") String propertyName,
                              @JsonProperty("tags") List<JsonAdaptedTag> tags,
                              @JsonProperty("ownerKeys") List<String> ownerKeys,
                              @JsonProperty("isAvailable") boolean isAvailable) {

        this.postalCode = postalCode;
        this.unitNumber = unitNumber;
        this.houseNumber = houseNumber;

        this.priceRange = priceRange;

        this.propertyName = propertyName;
        this.tags = tags;
        this.ownerKeys = ownerKeys;
        this.isAvailable = isAvailable;
    }


    /**
     * Converts a given {@code Listing} into this class for Jackson use.
     */
    public JsonAdaptedListing(Listing source) {
        postalCode = source.getPostalCode().postalCode;
        unitNumber = source.getUnitNumber() != null ? source.getUnitNumber().unitNumber : null;
        houseNumber = source.getHouseNumber() != null ? source.getHouseNumber().houseNumber : null;

        this.priceRange = new JsonAdaptedPriceRange(source.getPriceRange());

        propertyName = source.getPropertyName() != null ? source.getPropertyName().propertyName : null;
        tags = source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList());

        ownerKeys = source.getOwners().stream()
                .map(owner -> owner.getPhone().value)
                .collect(Collectors.toList());

        isAvailable = source.getAvailability();

    }


    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Listing} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Listing toModelType(AddressBook addressBook) throws IllegalValueException {
        if (postalCode == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PostalCode.class.getSimpleName()));
        }
        if (!PostalCode.isValidPostalCode(postalCode)) {
            throw new IllegalValueException(PostalCode.MESSAGE_CONSTRAINTS);
        }
        final PostalCode modelPostalCode = new PostalCode(postalCode);


        if (unitNumber == null && houseNumber == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    "Postal Code or Unit Number"));
        }

        HouseNumber modelHouseNumber = null;
        UnitNumber modelUnitNumber = null;
        PropertyName modelPropertyName = null;

        if (unitNumber != null) {
            modelUnitNumber = new UnitNumber(unitNumber);
        }

        if (houseNumber != null) {
            modelHouseNumber = new HouseNumber(houseNumber);
        }

        if (propertyName != null) {
            modelPropertyName = new PropertyName(propertyName);
        }

        boolean modelIsAvailable = isAvailable;

        Listing modelListing = Listing.of(modelPostalCode,
                modelUnitNumber,
                modelHouseNumber,
                priceRange.toModelType(),
                modelPropertyName,
                new HashSet<>(),
                getModelOwners(addressBook.getPersonList()),
                modelIsAvailable);

        for (JsonAdaptedTag jsonAdaptedTag : tags) {
            Tag tag = jsonAdaptedTag.toModelType(addressBook);

            modelListing.addTag(tag);
            tag.addListing(modelListing);

            addressBook.setTag(tag, tag);
        }

        for (Person owner : modelListing.getOwners()) {
            owner.addListing(modelListing);
        }

        return modelListing;
    }


    private List<Person> getModelOwners(ObservableList<Person> personList) {
        List<Person> owners = new ArrayList<>();
        for (String key : ownerKeys) {

            for (Person person : personList) {
                if (person == null) {
                    continue;
                }

                if (!person.getPhone().value.equals(key)) {
                    continue;
                }

                owners.add(person);

            }
        }
        return owners;
    }

}
