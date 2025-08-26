package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    public List<Integer> subtasksId = new ArrayList<>(); // поле для хранения id подзадач

    public Epic(String title, String description, Status status) {
        super(title, description, Status.NEW);
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksid(int subtasksId) {
        this.subtasksId.add(subtasksId);
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
                .append(")").append("\n").toString();
    }

}