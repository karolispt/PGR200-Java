package pgr200eksamen.db;

import pgr200eksamen.db.entities.Track;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class TrackDao extends DAO<Track> {
    public TrackDao(DataSource source) {
        super(source);
        super.table = "Track";
        super.idName ="TrackId";
        super.columns = "(TrackName)";
        super.values = "(?)";
    }

    @Override
    void executeSave(Track track, PreparedStatement stmt) throws SQLException {
        List<Track> entities = selectAll();

        if (!entities.contains(track))
        {
            stmt.setString(1, track.Name());
            stmt.executeUpdate();

            ResultSet result = stmt.getGeneratedKeys();
            result.next();

            int id = result.getInt(idName);
            track.setId(id);

            result.close();
        }
        else
        {
            for (Track eTrack : entities)
            {
                if (track.equals(eTrack))
                {
                    track.setId(eTrack.Id());
                }
            }
        }
    }

    @Override
    Track getEntity(ResultSet result) throws SQLException {
        Track track = new Track();
        track.setId(result.getInt(idName));
        track.setName(result.getString("TrackName"));

        return track;
    }
}
