package seamfinding;

/**
 * Tests for the {@link DynamicProgrammingSeamFinder} class.
 *
 * @see DynamicProgrammingSeamFinder
 */
public class DynamicProgrammingSeamFinderTests extends SeamFinderTests {
    @Override
    public SeamFinder createSeamFinder() {
        return new DynamicProgrammingSeamFinder();
    }

    public static void main(String[] args) {
        runtimeExperiments(DynamicProgrammingSeamFinder::new);
    }
}
