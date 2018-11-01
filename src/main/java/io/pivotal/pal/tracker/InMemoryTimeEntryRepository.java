package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Long count;
    private HashMap<Long, TimeEntry> repository;

    public InMemoryTimeEntryRepository() {
        this.count = 1L;
        this.repository = new HashMap<>();
    }

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(count);
        repository.put(count, timeEntry);
        count++;
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return repository.get(id);
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntry.setId(id);
        repository.replace(id, timeEntry);
        return timeEntry;
    }

    public List<TimeEntry> list() {
        return new ArrayList(repository.values());
    }

    public void delete(long id) {
        repository.remove(id);

    }
}
