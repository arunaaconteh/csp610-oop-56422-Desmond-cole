public class Main {
   
public boolean borrow(String isbn, long timeoutMs) throws InterruptedException {
    long nanos = TimeUnit.MILLISECONDS.toNanos(timeoutMs);
    lock.writeLock().lock();
    try {
        while (available.getOrDefault(isbn, 0) == 0) {
            if (nanos <= 0L) return false;
            nanos = copyAvailable.awaitNanos(nanos);
        }
        available.put(isbn, available.getOrDefault(isbn, 0) - 1);
        return true;
    } finally {
        lock.writeLock().unlock();
    }
}

public void returnCopy(String isbn) {
    lock.writeLock().lock();
    try {
        available.put(isbn, available.getOrDefault(isbn, 0) + 1);
        copyAvailable.signal(); // wake one waiting borrower
    } finally {
        lock.writeLock().unlock();
    }
}

public int availableCopies(String isbn) {
    lock.readLock().lock();
    try {
        return available.getOrDefault(isbn, 0);
    } finally {
        lock.readLock().unlock();
    }
}
    
}
