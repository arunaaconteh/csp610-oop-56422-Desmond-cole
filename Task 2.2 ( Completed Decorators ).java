public class Main {
  
package smartlib.patterns.structural;

import smartlib.domain.Notifiable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LoggingDecorator extends NotificationDecorator {
    public LoggingDecorator(NotificationService wrapped) {
        super(wrapped);
    }

    @Override
    public void send(Notifiable recipient, String message) {
        System.out.printf("[%s] Sending to %s: %s%n",
                LocalDateTime.now(), recipient.getContactAddress(), message);
        super.send(recipient, message);
    }
}

public class RetryDecorator extends NotificationDecorator {
    private final int maxAttempts;

    public RetryDecorator(NotificationService wrapped, int maxAttempts) {
        super(wrapped);
        this.maxAttempts = maxAttempts;
    }

    @Override
    public void send(Notifiable recipient, String message) {
        int attempt = 0;
        long delay = 100; // initial back-off in ms
        while (attempt < maxAttempts) {
            try {
                super.send(recipient, message);
                return; // success
            } catch (Exception e) {
                attempt++;
                if (attempt >= maxAttempts) {
                    System.err.println("Failed after " + maxAttempts + " attempts");
                    throw e;
                }
                try { Thread.sleep(delay); } catch (InterruptedException ignored) {}
                delay *= 2; // exponential back-off
            }
        }
    }
}
public class RateLimitingDecorator extends NotificationDecorator {
    private final int maxPerMinute;
    private final Map<String, List<Long>> recipientTimestamps = new ConcurrentHashMap<>();

    public RateLimitingDecorator(NotificationService wrapped, int maxPerMinute) {
        super(wrapped);
        this.maxPerMinute = maxPerMinute;
    }
    @Override
    public void send(Notifiable recipient, String message) {
        String key = recipient.getContactAddress();
        long now = System.currentTimeMillis();
        recipientTimestamps.putIfAbsent(key, new ArrayList<>());
        List<Long> timestamps = recipientTimestamps.get(key);
       
      // remove timestamps older than 1 minute
        timestamps.removeIf(ts -> now - ts > 60_000);
        if (timestamps.size() >= maxPerMinute) {
            System.err.printf("Rate limit exceeded for %s, dropping message: %s%n", key, message);
            return;
        }
        timestamps.add(now);
        super.send(recipient, message);
    }
}
b) Composition of Email Notification Service:
NotificationService service =
    new RateLimitingDecorator(
        new RetryDecorator(
            new LoggingDecorator(
                new EmailNotificationService()
            ), 3
        ), 5
    );
service.send(member, "Your loan is overdue!");

}
