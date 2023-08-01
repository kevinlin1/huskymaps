import minpq.DoubleMapMinPQ;
import minpq.MinPQ;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Simulate a content moderation priority queue with "streaming" data.
 */
public class Moderator {
    /**
     * Path to the toxic content.
     */
    private static final String PATH = "data/toxic.tsv";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new FileInputStream(PATH));
        scanner.nextLine(); // Skip header

        MinPQ<String> pq = new DoubleMapMinPQ<>();
        Random random = new Random();
        addComments(pq, scanner, random.nextInt(100));
        Scanner stdin = new Scanner(System.in);
        while (!pq.isEmpty()) {
            System.out.println();
            System.out.println(pq.removeMin());
            System.out.print("[Y]es/[N]o: ");
            String response = null;
            while (response == null && stdin.hasNextLine()) {
                response = stdin.nextLine();
                switch (response.strip().toLowerCase()) {
                    case "y":
                    case "yes":
                    case "n":
                    case "no":
                        // In a real system, write the response to the database.
                        break;
                    default:
                        response = null;
                        System.out.print("[Y]es/[N]o: ");
                        break;
                }
            }
            if (random.nextBoolean()) {
                addComments(pq, scanner, random.nextInt(4));
            }
        }
    }

    /**
     * Adds up to <i>N</i> comments from the scanner to the priority queue with negated weights.
     *
     * @param pq      the destination priority queue.
     * @param scanner the input scanner.
     * @param n       the number of comments to read from the scanner.
     */
    private static void addComments(MinPQ<String> pq, Scanner scanner, int n) {
        int i = 0;
        for (; i < n && scanner.hasNextLine(); i += 1) {
            Scanner line = new Scanner(scanner.nextLine()).useDelimiter("\t");
            double toxicity = line.nextDouble();
            // Replace all but the first letter in each word.
            String comment = line.next().replaceAll("\\w", "*");
            // Prioritize most toxic content first by negating the weight.
            pq.add(comment, -toxicity);
        }
        System.out.println(i + " comments added to pq");
    }
}
