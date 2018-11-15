package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

            return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        List<TimeEntry> all = list();
        if(all == null){
            return null;
        }
        for (TimeEntry timeEntry : all){
            if (timeEntry.getId() == timeEntryId){
                return timeEntry;
            }
        }
        return null;
    }

    @Override
    public List<TimeEntry> list() {
        TimeEntryMapper mapper = new TimeEntryMapper();
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement find = connection.prepareStatement("SELECT * FROM time_entries");
            ResultSet resultSet = find.executeQuery();
            List<TimeEntry> entries = new ArrayList<>();

            while (resultSet.next()) {
                entries.add(mapper.mapRow(resultSet, 1));
            }
            return entries;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {
        try {
            jdbcTemplate.update(
                    "UPDATE time_entries SET project_id = ?, user_id = ?, hours = ?, date = ? WHERE id = ?",
             any.getProjectId(),
             any.getUserId(),
             any.getHours(),
             Date.valueOf(any.getDate()),
             eq);
            return find(eq);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public void delete(long timeEntryId) {
        TimeEntryMapper mapper = new TimeEntryMapper();
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement delete = connection.prepareStatement("DELETE FROM time_entries WHERE ID = ?");
            delete.setLong(1, timeEntryId);
            delete.executeUpdate();
        } catch (Exception e) {
        }
    }
}
