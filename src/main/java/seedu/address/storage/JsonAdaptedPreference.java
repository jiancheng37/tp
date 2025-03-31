package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of a preference.
 */
class JsonAdaptedPreference {

    private final JsonAdaptedPriceRange priceRange;
    private final List<JsonAdaptedTag> tags;

    /**
     * Constructs a {@code JsonAdaptedPreference} with the given details.
     */
    @JsonCreator
    public JsonAdaptedPreference(@JsonProperty("priceRange") JsonAdaptedPriceRange priceRange,
                                 @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.priceRange = priceRange;
        this.tags = (tags != null) ? tags : new ArrayList<>();
    }

    /**
     * Converts a given {@code PriceRange} into this class for Jackson use.
     */
    public JsonAdaptedPreference(PropertyPreference source) {
        this.priceRange = new JsonAdaptedPriceRange(source.getPriceRange());
        this.tags = source.getTags().stream().map(JsonAdaptedTag::new).collect(Collectors.toList());
    }

    /**
     * Converts this Jackson-friendly adapted preference object into the model's {@code PriceRange} object.
     */
    public PropertyPreference toModelType(AddressBook addressBook, Person person) throws IllegalValueException {
        if (priceRange == null) {
            throw new IllegalValueException("PropertyPreference's priceRange cannot be null.");
        }

        if (tags == null) {
            throw new IllegalValueException("PropertyPreference's tags cannot be null.");
        }

        if (person == null) {
            throw new IllegalValueException("PropertyPreference's person cannot be null.");
        }

        PropertyPreference modelPreference = new PropertyPreference(priceRange.toModelType(), new HashSet<>(), person);

        for (JsonAdaptedTag jsonAdaptedTag : tags) {
            Tag tag = jsonAdaptedTag.toModelType(addressBook);

            modelPreference.addTag(tag);
            tag.addPropertyPreference(modelPreference);

            addressBook.setTag(tag, tag);
        }

        return modelPreference;
    }

}
