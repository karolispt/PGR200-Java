package pgr200eksamen.db.entities;

import java.util.Objects;

public class Track {
    int id;
    String name;

    public Track()
    {

    }

    public Track(String name)
    {
        this.name = name;
    }

    // Track id Get/Set
    public int Id()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    // Track name Get/Set
    public String Name()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return id == track.id &&
                Objects.equals(name, track.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
