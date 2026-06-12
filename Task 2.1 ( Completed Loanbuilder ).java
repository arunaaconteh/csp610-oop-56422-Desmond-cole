public class Main {
 
package smartlib.patterns.creational;

import smartlib.domain.Book;
import smartlib.domain.Member;
import smartlib.domain.Loan;

import java.time.LocalDate;
import java.util.Objects;

public final class LoanBuilder {

    // Mandatory
    private final Member member;
    private final Book book;

    // Optional
    private String notes = "";
    private int renewalCount = 0;
    private String referenceCode = null;

    public LoanBuilder(Member member, Book book) {
        this.member = Objects.requireNonNull(member);
        this.book = Objects.requireNonNull(book);
    }

    public LoanBuilder notes(String notes) {
        this.notes = notes;
        return this;
    }

    public LoanBuilder renewalCount(int count) {
        if (count < 0) throw new IllegalArgumentException("Renewal count cannot be negative");
        this.renewalCount = count;
        return this;
    }

    public LoanBuilder referenceCode(String code) {
        this.referenceCode = code;
        return this;
    }

    public Loan build() {
        return new Loan.Builder()
                .id("LN-" + System.nanoTime()) // auto-generate ID
                .member(member)
                .book(book)
                .startDate(LocalDate.now())
                .notes(notes)
                .renewalCount(renewalCount)
                .referenceCode(referenceCode)
                .build();
    }
}

}
