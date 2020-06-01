package pgr200eksamen.db;

import org.junit.Test;
import pgr200eksamen.db.entities.Conference;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestConference extends EntityCreator {
    ConferenceDao dao = new ConferenceDao(createDataSource());

    @Test
    public void shouldSaveAndSetId() throws SQLException {
        Conference c = newConference();
        dao.insert(c);
        assertThat(c.Id()).isNotNull();
    }

    @Test
    public void shouldFindById() throws SQLException {
        Conference c = newConference();
        dao.insert(c);
        assertThat(dao.selectSingle(c.Id())).isEqualTo(c);
    }

    @Test
    public void shouldFindAll() throws SQLException {
        List<Conference> conferences = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Conference c = newConference();
            dao.insert(c);
            conferences.add(c);
        }
        assertThat(dao.selectAll()).containsAll(conferences);
    }

    @Test
    public void shouldDeleteOne() throws SQLException {
        Conference c = newConference();
        dao.insert(c);
        assertThat(dao.selectAll()).contains(c);
        dao.removeSingle(c.Id());
        assertThat(dao.selectAll()).doesNotContain(c);
    }

    @Test
    public void shouldDeleteAll() throws SQLException {
        List<Conference> conferences = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Conference c = newConference();
            dao.insert(c);
            conferences.add(c);
        }
        dao.removeAll();
        assertThat(dao.selectAll()).doesNotContainAnyElementsOf(conferences);
    }
}
