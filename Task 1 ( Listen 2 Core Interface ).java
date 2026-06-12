package smartlib.domain;
import java.time.LocalDate;

// Immutable entities
public record Book(String id, String title, int availableCopies) implements Borrowable {
    @Override
    public boolean isAvailable() { return availableCopies > 0; }
}
public record Member(String id, String name, String email, BorrowingPolicy policy) implements Notifiable {
    @Override
    public String getContactAddress() { return email; }
   
 @Override
    public NotificationChannel preferredChannel() { return NotificationChannel.EMAIL; }
}
public record Fine(String loanId, double amount) {}
public record Notification(String message, String recipient, NotificationChannel channel) {}

// Mutable entities with lifecycle
public class Loan {
    private final String id;
    private final Member member;
    private final Book book;
    private LocalDate startDate;
    private LocalDate returnDate;
    private Fine fine;

    private Loan(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.book = builder.book;
        this.startDate = builder.startDate;
    }
    public static class Builder {
        private String id;
        private Member member;
        private Book book;
        private LocalDate startDate;


        public Builder id(String id) { this.id = id; return this; }
        public Builder member(Member member) { this.member = member; return this; }
        public Builder book(Book book) { this.book = book; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Loan build() { return new Loan(this); }
    }
}
public class Reservation {
    private final String id;
    private final Member member;
    private final Book book;
    private LocalDate expiryDate;
    // builder pattern similar to Loan
}

//b) Borrowing Policies
public class StandardPolicy implements BorrowingPolicy {
    public int maxBorrowLimit() { return 5; }
    public int loanDurationDays() { return 14; }
    public double dailyFineRate() { return 0.50; }
}
public class PremiumPolicy implements BorrowingPolicy {
    public int maxBorrowLimit() { return 10; }
    public int loanDurationDays() { return 28; }
    public double dailyFineRate() { return 0.25; }
}


    public class StudentPolicy implements BorrowingPolicy {
    public int maxBorrowLimit() { return 3; }
    public int loanDurationDays() { return 21; }
    public double dailyFineRate() { return 0.10; }
}

//C) In-Memory Repository Implementations 
import java.util.*;

public class InMemoryRepository<T, ID> implements Repository<T, ID> {
    private final Map<ID, T> store = new HashMap<>();

    @Override
    public void save(T entity) { 
        // assume entity has getId() via reflection or interface
    }
    @Override
    public Optional<T> findById(ID id) { return Optional.ofNullable(store.get(id)); }
    @Override
    public List<T> findAll() { return new ArrayList<>(store.values()); }
    @Override
    public void delete(ID id) { store.remove(id); }
}





