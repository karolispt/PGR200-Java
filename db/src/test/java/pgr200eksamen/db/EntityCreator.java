package pgr200eksamen.db;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import pgr200eksamen.db.entities.*;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public abstract class EntityCreator {
    String[] Tracks = {"Large", "Medium", "Small"};
    String[] Conferences = {"CES", "WWDC", "GoogleIO"};
    String[] Titles = {"Consumer Electronics Show", "Worldwide Developers Conference", "Google I/O"};
    String[] Topics = {"Driverless Vehicles", "iOS/iPadOS 14, iPhone 12", "Next Generation of Pixel"};
    String[] Descriptions = {"The tech of tomorrow", "Time for change..", "The camera, revolutionized."};

    Random r = new Random();

    public TimeEntry newTime() {
        //LocalTime startTime = LocalTime.of(18, 30);
        //LocalTime endTime = LocalTime.of(18, 30);
        LocalTime time = LocalTime.MIDNIGHT;

        TimeEntry t = new TimeEntry(time, time);
        return t;
    }

    public Discussion newDiscussion()
    {
        return new Discussion(
                Titles[r.nextInt(Titles.length)],
                Descriptions[r.nextInt(Descriptions.length)],
                Topics[r.nextInt(Topics.length)]
        );
    }

    public Day newDay()
    {
        LocalDate day = LocalDate.now();
        Day d = new Day(day);

        return d;
    }

    public Track newTrack()
    {
        String track = Tracks[ r.nextInt(Tracks.length)];
        Track t = new Track(track);
        return t;
    }

    public Conference newConference()
    {
        String conferenceName = Conferences[ r.nextInt(Conferences.length)];
        Conference c = new Conference(conferenceName);

        return c;
    }

    public static DataSource createDataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        Flyway.configure().dataSource(ds).load().migrate();
        return ds;
    }
}
