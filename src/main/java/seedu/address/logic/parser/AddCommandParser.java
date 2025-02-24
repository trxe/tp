package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SELLER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_ACTOR;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_PREAMBLE;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.buyer.AddBuyerCommand;
import seedu.address.logic.commands.property.AddPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.field.Actor;
import seedu.address.model.field.Email;
import seedu.address.model.field.Name;
import seedu.address.model.field.Person;
import seedu.address.model.field.Phone;
import seedu.address.model.field.Price;
import seedu.address.model.property.Address;
import seedu.address.model.property.Buyer;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {
    private static final int ACTOR_POSITIONAL_INDEX = 0;
    private static final int NUM_OF_PREAMBLE_ARGS = 1;
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE,
                PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG, PREFIX_SELLER, PREFIX_PRICE);

        Actor actor;

        String preamble = argMultimap.getPreamble();
        if (preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        try {
            ParserUtil.assertPreambleArgsCount(preamble, NUM_OF_PREAMBLE_ARGS);
            actor = ParserUtil.parseActor(preamble, ACTOR_POSITIONAL_INDEX);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_PREAMBLE, preamble,
                    AddCommand.EXPECTED_PREAMBLE), pe);
        }

        switch (actor) {
        case PROPERTY:
            return getAddPropertyCommand(argMultimap);
        case BUYER:
            return getAddBuyerCommand(argMultimap);
        default:
            throw new ParseException(MESSAGE_INVALID_ACTOR);
        }
    }

    private AddPropertyCommand getAddPropertyCommand(ArgumentMultimap argMultimap) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS,
                PREFIX_PHONE, PREFIX_EMAIL, PREFIX_SELLER, PREFIX_PRICE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddPropertyCommand.MESSAGE_USAGE));
        }
        Property property = getProperty(argMultimap);
        return new AddPropertyCommand(property);
    }

    private AddBuyerCommand getAddBuyerCommand(ArgumentMultimap argMultimap) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_PRICE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddBuyerCommand.MESSAGE_USAGE));
        }
        Buyer buyer = getBuyer(argMultimap);
        return new AddBuyerCommand(buyer);
    }

    private Property getProperty(ArgumentMultimap argMultimap) throws ParseException {
        Name propertyName = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Name sellerName = ParserUtil.parseName(argMultimap.getValue(PREFIX_SELLER).get());
        Phone sellerPhone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email sellerEmail = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Person seller = new Person(sellerName, sellerPhone, sellerEmail);
        Price price = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_PRICE).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Property property = new Property(propertyName, address, seller, price, tagList);
        return property;
    }

    private Buyer getBuyer(ArgumentMultimap argMultimap) throws ParseException {
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Price maxPrice = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_PRICE).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Buyer buyer = new Buyer(name, phone, email, maxPrice, tagList);
        return buyer;
    }


    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
