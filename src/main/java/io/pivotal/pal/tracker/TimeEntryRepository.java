package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;

public interface TimeEntryRepository {
    public TimeEntry create(TimeEntry timeEntry);
    public TimeEntry find (long timeEntryId) throws SQLException;

    public List<TimeEntry> list();

    public TimeEntry update(long eq, TimeEntry any);
    public void delete(long timeEntryId);
}
