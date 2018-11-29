package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter counter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               MeterRegistry registry) {
        this.timeEntryRepository = timeEntryRepository;
        this.counter  = registry.counter("timeEntry.actionCounter");
        this.timeEntrySummary = registry.summary("timeEntry.summary");
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        counter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity(
                timeEntryRepository.create(timeEntry),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/time-entries/{timeEntry}")
    public ResponseEntity<TimeEntry> read(@PathVariable("timeEntry") Long timeEntry) {
        TimeEntry timeEntry1 = null;
        try {
            timeEntry1 = timeEntryRepository.find(timeEntry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (timeEntry1 == null) {
            return new ResponseEntity(
                    HttpStatus.NOT_FOUND
            );
        } else {
            counter.increment();
            return new ResponseEntity(
                    timeEntry1,
                    HttpStatus.OK
            );
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntry1 = timeEntryRepository.list();
        if (timeEntry1 == null) {
            return new ResponseEntity(
                    HttpStatus.NOT_FOUND
            );
        } else {
            counter.increment();
            return new ResponseEntity(
                    timeEntry1,
                    HttpStatus.OK
            );
        }
    }

    @RequestMapping(value = "/time-entries/{timeEntry}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("timeEntry") long timeEntryId,
                                 @RequestBody TimeEntry expected) {
        TimeEntry timeEntry1 = timeEntryRepository.update(timeEntryId, expected);
        if (timeEntry1 == null) {
            return new ResponseEntity(
                    HttpStatus.NOT_FOUND
            );
        } else {
            counter.increment();
            return new ResponseEntity(
                    timeEntry1,
                    HttpStatus.OK
            );
        }
    }

    @RequestMapping(value = "/time-entries/{timeEntry}", method = RequestMethod.DELETE)
    public ResponseEntity<TimeEntry> delete(@PathVariable("timeEntry") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        counter.increment();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
