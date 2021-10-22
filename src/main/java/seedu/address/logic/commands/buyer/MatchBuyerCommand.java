package seedu.address.logic.commands.buyer;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.MatchOneToManyCommand;
import seedu.address.model.Model;
import seedu.address.model.property.Buyer;
import seedu.address.model.property.Match;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;

/**
 * Match buyer to properties in the address book.
 */
public class MatchBuyerCommand extends MatchOneToManyCommand {

    public static final String MESSAGE_SUCCESS = "Matched buyer to properties.";

    public MatchBuyerCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Buyer> buyerList = model.getFilteredBuyerList();
        Buyer buyer = buyerList.get(targetIndex.getZeroBased());
        Predicate<Buyer> currentBuyerFilter = (b) -> b.equals(buyer);

        Set<Tag> buyerTags = buyer.getTags();
        Predicate<Property> propertyFilter = (property) -> property.getPrice().isLessThanOrEqualTo(buyer.getPrice());


        Comparator<Property> propertyComparator = Comparator.<Property, Integer>comparing(property ->
                Match.getNumCommonTags(buyer, property)
        ).reversed().thenComparingLong(property -> property.getPrice().value);

        model.updateFilteredBuyerList(currentBuyerFilter);
        model.updateFilteredAndSortedPropertyList(propertyFilter, propertyComparator);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MatchBuyerCommand)) {
            return false;
        }

        return super.equals(other);
    }
}
