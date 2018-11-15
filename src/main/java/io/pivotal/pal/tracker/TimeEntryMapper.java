package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TimeEntryMapper implements RowMapper<TimeEntry> {

    @Override
    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TimeEntry(
                rs.getLong("ID"),
                rs.getLong("PROJECT_ID"),
                rs.getLong("USER_ID"),
                LocalDate.parse(rs.getString("DATE")),
                rs.getInt("HOURS")
        );
    }
}
