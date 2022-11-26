package autocomplete;

/**
 * Tests for the {@link TreeSetAutocomplete} class.
 *
 * @see TreeSetAutocomplete
 */
public class TreeSetAutocompleteTests extends AutocompleteTests {
    @Override
    public Autocomplete createAutocomplete() {
        return new TreeSetAutocomplete();
    }

    @Override
    void comparePrefixSea() {
        // Disable this inherited test for the reference implementation
    }

    @Override
    void compareRandomPrefixes() {
        // Disable this inherited test for the reference implementation
    }
}
