package autocomplete;

import java.util.Collection;

/**
 * Tests for the {@link BinarySearchAutocomplete} class.
 *
 * @see BinarySearchAutocomplete
 */
public class BinarySearchAutocompleteTests extends AutocompleteTests {
    @Override
    public Autocomplete createAutocomplete(Collection<? extends CharSequence> terms) {
        return new BinarySearchAutocomplete(terms);
    }

    public static void main(String[] args) {
        runtimeExperiments(BinarySearchAutocomplete::new);
    }
}
