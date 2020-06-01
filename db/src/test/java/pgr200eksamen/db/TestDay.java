package pgr200eksamen.db;

import org.junit.Test;
import pgr200eksamen.db.entities.Day;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDay extends EntityCreator {
    DayDao dao = new DayDao(createDataSource());

    @Test
    public void shouldSaveAndSetId() throws SQLException {
        Day d = newDay();
        dao.insert(d);
        assertThat(d.Id()).isNotNull();
    }

    @Test
    public void shouldFindById() throws SQLException {
        Day d = newDay();
        dao.insert(d);
        assertThat(dao.selectSingle(d.Id())).isEqualTo(d);
    }

    @Test
    public void shouldFindAll() throws SQLException {
        List<Day> days = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Day d = newDay();
            dao.insert(d);
            days.add(d);
        }
        assertThat(dao.selectAll()).containsAll(days);
    }

    @Test
    public void shouldDeleteOne() throws SQLException {
        Day d = newDay();
        dao.insert(d);
        assertThat(dao.selectAll()).contains(d);
        dao.removeSingle(d.Id());
        assertThat(dao.selectAll()).doesNotContain(d);
    }

    @Test
    public void shouldDeleteAll() throws SQLException {
        List<Day> days = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Day d = newDay();
            dao.insert(d);
            days.add(d);
        }
        dao.removeAll();
        assertThat(dao.selectAll()).doesNotContainAnyElementsOf(days);
    }
}
