package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_NO_RESULT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.applicant.Applicant;
import seedu.address.model.applicant.IdentifierPredicate;
import seedu.address.model.applicant.JobPosition;
import seedu.address.model.applicant.Status;

/**
 * Summarizes all applicants in the applicant records that match the given criteria.
 * Keyword matching is case-insensitive, exact match needed
 */
public class SummaryCommand extends Command {
    public static final String COMMAND_WORD = "summary";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Summarizes applicants who match all of the specified criteria (logical AND).\n"
            + "Parameters: [" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_JOB_POSITION + "JOB_POSITION] "
            + "[" + PREFIX_STATUS + "STATUS] "
            + "[" + PREFIX_BEFORE + "YYYY-MM-DD] "
            + "[" + PREFIX_AFTER + "YYYY-MM-DD]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_JOB_POSITION + "Senior SWE";

    public static final String MESSAGE_SUCCESS = "Summarized %1$d / %2$d Applicants \n%3$s";

    /**
     * The list of predicates used to filter the applicant list.
     * Each predicate corresponds to a specific summary criterion (e.g., name, email).
     */
    private final List<IdentifierPredicate> predicates;

    /**
     * Creates a SummaryCommand to summarise the applicants based on the given identifier
     *
     * @param predicates A list of predicates to filter the applicants for summary.
     */
    public SummaryCommand(List<IdentifierPredicate> predicates) {
        // Note: Predicate can be null i.e summarise all candidates
        this.predicates = predicates;
    }

    /**
     * Executes the summary command, optionally filtering the applicant list,
     * and producing statistics grouped by job position and application status.
     *
     * @param model The application's model.
     * @return A {@code CommandResult} with formatted summary data.
     * @throws CommandException If the command cannot be executed.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ObservableList<Applicant> applicants = model.getAddressBook().getPersonList();

        // Combine all predicates using logical AND (all conditions must be met)
        ObservableList<Applicant> filteredList =
                applicants.filtered(person -> predicates.stream()
                        .allMatch(p -> p.test(person)));

        model.updateFilteredPersonList(person -> predicates.stream()
                .allMatch(p -> p.test(person)));

        if (filteredList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_RESULT);
        }

        // Count how many Applicants per JobPosition
        Map<JobPosition, Long> jobPositionCountMap = filteredList.stream()
                .collect(Collectors.groupingBy(Applicant::getJobPosition, Collectors.counting()));

        // Count how many Applicants per Status
        Map<Status, Long> statusCountMap = filteredList.stream()
                .collect(Collectors.groupingBy(Applicant::getStatus, Collectors.counting()));

        // Get strings:
        String jobPositionStats = jobPositionCountMap.entrySet()
                .stream()
                .map(entry -> entry.getKey().toString() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        String statusStats = statusCountMap.entrySet()
                .stream()
                .map(entry -> entry.getKey().toString() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        String statisticsString = "Job Positions -> \n["
                + jobPositionStats
                + "] \nStatuses -> \n["
                + statusStats + "]";

        return new CommandResult(String.format(
                String.format(MESSAGE_SUCCESS, filteredList.size(), applicants.size(), statisticsString)));
    }

    /**
     * Checks if this {@code SummaryCommand} is equal to another object.
     *
     * @param other The object to compare.
     * @return True if the other object is a {@code SummaryCommand} with the same predicates.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof SummaryCommand otherSummaryCommand)) {
            return false;
        }

        List<IdentifierPredicate> otherPredicates = otherSummaryCommand.predicates;
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
     * Returns a string representation of this {@code SummaryCommand}.
     *
     * @return A string representation containing the predicates used in the summary.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicates)
                .toString();
    }
}
