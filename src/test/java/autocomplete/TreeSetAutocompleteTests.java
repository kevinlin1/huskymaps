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
}
