public class Main {
    
import org. junit. jupiter. api. Test;
import org. mockito. Mockito;
import static org.mockito.Mockito.*;

class LibraryManagerTest {
    //Test
    void testReturnBookSendsEmailAndSavesLoan() {
        FineCalculator fineCalculator = new SimpleFineCalculator();
        EmailService emailService = mock(EmailService.class);
        LoanRepository loanRepository = mock(LoanRepository.class);
       
        LibraryManager manager = new LibraryManager(fineCalculator, emailService, loanRepository);
        
        Member member = new Member();
        member.setEmail("test@example.com");
        manager.returnBook("L123", member, 20);

       

        verify(emailService).sendEmail(eq("test@example.com"), contains("3.0"));
        verify(loanRepository).saveLoan(eq("L123"), eq(3.0));
    }
}

}
