package it.discovery.monitoring;

import it.discovery.repository.BookRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class BookStorageIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    public BookStorageIndicator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        if(bookRepository.findAll().size() == 0) {
            return Health.down().withDetail("reason", "No books present").build();
        }
        return Health.up().build();
    }
}
