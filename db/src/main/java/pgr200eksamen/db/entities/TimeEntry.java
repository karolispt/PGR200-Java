package pgr200eksamen.db.entities;

import java.time.LocalTime;
import java.util.Objects;

public class TimeEntry {
    int id;
    LocalTime startTime;
    LocalTime endTime;

    public TimeEntry()
    {

    }

    public TimeEntry(LocalTime start, LocalTime end)
    {
        this.startTime = start;
        this.endTime = end;
    }

    // TimeEntry Id Get/Set
    public int Id()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    // TimeEntry StartTime Get/Set
    public LocalTime startTime()
    {
        return startTime;
    }

    public void setStartTime(LocalTime time)
    {
        this.startTime = time;
    }
    // TimeEntry EndTime Get/Set
    public LocalTime endTime()
    {
        return endTime;
    }

    public void setEndTime(LocalTime time)
    {
        this.endTime = time;
    }

    @Override
    public String toString() {
        return "TimeEntry{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return id == timeEntry.id &&
                Objects.equals(startTime, timeEntry.startTime) &&
                Objects.equals(endTime, timeEntry.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime);
    }
}
