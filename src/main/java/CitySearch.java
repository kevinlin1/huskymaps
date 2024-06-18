import autocomplete.Autocomplete;
import autocomplete.TreeSetAutocomplete;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Search the world's cities.
 */
public class CitySearch {
    /**
     * Maximum number of matches to print.
     */
    private static final int MAX_MATCHES = 10;
    /**
     * Maximum number of cities to parse.
     */
    private static final int MAX_CITIES = 1000000;
    /**
     * Path to the cities dataset.
     */
    private static final String PATH = "data/cities.tsv";

    public static void main(String[] args) throws IOException {
        Map<String, Integer> cities = new LinkedHashMap<>();
        Scanner input = new Scanner(new FileInputStream(PATH));
        for (int i = 0; i < MAX_CITIES && input.hasNextLine(); i += 1) {
            Scanner line = new Scanner(input.nextLine()).useDelimiter("\t");
            cities.put(line.next(), line.nextInt());
        }
        Autocomplete autocomplete = new TreeSetAutocomplete();
        autocomplete.addAll(cities.keySet());

        Scanner stdin = new Scanner(System.in);
        System.out.print("Query: ");
        while (stdin.hasNextLine()) {
            String prefix = stdin.nextLine();
            if (prefix.isEmpty()) {
                System.exit(0);
            }
            List<CharSequence> matches = autocomplete.allMatches(prefix);
            System.out.println(matches.size() + " matches");
            matches.sort(Comparator.comparingInt(cities::get).reversed());
            for (int i = 0; i < Math.min(matches.size(), MAX_MATCHES); i += 1) {
                System.out.println(matches.get(i));
            }
            System.out.println();
            System.out.print("Query: ");
        }
    }
}
