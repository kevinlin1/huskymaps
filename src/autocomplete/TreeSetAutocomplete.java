package autocomplete;

import java.util.*;

/**
 * {@link TreeSet} implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TreeSetAutocomplete implements Autocomplete {
    /**
     * {@link NavigableSet} of added autocompletion terms.
     */
    private final NavigableSet<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public TreeSetAutocomplete() {
        this.terms = new TreeSet<>(CharSequence::compare);
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();
        if (prefix == null || prefix.length() == 0) {
            return result;
        }
        CharSequence start = terms.ceiling(prefix);
        if (start == null) {
            return result;
        }
        for (CharSequence term : terms.tailSet(start)) {
            if (prefix.length() <= term.length()) {
                CharSequence part = term.subSequence(0, prefix.length());
                if (CharSequence.compare(prefix, part) == 0) {
                    result.add(term);
                } else {
                    return result;
                }
            }
        }
        return result;
    }
}
