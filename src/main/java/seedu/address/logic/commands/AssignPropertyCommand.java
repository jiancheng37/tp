package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;

/**
 * Assigns a {@code Listing} (property) identified using it's displayed index in the address book
 * to a {@code Person} identified using it's displayed index in the address book as an owner.
 */
public class AssignPropertyCommand extends Command {
    public static final String COMMAND_WORD = "assignProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add a property (to sell) to a person "
            + "Parameters: PERSON_INDEX (must be a positive integer) "
            + "PROPERTY_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2 1";

    public static final String MESSAGE_SUCCESS = "Property %1$s added to person.";
    public static final String MESSAGE_OWNER_ALREADY_IN_LISTING = "This person is already an owner of this property";

    private final Index personIndex;
    private final Index propertyIndex;

    /**
     * Creates an {@code AssignPropertyCommand} to add the specified {@code Listing} (property) to {@code Person}.
     *
     * @param personIndex Index of the person in the filtered person list to assign the property to
     * @param propertyIndex Index of the property (listing) in the filtered listing list to assign
     */
    public AssignPropertyCommand(Index personIndex, Index propertyIndex) {
        this.personIndex = personIndex;
        this.propertyIndex = propertyIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownPersonList = model.getFilteredPersonList();
        if (personIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToAssignProperty = lastShownPersonList.get(personIndex.getZeroBased());

        List<Listing> lastShownListingList = model.getFilteredListingList();
        if (propertyIndex.getZeroBased() >= lastShownListingList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX);
        }
        Listing property = lastShownListingList.get(propertyIndex.getZeroBased());

        if (property.getOwners().contains(personToAssignProperty)) {
            throw new CommandException(MESSAGE_OWNER_ALREADY_IN_LISTING);
        }

        // Add the property to the person (and the person as an owner to the property)
        property.addOwner(personToAssignProperty);
        personToAssignProperty.addListing(property);

        // Update the model
        model.setPerson(personToAssignProperty, personToAssignProperty);
        model.setListing(property, property);
        
        // Format a success message
        String propertyDetails = formatPropertyDetails(property);
        return new CommandResult(String.format(MESSAGE_SUCCESS, propertyDetails));
    }
    
    /**
     * Formats property details to display in success message
     */
    private String formatPropertyDetails(Listing property) {
        StringBuilder details = new StringBuilder();
        details.append(property.getPostalCode().toString());
        
        if (property.getUnitNumber() != null) {
            details.append(" ").append(property.getUnitNumber().toString());
        } else if (property.getHouseNumber() != null) {
            details.append(" ").append(property.getHouseNumber().toString());
        }
        
        return details.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignPropertyCommand)) {
            return false;
        }

        AssignPropertyCommand otherAssignPropertyCommand = (AssignPropertyCommand) other;
        return personIndex.equals(otherAssignPropertyCommand.personIndex)
                && propertyIndex.equals(otherAssignPropertyCommand.propertyIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("propertyIndex", propertyIndex)
                .toString();
    }
} 