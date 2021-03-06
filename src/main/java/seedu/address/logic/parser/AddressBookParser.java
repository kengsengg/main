package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddAppointmentCommand;
import seedu.address.logic.commands.AddCcaCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddInjuriesHistoryCommand;
import seedu.address.logic.commands.AddNextOfKinCommand;
import seedu.address.logic.commands.AddRemarkCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCcaCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteInjuriesHistoryCommand;
import seedu.address.logic.commands.DeleteRemarkCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.StreamCommand;
import seedu.address.logic.commands.TagDeleteCommand;
import seedu.address.logic.commands.TagReplaceCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord").toLowerCase();
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS:
            return new AddCommandParser().parse(arguments);

        case AddNextOfKinCommand.COMMAND_WORD:
            return new AddNextOfKinCommandParser().parse(arguments);

        case AddCcaCommand.COMMAND_WORD:
            return new AddCcaCommandParser().parse(arguments);

        case DeleteCcaCommand.COMMAND_WORD:
            return new DeleteCcaCommandParser().parse(arguments);

        case AddInjuriesHistoryCommand.COMMAND_WORD:
            return new AddInjuriesHistoryCommandParser().parse(arguments);

        case DeleteInjuriesHistoryCommand.COMMAND_WORD:
            return new DeleteInjuriesHistoryCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_ALIAS:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_ALIAS:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            return new FindCommandParser().parse(arguments);

        case TagDeleteCommand.COMMAND_WORD:
        case TagDeleteCommand.COMMAND_ALIAS:
            return new TagDeleteCommandParser().parse(arguments);

        case TagReplaceCommand.COMMAND_WORD:
        case TagReplaceCommand.COMMAND_ALIAS:
            return new TagReplaceCommandParser().parse(arguments);

        case AddAppointmentCommand.COMMAND_WORD:
        case AddAppointmentCommand.COMMAND_ALIAS:
            return new AddAppointmentCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

        case AddRemarkCommand.COMMAND_WORD:
            return new AddRemarkCommandParser().parse(arguments);

        case DeleteRemarkCommand.COMMAND_WORD:
            return new DeleteRemarkCommandParser().parse(arguments);

        case StreamCommand.COMMAND_WORD:
            return new StreamCommandParser().parse(arguments);

        case SortCommand.COMMAND_WORD:
            return new SortCommandParser().parse(arguments);

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
