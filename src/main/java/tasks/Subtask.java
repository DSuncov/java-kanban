package tasks;

import java.util.Objects;

public class Subtask extends Task {

    private int epicid;

    public Subtask(String title, String description, Status status) {
        super(title, description, status);
    }

    public Subtask(String title, String description, Status status, int epicid) {
        super(title, description, status);
        this.epicid = epicid;
    }

    public Subtask(String title, String description, int id, Status status, int epicid) {
        super(title, description, id, status);
        this.epicid = epicid;
    }

    public int getEpicid() {
        return epicid;
    }

    public void setEpicid(int epicid) {
        this.epicid = epicid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(getTitle(), subtask.getTitle()) &&
                Objects.equals(getDescription(), subtask.getDescription()) &&
                getStatus() == subtask.getStatus() &&
                getId() == subtask.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return new StringBuilder("Подзадача (Название: ").append(getTitle())
                .append("; Описание: ").append(getDescription())
                .append("; id: ").append(getId())
                .append("; Статус: ").append(getStatus())
                .append(")").append("\n").toString();
    }
}
