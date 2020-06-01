package pgr200eksamen.db.entities;

import java.io.Serializable;
import java.util.Objects;

public class Discussion implements Serializable {
    int id;
    String title;
    String description;
    String topic;
    TimeEntry time;

    public Discussion()
    {

    }

    public Discussion(String title, String description, String topic)
    {
        this.title = title;
        this.description = description;
        this.topic = topic;
    }

    public int Id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String Title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String Description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String Topic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public TimeEntry Time() {
        return time;
    }

    public void setTime(TimeEntry time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Discussion{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", topic='" + topic + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discussion that = (Discussion) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, topic);
    }
}
