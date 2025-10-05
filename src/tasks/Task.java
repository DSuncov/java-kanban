package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {

    private int id;
    private String title;
    private Status status;
    private String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static LocalDateTime defaultDateTime = LocalDateTime.parse("1970-01-01 00:00", formatter);

    public Task(String title, String description, Status status, String start, Long duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.parse(start, formatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description, int id, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
         if (startTime == null) {
             return defaultDateTime;
         }
         return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    public long getDuration() {
        if (duration == null) {
            return 0;
        }
        return duration.get(ChronoUnit.SECONDS) / 60;
    }

    public void setDuration(long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return getDefaultDateTime();
        }
        return startTime.plus(duration);
    }

    public static LocalDateTime getDefaultDateTime() {
        return defaultDateTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                status == task.status &&
                id == task.id;
    }

    @Override
    public String toString() {
        return new StringBuilder("Задача (Название: ").append(title)
                .append("; Описание: ").append(description)
                .append("; id: ").append(id)
                .append("; Статус: ").append(status)
                .append("; Начало: ").append(getStartTime())
                .append("; Продолжительность (минут): ").append(getDuration())
                .append("; Конец: ").append(getEndTime())
                .append(")").append("\n").toString();
    }
}
