public class Main {
   
package smartlib.generics;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A type-safe, sorted catalogue of Borrowable items.
 *
 * @param <T> the type of item, must be Borrowable and Comparable
 */ 
public class Catalogue<T extends Borrowable & Comparable<T>> {

    private final List<T> items = new ArrayList<>();
    public void add(T item) { items.add(item); }
    public void remove(T item) { items.remove(item); }

    // Return all items matching the given predicate. 
    public List<T> search(Predicate<T> predicate) {
        return items.stream()
                .filter(predicate)
                .toList();
    }

    
    // Return items sorted by the given key extractor.
    // Producer Extends rule applies: the key type K must be Comparable.
     
    public <K extends Comparable<K>> List<T> sortedBy(
            Function<? super T, ? extends K> keyExtractor) {
        return items.stream()
                .sorted(Comparator.comparing(keyExtractor))
                .toList();
    }
    /**
     * Copy all available items from another catalogue into this one.
     * Uses an upper-bounded wildcard: the source may hold any subtype of T.
     */
    public void addAllAvailable(Catalogue<? extends T> source) {
        source.items.stream()
                .filter(Borrowable::isAvailable)
                .forEach(this::add);
    }
    
     // Batch-update a field for all items matching a predicate.
    // Uses a lower-bounded wildcard on the consumer.
     
    public void updateMatching(Predicate<T> predicate,
                               Consumer<? super T> updater) {
        items.stream()
                .filter(predicate)
                .forEach(updater);
    }
     //Return usage statistics grouped by a classifier function. 
    public <K> Map<K, Long> groupCount(
            Function<? super T, ? extends K> classifier) {
        return items.stream()
                .collect(Collectors.groupingBy(classifier, Collectors.counting()));
    }
}
    
}
