package autocomplete;

/**
 * Tests for the {@link BinarySearchAutocomplete} class.
 *
 * @see BinarySearchAutocomplete
 */
public class BinarySearchAutocompleteTests extends AutocompleteTests {
    @Override
    public Autocomplete createAutocomplete() {
        return new BinarySearchAutocomplete();
    }
}
