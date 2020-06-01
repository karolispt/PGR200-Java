package pgr200eksamen.db.entities;

import java.util.*;

public class Conference {
    int id;
    String title;
    List<Day> days;
    List<Track>  tracks;

    public Conference()
    {

    }

    public Conference(String title)
    {
        this.title = title;
    }

    // Conference Id Get/Set
    public int Id()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    // Conference Title Get/Set
    public String Title()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    // Conference Days Get/Set
    public List<Day> Days()
    {
        return days;
    }

    public void setDays(List<Day> days)
    {
        this.days = days;
    }

    // Conference Tracks Get/Set
    public List<Track> Tracks()
    {
        return tracks;
    }

    public void setTracks(List<Track> tracks)
    {
        this.tracks = tracks;
    }

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", days=" + days +
                ", tracks=" + tracks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conference that = (Conference) o;
        return id == that.id &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
