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
}
