package autocomplete;

import java.util.Collection;

/**
 * Tests for the {@link SequentialSearchAutocomplete} class.
 *
 * @see SequentialSearchAutocomplete
 */
public class SequentialSearchAutocompleteTests extends AutocompleteTests {
    @Override
    public Autocomplete createAutocomplete(Collection<? extends CharSequence> terms) {
        return new SequentialSearchAutocomplete(terms);
    }

    public static void main(String[] args) {
        runtimeExperiments(SequentialSearchAutocomplete::new);
    }
}
