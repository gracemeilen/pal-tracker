package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;
    private final CounterService counterService;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               CounterService counterService) {
        this.timeEntryRepository = timeEntryRepository;
        this.counterService  = counterService;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        counterService.increment("timeEntries.actionCounter");
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
            counterService.increment("timeEntries.actionCounter");
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
            counterService.increment("timeEntries.actionCounter");
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
            counterService.increment("timeEntries.actionCounter");
            return new ResponseEntity(
                    timeEntry1,
                    HttpStatus.OK
            );
        }
    }

    @RequestMapping(value = "/time-entries/{timeEntry}", method = RequestMethod.DELETE)
    public ResponseEntity<TimeEntry> delete(@PathVariable("timeEntry") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        counterService.increment("timeEntries.actionCounter");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
