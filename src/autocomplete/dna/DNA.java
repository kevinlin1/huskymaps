package autocomplete.dna;

import autocomplete.Autocomplete;
import autocomplete.TreeSetAutocomplete;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Search DNA by autocompleting across all suffixes.
 */
class DNA {
    /**
     * Maximum number of matches to print.
     */
    private static final int MAX_MATCHES = 10;
    /**
     * Path to the DNA text.
     */
    private static final String PATH = "data/ecoli.txt";

    public static void main(String[] args) throws IOException {
        String dna = new Scanner(new FileInputStream(PATH)).next();
        Autocomplete autocomplete = new TreeSetAutocomplete();
        autocomplete.addAll(new SuffixCollection(dna));

        Scanner stdin = new Scanner(System.in);
        System.out.print("Query: ");
        while (stdin.hasNextLine()) {
            String prefix = stdin.nextLine();
            if (prefix.isEmpty()) {
                System.exit(0);
            }
            List<CharSequence> matches = autocomplete.allMatches(prefix);
            System.out.println(matches.size() + " matches");
            for (int i = 0; i < Math.min(matches.size(), MAX_MATCHES); i += 1) {
                CharSequence match = matches.get(i);
                if (match.length() >= 97) {
                    match = match.subSequence(0, 97) + "...";
                }
                System.out.println(match);
            }
            System.out.println();
            System.out.print("Query: ");
        }
    }
}
