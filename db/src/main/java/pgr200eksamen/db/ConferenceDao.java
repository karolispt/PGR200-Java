package pgr200eksamen.db;

import pgr200eksamen.db.entities.Conference;

import javax.sql.*;
import java.sql.*;
import java.util.*;

public class ConferenceDao extends DAO<Conference> {

    public ConferenceDao(DataSource source) {
        super(source);
        super.table = "Conference";
        super.idName = "ConferenceId";
        super.columns = "(ConferenceName)";
        super.values = "(?)";
    }

    @Override
    void executeSave(Conference conference, PreparedStatement stmt) throws SQLException {
        List<Conference> entities = selectAll();

        if (!entities.contains(conference))
        {
            stmt.setString(1, conference.Title());
            stmt.executeUpdate();

            ResultSet result = stmt.getGeneratedKeys();
            result.next();

            int id = result.getInt(idName);
            conference.setId(id);
            result.close();
        }
        else
        {
            for (Conference eCon : entities)
            {
                if (conference.equals(eCon))
                {
                    conference.setId(eCon.Id());
                }
            }
        }
    }

    @Override
    Conference getEntity(ResultSet result) throws SQLException {
        Conference c = new Conference();

        c.setId(result.getInt(idName));
        c.setTitle(result.getString("ConferenceName"));

        return c;
    }
}
