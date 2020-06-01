package pgr200eksamen.db;

import org.junit.Test;
import pgr200eksamen.db.entities.TimeEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTimeEntry extends EntityCreator {
    private TimeEntryDao dao = new TimeEntryDao(createDataSource());

    @Test
    public void shouldSaveAndSetId() throws SQLException {
        TimeEntry t = newTime();
        dao.insert(t);
        assertThat(t.Id()).isNotNull();
    }

//    @Test
//    public void shouldFindById() throws SQLException {
//        TimeEntry t = newTime();
//        dao.insert(t);
//        assertThat(dao.selectSingle(t.Id())).isEqualTo(t);
//    }
//
//
//    @Test
//    public void shouldFindAll() throws SQLException {
//        List<TimeEntry> TimeEntrys = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            TimeEntry t = newTime();
//            dao.insert(t);
//            TimeEntrys.add(t);
//        }
//        assertThat(dao.selectAll()).containsAll(TimeEntrys);
//    }
//
//    @Test
//    public void shouldDeleteOne() throws SQLException {
//        TimeEntry t = newTime();
//        dao.insert(t);
//        assertThat(dao.selectAll()).contains(t);
//        dao.removeSingle(t.Id());
//        assertThat(dao.selectAll()).doesNotContain(t);
//    }

    @Test
    public void shouldDeleteAll() throws SQLException {
        List<TimeEntry> entries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TimeEntry t = newTime();
            dao.insert(t);
            entries.add(t);
        }
        dao.removeAll();
        assertThat(dao.selectAll()).doesNotContainAnyElementsOf(entries);
    }
}
