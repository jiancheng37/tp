package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Stores mapping of prefixes to their respective arguments.
 * Each key may be associated with multiple argument values.
 * Values for a given key are stored in a list, and the insertion ordering is maintained.
 * Keys are unique, but the list of argument values may contain duplicate argument values, i.e. the same argument value
 * can be inserted multiple times for the same prefix.
 */
public class ArgumentMultimap {
    private static final String EMPTY_PREFIX = "";
    private static final String NEW_TAG_PREFIX = "nt/";
    private static final String TAG_PREFIX = "t/";
    private static final int SINGLE_VALUE = 1;
    private static final int FIRST_OCCURRENCE = 1;

    /** Prefixes mapped to their respective arguments**/
    private final Map<Prefix, List<String>> argMultimap = new HashMap<>();

    /**
     * Associates the specified argument value with {@code prefix} key in this map.
     * If the map previously contained a mapping for the key, the new value is appended to the list of existing values.
     *
     * @param prefix   Prefix key with which the specified argument value is to be associated
     * @param argValue Argument value to be associated with the specified prefix key
     */
    public void put(Prefix prefix, String argValue) {
        List<String> argValues = getAllValues(prefix);
        argValues.add(argValue);
        argMultimap.put(prefix, argValues);
    }

    /**
     * Returns the last value of {@code prefix}.
     */
    public Optional<String> getValue(Prefix prefix) {
        List<String> values = getAllValues(prefix);
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(values.size() - 1));
    }

    /**
     * Returns all values of {@code prefix}.
     * If the prefix does not exist or has no values, this will return an empty list.
     * Modifying the returned list will not affect the underlying data structure of the ArgumentMultimap.
     */
    public List<String> getAllValues(Prefix prefix) {
        if (!argMultimap.containsKey(prefix)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(argMultimap.get(prefix));
    }

    /**
     * Returns the preamble (text before the first valid prefix). Trims any leading/trailing spaces.
     */
    public String getPreamble() {
        return getValue(new Prefix(EMPTY_PREFIX)).orElse(EMPTY_PREFIX);
    }

    /**
     * Throws a {@code ParseException} if any of the prefixes given in {@code prefixes} appeared more than
     * once among the arguments.
     */
    public void verifyNoDuplicatePrefixesFor(Prefix... prefixes) throws ParseException {
        Prefix[] duplicatedPrefixes = Stream.of(prefixes).distinct()
                .filter(prefix -> argMultimap.containsKey(prefix) && argMultimap.get(prefix).size() > SINGLE_VALUE)
                .toArray(Prefix[]::new);

        if (duplicatedPrefixes.length > 0) {
            throw new ParseException(Messages.getErrorMessageForDuplicatePrefixes(duplicatedPrefixes));
        }
    }

    /**
     * Throws a {@code ParseException} if any values of new tag or tag prefix given in {@code prefixes}
     * appears more than once.
     */
    public void verifyNoDuplicateTagValues(String messageUsage) throws ParseException {
        Prefix newTagPrefix = new Prefix(NEW_TAG_PREFIX);
        Prefix tagPrefix = new Prefix(TAG_PREFIX);
        List<String> newTagValues = getAllValues(newTagPrefix);
        List<String> tagValues = getAllValues(tagPrefix);

        List<String> allTagValues = new ArrayList<>();
        allTagValues.addAll(newTagValues);
        allTagValues.addAll(tagValues);
        Map<String, Integer> valueCounts = new HashMap<>();

        for (String value : allTagValues) {
            String tagName = value.trim().toUpperCase();
            valueCounts.put(tagName, valueCounts.getOrDefault(tagName, 0) + FIRST_OCCURRENCE);

            if (valueCounts.get(tagName) > SINGLE_VALUE) {
                throw new ParseException(String.format(Messages.MESSAGE_DUPLICATE_VALUES_FOR_TAGS, tagName,
                        messageUsage));
            }
        }
    }
}
