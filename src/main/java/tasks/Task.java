package tasks;

import java.util.Objects;

public class Task {

    private String title;
    private String description;
    private int id;
    private Status status;

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
                .append(")").append("\n").toString();
    }
}
