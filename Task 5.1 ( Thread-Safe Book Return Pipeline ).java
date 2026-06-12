public class Main {

public CompletableFuture<ReturnResult> processReturn(String loanID) {
    return CompletableFuture
        .supplyAsync(() -> loanService.returnLoan(loanID), pool)
        .thenApplyAsync(loan -> {
            //** Step 2 + 3 must be atomic */  
            synchronized (loan) {
                loanService.updateInventory(loan);   // update inventory
                loanService.calculateFine(loan);     // calculate fine
            }
            return loan;
        }, pool)
        .thenApplyAsync(loan -> {
            notifService.notifyMember(loan);
            eventBus.publish(EventType.BOOK_RETURNED, loan);
            processedCount.incrementAndGet();
            return new ReturnResult(loan, null);
        }, pool)
        .exceptionally(ex -> {
            failedCount.incrementAndGet();
            eventBus.publish(EventType.RETURN_FAILED, loanID);
            return ReturnResult.failed(loanID, ex);
        });
}

 //** Batch processing  */  
public List<ReturnResult> processBatch(List<String> loanIDs) throws InterruptedException {
    List<CompletableFuture<ReturnResult>> futures = loanIDs.stream()
        .map(this::processReturn)
        .toList();

    CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    all.join(); // wait for all

    return futures.stream()
        .map(CompletableFuture::join)
        .toList();
}
    
}
