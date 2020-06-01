package pgr200eksamen.db.entities;

import java.util.Objects;

public class Timetable {
    int tableId;
    int entryId;
    int dayId;
    int trackId;

    public Timetable()
    {

    }

    public Timetable(int dayId, int timeEntryId, int trackId)
    {
        this.dayId = dayId;
        this.entryId = timeEntryId;
        this.trackId = trackId;
    }

    // Timetable ID Get/Set
    public int Id()
    {
        return tableId;
    }

    public void setId(int id)
    {
        this.tableId = id;
    }

    // Timetable EntryID Get/Set
    public int EntryId()
    {
        return entryId;
    }

    public void setEntryId(int id)
    {
        this.entryId = id;
    }

    // Timetable TrackID Get/Set
    public int TrackId()
    {
        return trackId;
    }

    public void setTrackId(int id)
    {
        trackId = id;
    }

    // Timetable DayID Get/Set
    public int DayId()
    {
        return dayId;
    }

    public void setDayId(int id)
    {
        this.dayId = id;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "tableId=" + tableId +
                ", entryId=" + entryId +
                ", dayId=" + dayId +
                ", trackId=" + trackId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timetable timetable = (Timetable) o;
        return tableId == timetable.tableId &&
                entryId == timetable.entryId &&
                dayId == timetable.dayId &&
                trackId == timetable.trackId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, entryId, dayId, trackId);
    }
}
