package autocomplete;

import java.util.Collection;

/**
 * Tests for the {@link TernarySearchTreeAutocomplete} class.
 *
 * @see TernarySearchTreeAutocomplete
 */
public class TernarySearchTreeAutocompleteTests extends AutocompleteTests {
    @Override
    public Autocomplete createAutocomplete(Collection<? extends CharSequence> terms) {
        return new TernarySearchTreeAutocomplete(terms);
    }

    public static void main(String[] args) {
        runtimeExperiments(TernarySearchTreeAutocomplete::new);
    }
}
