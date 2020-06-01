package pgr200eksamen.db;

import pgr200eksamen.db.entities.TimeEntry;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class TimeEntryDao extends DAO<TimeEntry> {
    public TimeEntryDao(DataSource source) {
        super(source);
        super.table = "TimeEntry";
        super.idName = "TimeEntryId";
        super.columns = "(StartTime, EndTime)";
        super.values = "(?, ?)";
    }

    void applyToTimetable(TimeEntry t) throws SQLException
    {
        DAOTuple data = Statement("INSERT INTO Timetable (TimeEntryId) values (?)");
        PreparedStatement stmt = data.getPreparedStatement();
        stmt.setInt(1, t.Id());
        stmt.executeUpdate();

        data.close();
    }

    @Override
    void executeSave(TimeEntry entity, PreparedStatement stmt) throws SQLException {
        List<TimeEntry> entities = selectAll();

        if (!entities.contains(entity))
        {
            stmt.setTime(1, Time.valueOf(entity.startTime()));
            stmt.setTime(2, Time.valueOf(entity.endTime()));

            stmt.executeUpdate();

            ResultSet result = stmt.getGeneratedKeys();
            result.next();

            entity.setId(result.getInt(idName));

            result.close();
            stmt.close();
        }
        else
        {
            for (TimeEntry table : entities)
            {
                if (entity.equals(table))
                {
                    entity.setId(table.Id());
                }
            }
        }
    }

    @Override
    TimeEntry getEntity(ResultSet result) throws SQLException {
        TimeEntry entry = new TimeEntry();
        entry.setId((result.getInt(idName)));

        entry.setStartTime(result.getTime("StartTime").toLocalTime());
        entry.setStartTime(result.getTime("EndTime").toLocalTime());

        return entry;
    }
}
