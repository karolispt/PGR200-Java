package pgr200eksamen.db.entities;

import java.time.*;
import java.util.*;

public class Day {
    int id;
    LocalDate date;

    public Day()
    {

    }

    public Day(LocalDate date)
    {
        this.date = date;
    }

    // Id Get/Set
    public int Id()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    // Date Get/Set
    public LocalDate Date()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return id == day.id &&
                Objects.equals(date, day.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
