package seedu.address.storage;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;

/**
 * Jackson-friendly version of {@link PriceRange}.
 */
class JsonAdaptedPriceRange {

    private final BigDecimal upper;
    private final BigDecimal lower;

    /**
     * Constructs a {@code JsonAdaptedPriceRange} with the given {@code upperPriceRange} and {@code lowerPriceRange}.
     */
    @JsonCreator
    public JsonAdaptedPriceRange(@JsonProperty("upper") BigDecimal upperPriceRange,
                                 @JsonProperty("lower") BigDecimal lowerPriceRange) {
        this.upper = upperPriceRange;
        this.lower = lowerPriceRange;
    }

    /**
     * Converts a given {@code PriceRange} into this class for Jackson use.
     */
    public JsonAdaptedPriceRange(PriceRange source) {
        this.lower = source.lowerBoundPrice == null ? null : source.lowerBoundPrice.price;
        this.upper = source.upperBoundPrice == null ? null : source.upperBoundPrice.price;
    }

    @JsonProperty("upper")
    public BigDecimal getUpperPriceRange() {
        return upper;
    }

    @JsonProperty("lower")
    public BigDecimal getLowerPriceRange() {
        return lower;
    }

    public PriceRange toModelType() throws IllegalValueException {

        PriceRange modelPriceRange = new PriceRange();

        try {
            if (lower != null && upper != null) {
                modelPriceRange = new PriceRange(new Price(lower.toString()), new Price(upper.toString()));
            } else if (lower != null) {
                modelPriceRange = new PriceRange(new Price(lower.toString()), false);
            } else if (upper != null) {
                modelPriceRange = new PriceRange(new Price(upper.toString()), true);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage());
        }

        return modelPriceRange;
    }
}
