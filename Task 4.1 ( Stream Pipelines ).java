public class Main {
  
//** 4.1a: Top N most-borrowed books */ 
public List<Book> topBorrowedBooks(int n) {
    return loans.stream()
        .collect(Collectors.groupingBy(Loan::getBook, Collectors.counting()))
        .entrySet().stream()
        .sorted(Map.Entry.<Book, Long>comparingByValue().reversed())
        .limit(n)
        .map(Map.Entry::getKey)
        .toList();
}

//** 4.1b: Members with overdue loans */ 
public List<Member> overdueMembers() {
    LocalDate today = LocalDate.now();
    return loans.stream()
        .filter(l -> l.getDueDate().isBefore(today) && !l.isReturned())
        .map(Loan::getMember)
        .distinct()
        .toList();
}

//** 4.1c: Average fine per membership type */  
public Map<MembershipType, Double> avgFineByMembership() {
    return loans.stream()
        .collect(Collectors.groupingBy(
            l -> l.getMember().getMembershipType(),
            Collectors.averagingDouble(Loan::getFine)
        ));
}
//** 4.1d: Sorted unique authors */  
public List<String> sortedAuthors() {
    return books.stream()
        .flatMap(b -> b.getAuthors().stream()) // if multiple authors per book
        .distinct()
        .sorted()
        .toList();
}
//** 4.1e: Partition books by availability */   
public Map<Boolean, List<Book>> partitionByAvailability() {
    return books.stream()
        .collect(Collectors.partitioningBy(Book::isAvailable));
}
//** 4.1f: ISBN list for a given author */   
public Optional<String> isbnListForAuthor(String author) {
    return Optional.ofNullable(
        books.stream()
            .filter(b -> b.getAuthors().contains(author))
            .map(Book::getIsbn)
            .collect(Collectors.joining(","))
    ).filter(s -> !s.isEmpty());
}
//** 4.1g: Loan count per month for a given year */    
public Map<Integer, Long> loansPerMonth(int year) {
    return loans.stream()
        .filter(l -> l.getLoanDate().getYear() == year)
        .collect(Collectors.groupingBy(
            l -> l.getLoanDate().getMonthValue(),
            TreeMap::new, // ensures sorted by month
            Collectors.counting()
        ));
}
    
}
