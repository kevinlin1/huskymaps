package autocomplete;

import java.util.Collection;

/**
 * Tests for the {@link TreeSetAutocomplete} class.
 *
 * @see TreeSetAutocomplete
 */
public class TreeSetAutocompleteTests extends AutocompleteTests {
    @Override
    public Autocomplete createAutocomplete(Collection<? extends CharSequence> terms) {
        return new TreeSetAutocomplete(terms);
    }

    public static void main(String[] args) {
        runtimeExperiments(TreeSetAutocomplete::new);
    }
}
