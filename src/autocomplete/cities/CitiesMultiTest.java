package autocomplete.cities;

import autocomplete.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Compare different {@link Autocomplete} implementations to check that they compute the same values.
 *
 * @see Autocomplete
 * @see TreeSetAutocomplete
 * @see SequentialSearchAutocomplete
 * @see BinarySearchAutocomplete
 * @see TernarySearchTreeAutocomplete
 */
class CitiesMultiTest {
    /**
     * Maximum number of matches to print.
     */
    private static final int MAX_MATCHES = 10;
    /**
     * Maximum number of cities to parse.
     */
    private static final int MAX_CITIES = 500000;
    /**
     * Path to the cities dataset.
     */
    private static final String PATH = "data/cities.tsv";

    /**
     * Print up to the first {@code MAX_MATCHES} given matches.
     */
    private static void printMatches(List<CharSequence> matches) {
        System.out.println(matches.size() + " matches");
        for (int i = 0; i < Math.min(matches.size(), MAX_MATCHES); i += 1) {
            System.out.println(matches.get(i));
        }
    }

    public static void main(String[] args) throws IOException {
        Set<String> unique = new HashSet<>(MAX_CITIES, 1.0f);
        Scanner input = new Scanner(new FileInputStream(PATH));
        while (input.hasNextLine() && unique.size() < MAX_CITIES) {
            Scanner line = new Scanner(input.nextLine()).useDelimiter("\t");
            String city = line.next();
            // line.nextInt();
            unique.add(city);
        }
        System.out.println(unique.size() + " cities loaded");

        // Reference implementation.
        Autocomplete reference = new TreeSetAutocomplete();
        reference.addAll(unique);

        // Testing implementations.
        Map<String, Autocomplete> implementations = Map.of(
                "SequentialSearchAutocomplete", new SequentialSearchAutocomplete(),
                "BinarySearchAutocomplete", new BinarySearchAutocomplete(),
                "TernarySearchTreeAutocomplete", new TernarySearchTreeAutocomplete()
        );
        // Add cities to each testing implementation.
        for (Autocomplete autocomplete : implementations.values()) {
            autocomplete.addAll(unique);
        }

        Scanner stdin = new Scanner(System.in);
        System.out.print("Query: ");
        while (stdin.hasNextLine()) {
            String prefix = stdin.nextLine();
            if (prefix.isEmpty()) {
                System.exit(0);
            }

            // Ground truth for the given query.
            List<CharSequence> referenceMatches = reference.allMatches(prefix);
            // Sort the output so things can be compared.
            referenceMatches.sort(CharSequence::compare);
            printMatches(referenceMatches);

            // Check each implementation against the reference matches.
            for (String name : implementations.keySet()) {
                Autocomplete autocomplete = implementations.get(name);
                List<CharSequence> matches = autocomplete.allMatches(prefix);
                matches.sort(CharSequence::compare);
                if (matches.equals(referenceMatches)) {
                    System.out.println(name + " PASS!");
                } else {
                    System.out.println(name + " FAIL!");
                    printMatches(matches);
                }
            }
            System.out.println();
            System.out.print("Query: ");
        }
    }
}
