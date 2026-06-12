public class Main {

public class LoanSummaryCollector
    implements Collector<Loan, LoanSummaryCollector.Accumulator, LoanSummary> {

    public static class Accumulator {
        int count = 0;
        double totalFines = 0.0;
        LocalDate latestDate = LocalDate.MIN;
        Map<MembershipType, Long> breakdown = new EnumMap<>(MembershipType.class);
    }
    @Override
    public Supplier<Accumulator> supplier() {
        return Accumulator::new;
    }
    @Override
    public BiConsumer<Accumulator, Loan> accumulator() {
        return (acc, loan) -> {
            acc.count++;
            acc.totalFines += loan.getFine();
            acc.latestDate = acc.latestDate.isBefore(loan.getLoanDate())
                ? loan.getLoanDate() : acc.latestDate;
            acc.breakdown.merge(loan.getMember().getMembershipType(), 1L, Long::sum);
        };
    }
    @Override
    public BinaryOperator<Accumulator> combiner() {
        return (a1, a2) -> {
            a1.count += a2.count;
            a1.totalFines += a2.totalFines;
            a1.latestDate = a1.latestDate.isBefore(a2.latestDate) ? a2.latestDate : a1.latestDate;
            a2.breakdown.forEach((k,v) -> a1.breakdown.merge(k,v,Long::sum));
            return a1;
        };
    }

    @Override
    public Function<Accumulator, LoanSummary> finisher() {
        return acc -> new LoanSummary(acc.count, acc.totalFines, acc.latestDate, acc.breakdown);
    }
    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}
    
}
