package pgr200eksamen.db;

import pgr200eksamen.db.entities.Discussion;
import pgr200eksamen.db.entities.TimeEntry;

import javax.sql.*;
import java.sql.*;
import java.time.LocalTime;

public class DiscussionDao extends DAO<Discussion> {
    public DiscussionDao(DataSource source) {
        super(source);
        super.table = "Discussion";
        super.idName = "DiscussionId";
        super.columns = "(Title, Description, Topic, TimeEntryId)";
        super.values = "(?, ?, ?, ?)";
    }



    @Override
    void executeSave(Discussion entity, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.Title()); // Title
        stmt.setString(2, entity.Description()); // Description
        stmt.setString(3, entity.Topic()); // Topic

        DataSource source = DataSource();

        if (entity.Time()  != null)
        {
            TimeEntry time = entity.Time();

            if (entity.Time().Id() != 0)
            {
                stmt.setInt(4, entity.Time().Id());
                stmt.executeUpdate();
            }
            else
            {
                TimeEntryDao timeEntryDao = new TimeEntryDao(source);
                timeEntryDao.insert(entity.Time());

                stmt.setInt(4, time.Id());
                stmt.executeUpdate();
            }
        }
        else
        {
            LocalTime now = LocalTime.MIDNIGHT;
            TimeEntry time = new TimeEntry(now, now);

            TimeEntryDao timeEntryDao = new TimeEntryDao(source);
            timeEntryDao.insert(time);

            entity.setTime(time);
            stmt.setInt(4, time.Id());
            stmt.executeUpdate();
        }

        if (entity.Id() == 0)
        {
            ResultSet result = stmt.getGeneratedKeys();
            result.next();

            entity.setId(result.getInt(idName));
        }

    }

    @Override
    Discussion getEntity(ResultSet result) throws SQLException {
        Discussion discussion = new Discussion();
        discussion.setId(result.getInt(idName));
        discussion.setTitle(result.getString("Title"));
        discussion.setDescription(result.getString("Description"));
        discussion.setTopic(result.getString("Topic"));

        DataSource source = DataSource();
        TimeEntryDao timeEntryDao = new TimeEntryDao(source);
        TimeEntry time = timeEntryDao.selectSingle(result.getInt("TimeEntryId"));

        discussion.setTime(time);

        return discussion;
    }
}
