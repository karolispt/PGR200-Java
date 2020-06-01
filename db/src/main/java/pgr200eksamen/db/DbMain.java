package pgr200eksamen.db;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import pgr200eksamen.db.entities.*;

import org.postgresql.ds.PGSimpleDataSource;
import org.flywaydb.core.Flyway;
import pgr200eksamen.db.entities.*;

import javax.sql.*;
import java.sql.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DbMain {
    static DataSource source = DataSource();

    static String[] Tracks = {"Large", "Medium", "Small"};
    static String[] Conferences = {"CES", "WWDC", "GoogleIO"};
    static String[] Titles = {"Consumer Electronics Show", "Worldwide Developers Conference", "Google I/O"};
    static String[] Topics = {"Driverless Vehicles", "iOS/iPadOS 14, iPhone 12", "Next Generation of Pixel"};
    static String[] Descriptions = {"The tech of tomorrow", "Time for change..", "The camera, revolutionized."};

    // DAO's
    static DayDao dayDao = new DayDao(source);
    static DiscussionDao discussionDao = new DiscussionDao(source);
    static ConferenceDao conferenceDao = new ConferenceDao(source);
    static TimeEntryDao timeEntryDao = new TimeEntryDao(source);
    static TimetableDao timetableDao = new TimetableDao(source);
    static TrackDao trackDao = new TrackDao(source);

    static Random r = new Random();

    public DbMain() {

    }

    public DbMain(DataSource source)
    {
        this.source = source;
    }

    public static void migrate()
    {
        FluentConfiguration config = Flyway.configure();
        config.dataSource(source);
        Flyway flyway = config.load();
        flyway.migrate();
    }

    public static void initialMigrate(DataSource source)
    {
        FluentConfiguration config = Flyway.configure();
        config.dataSource(source);

        Flyway flyway = config.load();
        flyway.migrate();
    }

    public static void resetDB() throws SQLException
    {
        Connection conn = source.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DROP TABLE Discussion CASCADE;" +
                "DROP TABLE Conference CASCADE" +
                "DROP TABLE TimeEntry CASCADE" +
                "DROP TABLE TimetableDay CASCADE" +
                "DROP TABLE CASCADE" +
                "DROP TABLE CASCADE");

        migrate();

        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public static void main(String[] args) throws SQLException, IOException
    {
        int entriesToMake = 5;
        Day[] days = new Day[entriesToMake];
        Discussion[] discussions = new Discussion[entriesToMake];
        TimeEntry[] entries = new TimeEntry[entriesToMake];
        Track[] tracks = new Track[entriesToMake];
        Timetable[] timetables = new Timetable[entriesToMake];

        for (int i = 0; i < entriesToMake; i++)
        {
            entries[i] = newTime();
            timeEntryDao.insert(entries[i]);

            discussions[i] = newDiscussion();
            discussionDao.insert(discussions[i]);

            days[i] = newDay();
            dayDao.insert(days[i]);

            tracks[i] = newTrack();
            trackDao.insert(tracks[i]);

            Timetable timetable = new Timetable(days[i].Id(), entries[i].Id(), tracks[i].Id());
            timetableDao.insert(timetable);

            Conference conference = newConference();
            conferenceDao.insert(conference);
        }
    }

    public static DataSource DataSource()
    {
        Properties config = new Properties();
        InputStream is = null;

        try
        {
            is = new FileInputStream("config.properties");
            config.load(is);

            PGSimpleDataSource source = new PGSimpleDataSource();

            String url = config.getProperty("dataSource.url");
            String usr = config.getProperty("dataSource.user");
            String pwd = config.getProperty("dataSource.pass");

            source.setUrl(url);
            source.setUser(usr);
            source.setPassword(pwd);

            initialMigrate(source);

            return source;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if (is != null)
                try {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        return null;
    }

    static Discussion newDiscussion() {
        String title = Titles[ r.nextInt(Titles.length)];
        String desc = Descriptions[ r.nextInt(Descriptions.length)];
        String topic = Topics[ r.nextInt(Topics.length)];
        Discussion d = new Discussion(title, desc, topic);

        return d;
    }

    static TimeEntry newTime() {
        int startHour = r.nextInt(24);
        int startMinute = r.nextInt(60);

        int endHour = r.nextInt(24);
        int endMinute = r.nextInt(60);

        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);

        TimeEntry t = new TimeEntry(startTime, endTime);
        return t;
    }

    static Day newDay()
    {
        LocalDate day = LocalDate.now();
        Day d = new Day(day);

        return d;
    }

    static Track newTrack()
    {
        String track = Tracks[ r.nextInt(Tracks.length)];
        Track t = new Track(track);
        return t;
    }

    static Conference newConference()
    {
        String conferenceName = Conferences[ r.nextInt(Conferences.length)];
        Conference c = new Conference(conferenceName);

        return c;
    }
}
