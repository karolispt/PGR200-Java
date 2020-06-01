package pgr200eksamen.db;

import pgr200eksamen.db.entities.Day;

import javax.sql.*;
import java.sql.*;
import java.time.*;
import java.util.List;

public class DayDao extends DAO<Day> {

    public DayDao(DataSource source)
    {
        super(source);
        super.table = "ConferenceDay";
        super.idName = "DayId";
        super.columns = "(DayDate)";
        super.values = "(?)";
    }

    void applyToTimetable(Day d) throws SQLException
    {
        DAOTuple data = Statement("INSERT INTO Timetable (DayId) values (?)");
        PreparedStatement stmt = data.getPreparedStatement();
        stmt.setInt(1, d.Id());
        stmt.executeUpdate();

        data.close();
    }

    @Override
    void executeSave(Day day, PreparedStatement stmt) throws SQLException
    {
        List<Day> entities = selectAll();

        if (!entities.contains(day))
        {
            LocalDate date = day.Date();
            stmt.setDate(1, Date.valueOf(date));
            stmt.executeUpdate();

            ResultSet result = stmt.getGeneratedKeys();
            result.next();

            int id = result.getInt(idName);
            day.setId(id);

            result.close();
        }
        else
        {
            for (Day eDay : entities)
            {
                if (day.equals(eDay))
                {
                    day.setId(eDay.Id());
                }
            }
        }
    }

    @Override
    Day getEntity(ResultSet result) throws SQLException
    {
        Day d = new Day();

        d.setId(result.getInt(idName));

        Date date = result.getDate("DayDate");
        LocalDate lDate = date.toLocalDate();


        d.setDate(lDate);

        return d;
    }
}
