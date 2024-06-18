package autocomplete;

/**
 * Tests for the {@link TernarySearchTreeAutocomplete} class.
 *
 * @see TernarySearchTreeAutocomplete
 */
public class TernarySearchTreeAutocompleteTests extends AutocompleteTests {
    @Override
    public Autocomplete createAutocomplete() {
        return new TernarySearchTreeAutocomplete();
    }
}
