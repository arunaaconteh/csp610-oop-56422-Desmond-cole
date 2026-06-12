// Domain classes
public class Book {
    private String id;
    private String title;
    // getters/setters
}
public class Member {
    private String id;
    private String email;
    // getters/setters
}
// Abstractions
public interface FineCalculator {
    double calculateFine(long days);
}
public interface EmailService {
    void sendEmail(String recipient, String message);
}
public interface LoanRepository {
    void saveLoan(String loanId, double fine);
}
public interface ReportGenerator {
    void generateReport();
}
// Implementations
public class SimpleFineCalculator implements FineCalculator {
    @Override
    public double calculateFine(long days) {
        return days > 14 ? (days - 14) * 0.50 : 0;
    }
}
public class SmtpEmailService implements EmailService {
    @Override
    public void sendEmail(String recipient, String message) {
        // JavaMail logic here
        System.out.println("Email sent to " + recipient);
    }
}
public class CsvLoanRepository implements LoanRepository {
    @Override
    public void saveLoan(String loanId, double fine) {
        try (FileWriter fw = new FileWriter("loans.csv", true)) {
            fw.write(loanId + ",RETURNED," + fine + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
// Report generators
public class PdfReportGenerator implements ReportGenerator {
    @Override
    public void generateReport() {
        // iText logic
    }
}
public class CsvReportGenerator implements ReportGenerator {
    @Override
    public void generateReport() {
        // CSV logic
    }
}
public class ConsoleReportGenerator implements ReportGenerator {
    @Override
    public void generateReport() {
        System.out.println("Console report generated");
    }
}
// High-level LibraryManager
public class LibraryManager {
    private final FineCalculator fineCalculator;
    private final EmailService emailService;
    private final LoanRepository loanRepository;
    public LibraryManager(FineCalculator fineCalculator,
                          EmailService emailService,
                          LoanRepository loanRepository) {
        this.fineCalculator = fineCalculator;
        this.emailService = emailService;
        this.loanRepository = loanRepository;
    }
    public void returnBook(String loanId, Member member, long days) {
        double fine = fineCalculator.calculateFine(days);
        emailService.sendEmail(member.getEmail(), "Your fine is: " + fine);
        loanRepository.saveLoan(loanId, fine);
    }
} 

