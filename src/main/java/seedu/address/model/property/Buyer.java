package seedu.address.model.property;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.field.Email;
import seedu.address.model.field.Name;
import seedu.address.model.field.Person;
import seedu.address.model.field.Phone;
import seedu.address.model.field.Price;

public class Buyer {

    private final Person person;
    private final Price max;
    // TODO: add Set<Tags> tags

    /**
     * Every field must be present and not null.
     */
    public Buyer(Person person, Price max) {
        requireAllNonNull(person, max);
        this.person = person;
        this.max = max;
    }

    /**
     * Every field must be present and not null.
     */
    public Buyer(Name name, Phone phone, Email email, Price max) {
        requireAllNonNull(name, phone, email, max);
        this.person = new Person(name, phone, email);
        this.max = max;
    }

    public Person getPerson() {
        return person;
    }

    public Price getPrice() {
        return max;
    }

    /**
     * Returns true if both buyers have the same name.
     * This defines a weaker notion of equality between two buyers.
     */
    public boolean isSameBuyer(Buyer otherBuyer) {
        if (otherBuyer == this) {
            return true;
        }

        return otherBuyer != null
                && otherBuyer.getPerson().isSamePerson(getPerson());
    }

    /**
     * Returns true if both properties have the same identity and data fields.
     * This defines a stronger notion of equality between two properties.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Buyer)) {
            return false;
        }

        Buyer otherBuyer = (Buyer) other;
        return otherBuyer.getPerson().equals(getPerson())
                && otherBuyer.getPrice().equals(getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, max);
    }

    @Override
    public String toString() {
        return String.format("%s; Maximum Price: %s", person, max);
    }
}
