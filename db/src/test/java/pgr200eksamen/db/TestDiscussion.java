package pgr200eksamen.db;

import org.junit.Test;
import pgr200eksamen.db.entities.Discussion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDiscussion extends EntityCreator {
    DiscussionDao dao = new DiscussionDao(createDataSource());

    @Test
    public void shouldSaveAndSetId() throws SQLException {
        Discussion t = newDiscussion();
        //t.setTimeSlot(createTimeSlot());
        //t.setTime(newTime());
        dao.insert(t);
        assertThat(t.Id()).isNotZero();
        dao.removeAll();
    }

    @Test
    public void shouldFindById() throws SQLException {
        Discussion t = newDiscussion();
        t.setTime(newTime());
        dao.insert(t);
        assertThat(dao.selectSingle(t.Id())).isEqualTo(t);
        dao.removeAll();
    }

    @Test
    public void shouldFindAll() throws SQLException {
        List<Discussion> Discussions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Discussion t = newDiscussion();
            t.setTime(newTime());
            dao.insert(t);
            Discussions.add(t);
        }
        assertThat(dao.selectAll()).containsAll(Discussions);
        dao.removeAll();
    }

    @Test
    public void shouldDeleteOne() throws SQLException {
        Discussion t = newDiscussion();
        t.setTime(newTime());
        dao.insert(t);
        assertThat(dao.selectAll()).contains(t);
        dao.removeSingle(t.Id());
        assertThat(dao.selectAll()).doesNotContain(t);
        dao.removeAll();
    }

    @Test
    public void shouldDeleteAll() throws SQLException {
        List<Discussion> Discussions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Discussion t = newDiscussion();
            t.setTime(newTime());
            dao.insert(t);
            Discussions.add(t);
        }
        dao.removeAll();
        assertThat(dao.selectAll()).doesNotContainAnyElementsOf(Discussions);
    }
}
