import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;

/**
 * A thread-safe version of {@link IndexedSet} using the synchronized keyword.
 *
 * @param <E> element type
 * @see IndexedSet
 */
public class SynchronizedSet<E> extends IndexedSet<E> {

    public SynchronizedSet() {
        this(false);
    }

    public SynchronizedSet(boolean sorted) {
        super(sorted);
    }

    @Override
    public boolean add(E element) {
        synchronized (this) {
            return super.add(element);
        }
    }

    @Override
    public boolean addAll(Collection<E> elements) {
        synchronized (this) {
            return super.addAll(elements);
        }
    }

    @Override
    public int size() {
        synchronized (this) {
            return super.size();
        }
    }

    @Override
    public boolean contains(E element) {
        synchronized (this) {
            return super.contains(element);
        }
    }

    @Override
    public E get(int index) {
        synchronized (this) {
            return super.get(index);
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            return super.toString();
        }
    }

    @Override
    public synchronized Set<E> unsortedCopy() {
        synchronized (this) {
            return super.unsortedCopy();
        }
    }

    @Override
    public synchronized SortedSet<E> sortedCopy() {
        synchronized (this) {
            return super.sortedCopy();
        }
    }
}
