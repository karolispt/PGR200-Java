package pgr200eksamen.db;

import org.junit.Test;
import pgr200eksamen.db.entities.Track;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTrack extends EntityCreator {
    TrackDao dao = new TrackDao(createDataSource());

    @Test
    public void shouldSaveAndSetId() throws SQLException {
        Track t = newTrack();
        dao.insert(t);
        assertThat(t.Id()).isNotNull();
    }

    @Test
    public void shouldFindById() throws SQLException {
        Track t = newTrack();
        dao.insert(t);
        assertThat(dao.selectSingle(t.Id())).isEqualTo(t);
    }

    @Test
    public void shouldFindAll() throws SQLException {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Track t = newTrack();
            dao.insert(t);
            tracks.add(t);
        }
        assertThat(dao.selectAll()).containsAll(tracks);
    }

    @Test
    public void shouldDeleteOne() throws SQLException {
        Track t = newTrack();
        dao.insert(t);
        assertThat(dao.selectAll()).contains(t);
        dao.removeSingle(t.Id());
        assertThat(dao.selectAll()).doesNotContain(t);
    }

    @Test
    public void shouldDeleteAll() throws SQLException {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Track t = newTrack();
            dao.insert(t);
            tracks.add(t);
        }
        dao.removeAll();
        assertThat(dao.selectAll()).doesNotContainAnyElementsOf(tracks);
    }
}
