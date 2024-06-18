import deques.ArrayDeque;
import deques.Deque;

/**
 * Simulate web browser history using deques.
 */
public class BrowserHistory {
    public static void main(String[] args) {
        Deque<String> history = new ArrayDeque<>();
        history.addLast("uw.edu");
        // Latest page is "uw.edu". Visit "my.uw.edu"...
        history.addLast("my.uw.edu");
        // Latest page is "my.uw.edu". Visit "cs.uw.edu"...
        history.addLast("cs.uw.edu");
        // Latest page is "cs.uw.edu". Visit "canvas.uw.edu"...
        history.addLast("canvas.uw.edu");
        System.out.println(history);

        // Latest page is "canvas.uw.edu". Remove this page from the history...
        history.removeLast();
        // Latest page is "cs.uw.edu". Visit "notify.uw.edu"...
        history.addLast("notify.uw.edu");
        // Latest page is "notify.uw.edu". Remove the oldest two pages from the history...
        history.removeFirst();
        history.removeFirst();
        System.out.println(history);
    }
}