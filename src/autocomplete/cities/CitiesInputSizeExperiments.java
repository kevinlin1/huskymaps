package autocomplete.cities;

import autocomplete.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Supplier;

/**
 * Run timing experiments and save a CSV file for each {@link Autocomplete} implementation.
 */
class CitiesInputSizeExperiments {
    /**
     * Maximum number of cities to include in each experiment.
     */
    private static final int MAX_CITIES = 100000;
    /**
     * Step size increment. Making this smaller means experiments run slower.
     */
    private static final int INPUT_STEP_SIZE = 10000;
    /**
     * Number of trials per implementation run. Making this smaller means experiments run faster.
     */
    private static final int NUM_TRIALS = 1000;
    /**
     * Prefix used for benchmarking {@code allMatches}.
     */
    private static final String TEST_PREFIX = "Sea";
    /**
     * Path to the cities dataset.
     */
    private static final String PATH = "data/cities.tsv";

    public static void main(String[] args) throws IOException {
        Set<String> unique = new HashSet<>(MAX_CITIES, 1.0f);
        Scanner input = new Scanner(new FileInputStream(PATH));
        while (input.hasNextLine() && unique.size() < MAX_CITIES) {
            Scanner line = new Scanner(input.nextLine()).useDelimiter("\t");
            String city = line.next();
            // line.nextInt();
            unique.add(city);
        }
        // Shuffle the cities to keep things interesting.
        List<String> cities = new ArrayList<>(unique);
        Collections.shuffle(cities);
        System.out.println(cities.size() + " cities loaded");

        // Testing implementations.
        Map<String, Supplier<Autocomplete>> implementations = Map.of(
                "TreeSetAutocomplete", TreeSetAutocomplete::new,
                "LinearSearchAutocomplete", SequentialSearchAutocomplete::new,
                "BinarySearchAutocomplete", BinarySearchAutocomplete::new,
                "TernarySearchTreeAutocomplete", TernarySearchTreeAutocomplete::new
        );
        for (String name : implementations.keySet()) {
            System.out.println();
            System.out.println(name);
            new File(TEST_PREFIX).mkdir();
            PrintStream out = new PrintStream(TEST_PREFIX + "/" + name + ".csv");

            // N = size of the autocompletion dataset.
            for (int N = INPUT_STEP_SIZE; N <= cities.size(); N += INPUT_STEP_SIZE) {
                System.out.println("N = " + N);
                out.print(N);
                out.print(',');

                // Make a new test input dataset containing the first N cities.
                // Defensive programming: make a copy of the list to handle buggy implementations.
                List<String> dataset = new ArrayList<>(cities.subList(0, N));

                // Record the total runtimes for this dataset.
                double totalAddTime = 0.0;
                double totalQueryTime = 0.0;

                for (int i = 0; i < NUM_TRIALS; i += 1) {
                    Autocomplete autocomplete = implementations.get(name).get();

                    // First, measure addAll.
                    long addStart = System.nanoTime();
                    autocomplete.addAll(dataset);
                    long addTime = System.nanoTime() - addStart;
                    // Convert from nanoseconds to seconds and add to total time.
                    totalAddTime += (double) addTime / 1_000_000_000;

                    // With the dataset loaded, measure allMatches.
                    long queryStart = System.nanoTime();
                    autocomplete.allMatches(TEST_PREFIX);
                    long queryTime = System.nanoTime() - queryStart;
                    totalQueryTime += (double) queryTime / 1_000_000_000;
                }

                // Output the averages to 10 decimal places.
                out.printf("%.10f", totalAddTime / NUM_TRIALS);
                out.print(',');
                out.printf("%.10f", totalQueryTime / NUM_TRIALS);
                out.println();
            }

            // Flush the output and release the file handle.
            out.close();
        }
    }
}
