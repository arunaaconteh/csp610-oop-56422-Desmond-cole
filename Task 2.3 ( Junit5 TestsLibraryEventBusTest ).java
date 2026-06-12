public class Main {

    class LibraryEventBusTest {
    private LibraryEventBus bus;

    @BeforeEach
    void setup() {
        bus = new LibraryEventBus();
    }
    @Test
    void listenerReceivesCorrectPayload() {
        AtomicReference<LibraryEvent> received = new AtomicReference<>();
        bus.subscribe(received::set);

        LibraryEvent event = new LibraryEvent(EventType.BOOK_RETURNED, "Book123", Instant.now());
        bus.publish(event);

        assertEquals("Book123", received.get().payload());
    }
    @Test
    void unsubscribedListenerDoesNotReceiveEvents() {
        AtomicBoolean called = new AtomicBoolean(false);
        LibraryEventListener listener = e -> called.set(true);

        bus.subscribe(listener);
        bus.unsubscribe(listener);

        bus.publish(new LibraryEvent(EventType.BOOK_RETURNED, "Book123", Instant.now()));
        assertFalse(called.get());
    }
    @Test
    void exceptionInOneListenerDoesNotStopOthers() {
        AtomicBoolean secondCalled = new AtomicBoolean(false);

        bus.subscribe(e -> { throw new RuntimeException("Boom"); });
        bus.subscribe(e -> secondCalled.set(true));

        bus.publish(new LibraryEvent(EventType.BOOK_RETURNED, "Book123", Instant.now()));
        assertTrue(secondCalled.get());
    }
}

}
