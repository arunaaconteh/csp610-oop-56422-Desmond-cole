public class Main {
    
package smartlib.modern;

import smartlib.domain.*; //** adjust imports for Loan, Book, Member, Reservation, etc. */ 
import smartlib.events.EventBus;
import smartlib.events.LibraryEvent;

public class LoanResultHandler {

    private final EventBus eventBus;

    public LoanResultHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void handle(LoanResult result) {
        switch (result) {
            case LoanResult.Success s -> {
                System.out.println("Loan successful: " + s.loan());
                eventBus.publish(new LibraryEvent("BOOK_BORROWED", s.loan()));
            }
            case LoanResult.InsufficientCopies ic -> {
                System.out.printf("No copies available for %s. Requested: %d, Available: %d%n",
                        ic.book().title(), ic.requested(), ic.available());
                Reservation reservation = new Reservation(ic.book(), ic.requested());
                notifyMember(ic.book(), reservation);
            }
            case LoanResult.MemberSuspended ms -> {
                System.err.println("Member suspended: " + ms.member().name() +
                        " Reason: " + ms.reason());
            }
            case LoanResult.FineExceeded fe -> {
                System.out.printf("Member %s has outstanding fine %.2f (threshold %.2f)%n",
                        fe.member().name(), fe.outstandingFine(), fe.threshold());
                sendPaymentReminder(fe.member(), fe.outstandingFine());
            }
        }
    }

    private void notifyMember(Book book, Reservation reservation) {
        //** Example notification logic */ 
        System.out.println("Reservation created for book: " + book.title());
        // Could integrate with MemberNotificationService
    }

    private void sendPaymentReminder(Member member, double fine) {
        //** Example reminder logic */ 
        System.out.println("Reminder sent to " + member.email() +
                " for outstanding fine: " + fine);
    }
}

}
