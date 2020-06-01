package pgr200eksamen.db;

import pgr200eksamen.db.entities.Timetable;

import javax.sql.*;
import java.sql.*;
import java.util.*;

public class TimetableDao extends DAO<Timetable> {
    public TimetableDao(DataSource source) {
        super(source);
        super.table = "Timetable";
        super.idName = "TimetableId";
        super.columns = "(DayId, TimeEntryId, TrackId)";
        super.values = "(?, ?, ?)";
    }

    @Override
    void executeSave(Timetable entity, PreparedStatement stmt) throws SQLException {
        List<Timetable> entities = selectAll();

        if (!entities.contains(entity))
        {
            stmt.setInt(1, entity.DayId());
            stmt.setInt(2, entity.EntryId());
            stmt.setInt(3, entity.TrackId());

            stmt.executeUpdate();

            ResultSet result = stmt.getGeneratedKeys();
            result.next();

            entity.setEntryId(result.getInt(idName));

            result.close();
            stmt.close();
        }
        else
        {
            for (Timetable table : entities)
            {
                if (entity.equals(table))
                {
                    entity.setId(table.Id());
                }
            }
        }
    }

    public List<String> getTimetable() throws SQLException
    {
        Connection conn = Connection();
        Statement stmt = conn.createStatement();

        ResultSet result = stmt.executeQuery("SELECT Discussion.Title, Discussion.Description, Discussion.Topic, TimeEntry.StartTime, TimeEntry.EndTime, Track.TrackName, TimetableDay.DayDate " +
                "FROM Timetable JOIN TimeEntry INNER JOIN Discussion " +
                "ON Discussion.Time = TimeEntry.EntryId " +
                "ON Timetable.TimeEntryId = TimeEntry.EntryId " +
                "JOIN TimetableDay ON Timetable.DayId = TimetableDay.DayId " +
                "JOIN Track ON Timetable.TrackId = Track.TrackId " +
                "ORDER BY TimetableDay.DayDate, TimeEntry.StartTime ASC;");

        List<String> timetable = new ArrayList<String>();

        while (result.next())
        {
            StringBuilder builder = new StringBuilder();
            builder.append("Title: " + result.getString(1) + ", "); // Title
            builder.append("Decription: " + result.getString(2) + ", "); // Description
            builder.append("Topic: " + result.getString(3) + ", "); // Topic
            builder.append("Starts: " + result.getString(4) + ", "); // Start Time
            builder.append("Ends: " + result.getString(5) + ", "); // End Time
            builder.append("Track: " + result.getString(6) + ", "); // Track
            builder.append("Date: " + result.getString(7) + ", "); // Date
            builder.append("\n");
            timetable.add(builder.toString());
        }

        result.close();
        stmt.close();
        conn.close();

        return timetable;
    }

    @Override
    Timetable getEntity(ResultSet result) throws SQLException {
        Timetable table = new Timetable();
        table.setEntryId(result.getInt(idName));
        table.setDayId(result.getInt("DayId"));
        table.setTrackId(result.getInt("TimeEntryId"));

        return table;
    }
}
