package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {
    private TimeEntryRepository repo;

    public CustomHealthIndicator(TimeEntryRepository timeEntryRepository) {
        this.repo = timeEntryRepository;
    }

    @Override
    public Health health() {
        if (repo.list().size() < 5) {
            return new Health.Builder().up().build();
        }
        return new Health.Builder().down().build();
    }
}
