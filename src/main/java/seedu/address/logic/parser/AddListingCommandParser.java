package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ADD_LISTING_PREAMBLE_FOUND;
import static seedu.address.logic.Messages.MESSAGE_HOUSE_OR_UNIT_NUMBER_REQUIRED;
import static seedu.address.logic.Messages.MESSAGE_LOWER_GREATER_THAN_UPPER_FOR_PRICE;
import static seedu.address.logic.Messages.MESSAGE_POSTAL_CODE_REQUIRED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOUSE_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOWER_BOUND_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNIT_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UPPER_BOUND_PRICE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.commands.AddListingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.listing.HouseNumber;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.PropertyName;
import seedu.address.model.listing.UnitNumber;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;

/**
 * Parses input arguments and creates a new {@code AddListingCommand} object.
 */
public class AddListingCommandParser implements Parser<AddListingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddListingCommand
     * and returns an AddListingCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddListingCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_POSTAL_CODE, PREFIX_UNIT_NUMBER, PREFIX_HOUSE_NUMBER,
                        PREFIX_LOWER_BOUND_PRICE, PREFIX_UPPER_BOUND_PRICE, PREFIX_PROPERTY_NAME, PREFIX_TAG,
                        PREFIX_NEW_TAG);

        checkCommandFormat(argMultimap);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_POSTAL_CODE, PREFIX_UNIT_NUMBER, PREFIX_HOUSE_NUMBER,
                PREFIX_LOWER_BOUND_PRICE, PREFIX_UPPER_BOUND_PRICE, PREFIX_PROPERTY_NAME);
        PostalCode postalCode = ParserUtil.parsePostalCode(argMultimap.getValue(PREFIX_POSTAL_CODE).get());
        UnitNumber unitNumber = ParserUtil.parseUnitNumber(argMultimap.getValue(PREFIX_UNIT_NUMBER).orElse(null));
        HouseNumber houseNumber = ParserUtil.parseHouseNumber(argMultimap.getValue(PREFIX_HOUSE_NUMBER)
                .orElse(null));
        Price lowerBoundPrice = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_LOWER_BOUND_PRICE)
                .orElse(null));
        Price upperBoundPrice = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_UPPER_BOUND_PRICE)
                .orElse(null));
        PriceRange priceRange = createPriceRange(lowerBoundPrice, upperBoundPrice);
        PropertyName propertyName = ParserUtil.parsePropertyName(argMultimap
                .getValue(PREFIX_PROPERTY_NAME).orElse(null));
        Listing listing = createListing(postalCode, unitNumber, houseNumber, priceRange, propertyName);

        Set<String> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Set<String> newTagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_NEW_TAG));

        return new AddListingCommand(listing, tagList, newTagList);
    }

    private static PriceRange createPriceRange(Price lowerBoundPrice, Price upperBoundPrice) throws ParseException {
        if (lowerBoundPrice == null && upperBoundPrice == null) {
            return new PriceRange();
        } else if (lowerBoundPrice == null) {
            return new PriceRange(upperBoundPrice, true);
        } else if (upperBoundPrice == null) {
            return new PriceRange(lowerBoundPrice, false);
        } else {
            try {
                return new PriceRange(lowerBoundPrice, upperBoundPrice);
            } catch (IllegalArgumentException e) {
                throw new ParseException(String.format(MESSAGE_LOWER_GREATER_THAN_UPPER_FOR_PRICE,
                        AddListingCommand.MESSAGE_USAGE));
            }
        }
    }

    private static Listing createListing(PostalCode postalCode, UnitNumber unitNumber, HouseNumber houseNumber,
                                         PriceRange priceRange, PropertyName propertyName) {
        if (unitNumber == null && propertyName == null) {
            return new Listing(postalCode, houseNumber, priceRange, new HashSet<>(), new ArrayList<>(), true);
        } else if (houseNumber == null && propertyName == null) {
            return new Listing(postalCode, unitNumber, priceRange, new HashSet<>(), new ArrayList<>(), true);
        }

        if (unitNumber == null) {
            return new Listing(postalCode, houseNumber, priceRange, propertyName, new HashSet<>(), new ArrayList<>(),
                    true);
        }
        return new Listing(postalCode, unitNumber, priceRange, propertyName, new HashSet<>(), new ArrayList<>(),
                true);
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap) throws ParseException {
        boolean hasUnitNumber = argMultimap.getValue(PREFIX_UNIT_NUMBER).isPresent();
        boolean hasHouseNumber = argMultimap.getValue(PREFIX_HOUSE_NUMBER).isPresent();
        boolean hasExclusiveHouseOrUnitNumber = hasUnitNumber ^ hasHouseNumber;
        boolean hasPostalCode = argMultimap.getValue(PREFIX_POSTAL_CODE).isPresent();

        if (!hasExclusiveHouseOrUnitNumber) {
            throw new ParseException(String.format(MESSAGE_HOUSE_OR_UNIT_NUMBER_REQUIRED,
                    AddListingCommand.MESSAGE_USAGE));
        }

        if (!hasPostalCode) {
            throw new ParseException(String.format(MESSAGE_POSTAL_CODE_REQUIRED, AddListingCommand.MESSAGE_USAGE));
        }

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ADD_LISTING_PREAMBLE_FOUND,
                    AddListingCommand.MESSAGE_USAGE));
        }
    }

}
