//@@author sofarsophie
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CUSTOM_FIELD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate.FindFields;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_CUSTOM_FIELD,
                        PREFIX_ADDRESS);

        if (!(arePrefixesPresent(argMultimap, PREFIX_NAME) || arePrefixesPresent(argMultimap, PREFIX_PHONE)
            || (arePrefixesPresent(argMultimap, PREFIX_EMAIL)) || arePrefixesPresent(argMultimap, PREFIX_ADDRESS)
            || arePrefixesPresent(argMultimap, PREFIX_CUSTOM_FIELD))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        FindFields fieldsToFind = new FindFields();
        try {
            parseNamesForSearch(argMultimap.getAllValues(PREFIX_NAME)).ifPresent(fieldsToFind::setNameKeywords);
            parsePhonesForSearch(argMultimap.getAllValues(PREFIX_PHONE)).ifPresent(fieldsToFind::setPhoneKeywords);
            parseEmailsForSearch(argMultimap.getAllValues(PREFIX_EMAIL)).ifPresent(fieldsToFind::setEmailKeywords);
            parseAddressesForSearch(argMultimap.getAllValues(PREFIX_ADDRESS))
                    .ifPresent(fieldsToFind::setAddressKeywords);
            parseCustomFieldsForSearch(argMultimap.getAllValues(PREFIX_CUSTOM_FIELD))
                    .ifPresent(fieldsToFind::setFieldsKeywords);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        return new FindCommand(new PersonContainsKeywordsPredicate(fieldsToFind));
    }

    /**
     * Parses {@code List<String> names} into a {@code List<Name>} if {@code names} is non-empty.
     * If {@code names} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Name>} containing zero names.
     */
    private Optional<List<Name>> parseNamesForSearch(List<String> names) throws IllegalValueException {
        assert names != null;

        if (names.isEmpty()) {
            return Optional.empty();
        }
        List<String> nameList = names.size() == 1 && names.contains("") ? Collections.emptyList() : names;
        return Optional.of(ParserUtil.parseNames(nameList));
    }

    /**
     * Parses {@code List<String> phones} into a {@code List<Phone>} if {@code phones} is non-empty.
     * If {@code phones} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Phone>} containing zero phones.
     */
    private Optional<List<Phone>> parsePhonesForSearch(List<String> phones) throws IllegalValueException {
        assert phones != null;

        if (phones.isEmpty()) {
            return Optional.empty();
        }
        List<String> phoneList = phones.size() == 1 && phones.contains("") ? Collections.emptyList() : phones;
        return Optional.of(ParserUtil.parsePhones(phoneList));
    }

    /**
     * Parses {@code List<String> emails} into a {@code List<Email>} if {@code emails} is non-empty.
     * If {@code emails} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Email>} containing zero emails.
     */
    private Optional<List<Email>> parseEmailsForSearch(List<String> emails) throws IllegalValueException {
        assert emails != null;

        if (emails.isEmpty()) {
            return Optional.empty();
        }
        List<String> emailList = emails.size() == 1 && emails.contains("") ? Collections.emptyList() : emails;
        return Optional.of(ParserUtil.parseEmails(emailList));
    }

    /**
     * Parses {@code List<String> addresses} into a {@code List<Address>} if {@code addresses} is non-empty.
     * If {@code addresses} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Address>} containing zero addresses.
     */
    private Optional<List<Address>> parseAddressesForSearch(List<String> addresses) throws IllegalValueException {
        assert addresses != null;

        if (addresses.isEmpty()) {
            return Optional.empty();
        }
        List<String> addressList = addresses.size() == 1 && addresses.contains("")
                ? Collections.emptyList() : addresses;
        return Optional.of(ParserUtil.parseAddresses(addressList));
    }

    //@@author syy94
    /**
     * Parses {@code List<String> fields} into a {@code List<CustomField>} if {@code fields} is non-empty.
     * If {@code fields} contain only one element which is an empty string, it will be parsed into a
     * {@code List<Address>} containing zero addresses.
     */
    private Optional<List<String>> parseCustomFieldsForSearch(List<String> fields) throws IllegalValueException {
        assert fields != null;

        if (fields.isEmpty()) {
            return Optional.empty();
        }
        List<String> fieldList = fields.size() == 1 && fields.contains("")
                ? Collections.emptyList() : fields;
        return Optional.of(fieldList);
    }
    //@@author

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
//@@author
