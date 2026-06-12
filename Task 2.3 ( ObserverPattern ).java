public class Main {

//ai) Completed LiberaryEventBus

public void publish(LibraryEvent event) {
    for (LibraryEventListener listener : listeners) {
        try {
            listener.onEvent(event);
        } catch (Exception e) {
            // Log and continue — don’t stop other listeners
            System.err.println("Listener failed: " + e.getMessage());
        }
    }
}

//aii) All Three Listeners:
// 1)	OverdueFineListener
public class OverdueFineListener implements LibraryEventListener {
    private final LoanService loanService;
    private final FineService fineService;

    public OverdueFineListener(LoanService loanService, FineService fineService) {
        this.loanService = loanService;
        this.fineService = fineService;
    }
    @Override
    public void onEvent(LibraryEvent event) {
        if (event.type() == EventType.BOOK_RETURNED) {
            Loan loan = (Loan) event.payload();
            if (loan.isOverdue()) {
                fineService.imposeFine(loan.getMemberId(), loan.calculateFine());
            }
        }
    }
}


//2)	MemberNotificationListener:

public class MemberNotificationListener implements LibraryEventListener {
    private final NotificationService notificationService;

    public MemberNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @Override
    public void onEvent(LibraryEvent event) {
        switch (event.type()) {
            case RESERVATION_EXPIRED -> {
                Reservation res = (Reservation) event.payload();
                notificationService.notify(res.getMemberId(),
                    "Your reservation has expired.");
            }
            case FINE_IMPOSED -> {
                Fine fine = (Fine) event.payload();
                notificationService.notify(fine.getMemberId(),
                    "A fine has been imposed: $" + fine.getAmount());
            }
        }
    }
}




//3)	AuditLogListener

public class AuditLogListener implements LibraryEventListener {
    private final AuditService auditService;

    public AuditLogListener(AuditService auditService) {
        this.auditService = auditService;
    }
    @Override
    public void onEvent(LibraryEvent event) {
        auditService.appendLog(event.type() + " at " + event.occurredAt()
            + " payload=" + event.payload());
    }
}
    
}
