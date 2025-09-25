package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public List<Integer> subtasksId = new ArrayList<>(); // поле для хранения id подзадач

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(int subtasksId) {
        this.subtasksId.add(subtasksId);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setDuration(Subtask subtask) {
        long durationMinutes = subtask.getDuration();

        if (durationMinutes == 0) {
            duration = Duration.ZERO;
            return;
        }

        if (duration == null) {
            duration = Duration.ofMinutes(durationMinutes);
            return;
        }

        duration = Duration.between(startTime, endTime);
    }

    public long getDuration() {
        if (duration == null) {
            return 0;
        }
        return duration.get(ChronoUnit.SECONDS) / 60;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) {
            return getDefaultDateTime();
        }
        return startTime;
    }

    public void setStartTime(Subtask subtask) {
        LocalDateTime localDateTimeSubtask = subtask.getStartTime();

        if (startTime == null) {
            startTime = localDateTimeSubtask;
        }

        if (startTime.isAfter(localDateTimeSubtask)) {
            startTime = localDateTimeSubtask;
        }
        endTime = getEndTime(subtask);
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return getDefaultDateTime();
        }
        return startTime.plus(duration);
    }

    public LocalDateTime getEndTime(Subtask subtask) {
        if (startTime == null) {
            endTime = Task.getDefaultDateTime();
        } else if (subtask.getDuration() == 0) {
            endTime = startTime;
        } else {
            endTime = startTime.plus(Duration.ofMinutes(subtask.getDuration()));
        }

        LocalDateTime subtaskEndTime = subtask.getEndTime();

        if (endTime.isBefore(subtaskEndTime)) {
            endTime = subtaskEndTime;
        }
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(getTitle(), epic.getTitle()) &&
                Objects.equals(getDescription(), epic.getDescription()) &&
                getStatus() == epic.getStatus() &&
                getId() == epic.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return new StringBuilder("Задача (Название: ").append(getTitle())
                .append("; Описание: ").append(getDescription())
                .append("; id: ").append(getId())
                .append("; Статус: ").append(getStatus())
                .append("; Начало: ").append(startTime)
                .append("; Продолжительность (минут): ").append(getDuration())
                .append("; Конец: ").append(endTime)
                .append(")").append("\n").toString();
    }
}