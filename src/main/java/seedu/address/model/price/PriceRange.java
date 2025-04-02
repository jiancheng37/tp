package seedu.address.model.price;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Price Range in the real estate system.
 * Guarantees: immutable; is valid as Price is valid.
 * A PriceRange can be bounded or unbounded.
 * If it is bounded on both sides, it must have a lowerBoundPrice and an upperBoundPrice.
 * If it is bounded on one side, it must have one null field.
 * If it is unbounded, it must have two null fields.
 */
public class PriceRange {

    private static final String ANY_PRICE_STRING = "Any Price";
    private static final String UP_TO_STRING = "Up to ";
    private static final String FROM_STRING = "From ";
    private static final String TO_STRING = " to ";
    private static final int LESS_THAN = -1;
    private static final int EQUAL_TO = 0;
    private static final int GREATER_THAN = 1;

    public final Price lowerBoundPrice;
    public final Price upperBoundPrice;

    /**
     * Constructs an unbounded {@code PriceRange}.
     */
    public PriceRange() {
        this.lowerBoundPrice = null;
        this.upperBoundPrice = null;
    }

    /**
     * Constructs a {@code PriceRange} with one bound.
     *
     * @param singleBoundPrice the price that is the bound.
     * @param isUpperBound     if true, the singleBoundPrice is the upper bound, otherwise it is the lower bound.
     */
    public PriceRange(Price singleBoundPrice, boolean isUpperBound) {
        requireAllNonNull(singleBoundPrice, isUpperBound);

        if (isUpperBound) {
            this.lowerBoundPrice = null;
            this.upperBoundPrice = singleBoundPrice;
        } else {
            this.lowerBoundPrice = singleBoundPrice;
            this.upperBoundPrice = null;
        }
    }

    /**
     * Constructs a {@code PriceRange} with both bounds.
     *
     * @param lowerBoundPrice the lower bound price.
     * @param upperBoundPrice the upper bound price.
     */
    public PriceRange(Price lowerBoundPrice, Price upperBoundPrice) {
        requireAllNonNull(lowerBoundPrice, upperBoundPrice);
        checkArgument(lowerBoundPrice.compare(upperBoundPrice) <= EQUAL_TO);
        this.lowerBoundPrice = lowerBoundPrice;
        this.upperBoundPrice = upperBoundPrice;
    }

    /**
     * Checks if a price is within the price range.
     *
     * @param otherPrice the price to check.
     * @return true if the price is within the range, false otherwise.
     */
    public boolean isPriceWithinRange(Price otherPrice) {
        requireNonNull(otherPrice);

        // If range is unbounded
        if (this.lowerBoundPrice == null && this.upperBoundPrice == null) {
            return true;
        }

        // If range is only upper bounded
        if (this.lowerBoundPrice == null) {
            return otherPrice.compare(this.upperBoundPrice) <= EQUAL_TO;
        }

        // If range is only lower bounded
        if (this.upperBoundPrice == null) {
            return otherPrice.compare(this.lowerBoundPrice) >= EQUAL_TO;
        }

        // If range is bounded on both sides
        return otherPrice.compare(this.lowerBoundPrice) >= EQUAL_TO
            && otherPrice.compare(this.upperBoundPrice) <= EQUAL_TO;
    }

    /**
     * Checks if two price ranges overlap.
     *
     * @param otherPriceRange the other price range to check.
     * @return true if the price ranges overlap, false otherwise.
     */
    public boolean doPriceRangeOverlap(PriceRange otherPriceRange) {
        requireNonNull(otherPriceRange);

        boolean isOtherLowerBoundWithinRange = otherPriceRange.lowerBoundPrice == null
                || this.isPriceWithinRange(otherPriceRange.lowerBoundPrice);

        boolean isOtherUpperBoundWithinRange = otherPriceRange.upperBoundPrice == null
                || this.isPriceWithinRange(otherPriceRange.upperBoundPrice);

        boolean isThisLowerBoundWithinRange = this.lowerBoundPrice == null
                || otherPriceRange.isPriceWithinRange(this.lowerBoundPrice);

        boolean isThisUpperBoundWithinRange = this.upperBoundPrice == null
                || otherPriceRange.isPriceWithinRange(this.upperBoundPrice);

        return isOtherLowerBoundWithinRange || isOtherUpperBoundWithinRange
                || (isThisLowerBoundWithinRange && isThisUpperBoundWithinRange);
    }

    @Override
    public String toString() {
        if (lowerBoundPrice == null && upperBoundPrice == null) {
            return ANY_PRICE_STRING;
        } else if (lowerBoundPrice == null) {
            return UP_TO_STRING + upperBoundPrice;
        } else if (upperBoundPrice == null) {
            return FROM_STRING + lowerBoundPrice;
        } else {
            return lowerBoundPrice + TO_STRING + upperBoundPrice;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PriceRange)) {
            return false;
        }

        PriceRange otherPriceRange = (PriceRange) other;
        return Objects.equals(this.lowerBoundPrice, otherPriceRange.lowerBoundPrice)
                && Objects.equals(this.upperBoundPrice, otherPriceRange.upperBoundPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBoundPrice, upperBoundPrice);
    }

}
