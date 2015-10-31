import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demonstrates the time difference between using the synchronized keyword and a
 * read/write lock for an IndexedSet object.
 *
 * @see IndexedSet
 * @see SynchronizedSet
 * @see ConcurrentSet
 */
public class SetDemo {
    private static final Logger log = LogManager.getLogger();

    /**
     * Generates a list of generic data elements.
     *
     * @param size
     *            number of data elements to generate
     * @return list of data elements
     */
    public static ArrayList<String> generateData(int size) {
        ArrayList<String> elements = new ArrayList<>(size);

        // use size to determine how many 0s to pad each number
        int length = Integer.toString(size).length();
        String format = "item-%0" + length + "d";

        // add elements to list one at a time
        // (simple, but not necessarily most efficient)
        for (int i = 0; i < size; i++) {
            elements.add(String.format(format, i));
        }

        return elements;
    }

    /**
     * Worker thread that adds elements.
     */
    public static class AddWorker extends Thread {
        private final List<String> source;
        private final IndexedSet<String> destination;

        public AddWorker(List<String> source, IndexedSet<String> destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public void run() {
            log.debug("Adding {} values to set.", source.size());
            destination.addAll(source);
            log.debug("Finished adding values.");
        }
    }

    /**
     * Worker thread that copies sorted elements.
     */
    public static class SortedCopyWorker extends Thread {
        private final IndexedSet<String> destination;

        public SortedCopyWorker(IndexedSet<String> destination) {
            this.destination = destination;
        }

        @Override
        public void run() {
            log.debug("Copying {} sorted values.", destination.size());
            destination.sortedCopy();
            log.debug("Finished copying values.");
        }
    }

    /**
     * Worker thread that copies unsorted elements.
     */
    public static class UnsortedCopyWorker extends Thread {
        private final IndexedSet<String> destination;

        public UnsortedCopyWorker(IndexedSet<String> destination) {
            this.destination = destination;
        }

        @Override
        public void run() {
            log.debug("Copying {} unsorted values.", destination.size());
            destination.unsortedCopy();
            log.debug("Finished copying values.");
        }
    }

    /**
     * Returns the number of nanoseconds that passed from one add and two copy
     * operations.
     *
     * @param source
     *            source data to add to destination set
     * @param destination
     *            destination set (should be thread-safe)
     * @return nanoseconds passed
     */
    public static long time(List<String> source, IndexedSet<String> destination) {
        long time = System.nanoTime();

        Thread adder = new AddWorker(source, destination);
        Thread copy1 = new SortedCopyWorker(destination);
        Thread copy2 = new UnsortedCopyWorker(destination);

        adder.setPriority(Thread.MAX_PRIORITY);
        copy1.setPriority(Thread.NORM_PRIORITY);
        copy2.setPriority(Thread.NORM_PRIORITY);

        adder.start();
        copy1.start();
        copy2.start();

        try {
            adder.join();
            copy1.join();
            copy2.join();
        }
        catch (InterruptedException e) {
            log.debug(e.getMessage(), e);
        }

        time = System.nanoTime() - time;

        return time;
    }

    /**
     * Roughly demonstrates runtime different between using synchronized and a
     * read/write lock when there are more than one large read operations. Note:
     * not an accurate benchmark, but you get the idea.
     *
     * @param args
     *            unused
     */
    public static void main(String[] args) {
        List<String> data = generateData(1000);

        SynchronizedSet<String> set1 = new SynchronizedSet<>();
        ConcurrentSet<String> set2 = new ConcurrentSet<>();

        System.out.println(time(data, set1) + " nanoseconds");
        System.out.println(time(data, set2) + " nanoseconds");
    }
}
