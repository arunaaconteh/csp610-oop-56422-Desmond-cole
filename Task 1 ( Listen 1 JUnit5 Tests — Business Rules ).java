public class Main {
    
    //Section 2.2 rules (borrowing limits, loan durations, fines, reservation expiry, unpaid fines restrictions) can be tested like this: 
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BorrowingPolicyTest {
    @Test
    void standardPolicyLimits() {
        BorrowingPolicy policy = new StandardPolicy();
        assertEquals(5, policy.maxBorrowLimit());
        assertEquals(14, policy.loanDurationDays());
        assertEquals(0.50, policy.dailyFineRate());
    }
    @Test
    void fineCalculation() {
        BorrowingPolicy policy = new StandardPolicy();
        long overdueDays = 5;
        double fine = overdueDays * policy.dailyFineRate();
        assertEquals(2.5, fine);
    }
    @Test
    void reservationExpires() {
        Reservation reservation = new Reservation(/* builder with expiry */);
        assertTrue(reservation.getExpiryDate().isBefore(LocalDate.now().plusDays(7)));
    }
}
}
