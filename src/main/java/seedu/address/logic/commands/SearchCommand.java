package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_NO_RESULT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BEFORE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AFTER;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.applicant.IdentifierPredicate;

/**
 * Finds and lists all applicants in the applicant records that match the given criteria.
 * Keyword matching is case-insensitive, exact match needed
 */
public class SearchCommand extends Command {

    /**
     * Command word to trigger the search functionality in the application.
     */
    public static final String COMMAND_WORD = "search";

    /**
     * Usage message displayed when the user provides an incorrect format for the search command.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Searches for applicants who match at least one of the specified criteria (logical OR).\n"
            + "Parameters: [" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_JOB_POSITION + "JOB_POSITION] "
            + "[" + PREFIX_STATUS + "STATUS] "
            + "[" + PREFIX_BEFORE + "YYYY-MM-DD] "
            + "[" + PREFIX_AFTER + "YYYY-MM-DD]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "John "
            + PREFIX_EMAIL + "alice@example.com";

    /**
     * The list of predicates used to filter the applicant list.
     * Each predicate corresponds to a specific search criterion (e.g., name, email).
     */
    private final List<IdentifierPredicate> predicates;

    /**
     * Constructs a {@code SearchCommand} with the specified list of predicates.
     *
     * @param predicates A list of predicates to filter the applicants.
     */
    public SearchCommand(List<IdentifierPredicate> predicates) {
        requireNonNull(predicates);
        this.predicates = predicates;
    }

    /**
     * Executes the search command by filtering the applicant list based on the given predicates.
     *
     * @param model The model containing the list of applicants.
     * @return A {@code CommandResult} indicating the number of matching applicants.
     * @throws CommandException If no applicants match the search criteria.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Combine all predicates using logical OR (at least one condition needs to be met)
        model.updateFilteredPersonList(person -> predicates.stream()
                .anyMatch(p -> p.test(person)));

        int count = model.getFilteredPersonList().size();
        if (count == 0) {
            throw new CommandException(MESSAGE_NO_RESULT);
        }

        return new CommandResult(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, count));
    }

    /**
     * Checks if this {@code SearchCommand} is equal to another object.
     *
     * @param other The object to compare.
     * @return True if the other object is a {@code SearchCommand} with the same predicates.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof SearchCommand otherSearchCommand)) {
            return false;
        }

        List<IdentifierPredicate> otherPredicates = otherSearchCommand.predicates;
        if (predicates.size() != otherPredicates.size()) {
            return false;
        }
        for (int i = 0; i < predicates.size(); i++) {
            IdentifierPredicate thisPredicate = predicates.get(i);
            IdentifierPredicate thatPredicate = otherPredicates.get(i);
            if (!thisPredicate.equals(thatPredicate)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the hash code value for this {@code SearchCommand}.
     * The hash code is computed based on the list of predicates,
     * ensuring that two {@code SearchCommand} objects with the same predicates
     * will return the same hash code.
     *
     * @return the hash code value for this command.
     */
    @Override
    public int hashCode() {
        return predicates.hashCode();
    }

    /**
     * Returns a string representation of this {@code SearchCommand}.
     *
     * @return A string representation containing the predicates used in the search.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicates)
                .toString();
    }
}
