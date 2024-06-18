package autocomplete;

import java.util.Collection;
import java.util.List;

/**
 * Suggest exact-character prefix matches for any query {@link CharSequence}.
 *
 * @see TreeSetAutocomplete
 * @see SequentialSearchAutocomplete
 * @see BinarySearchAutocomplete
 * @see TernarySearchTreeAutocomplete
 */
public interface Autocomplete {
    /**
     * Adds the given collection of autocompletion terms.
     *
     * @param terms collection containing elements to be added.
     */
    void addAll(Collection<? extends CharSequence> terms);

    /**
     * Returns all autocompletion terms that match the given prefix.
     *
     * @param prefix search query.
     */
    List<CharSequence> allMatches(CharSequence prefix);

    /**
     * Returns true if and only if the given term matches the given prefix.
     *
     * @param prefix prefix template.
     * @param term term to check against the prefix.
     * @return true if and only if the given term matches the given prefix.
     */
    static boolean isPrefixOf(CharSequence prefix, CharSequence term) {
        if (prefix.length() <= term.length()) {
            CharSequence part = term.subSequence(0, prefix.length());
            return CharSequence.compare(prefix, part) == 0;
        }
        return false;
    }
}
