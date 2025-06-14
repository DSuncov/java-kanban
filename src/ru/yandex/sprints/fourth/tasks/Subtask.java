package ru.yandex.sprints.fourth.tasks;

import java.util.Objects;

public class Subtask extends Task {

    private int epicid;

    public Subtask(String title, String description, Status status) {
        super(title, description, status);
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
        if (!(o instanceof Subtask subtask)) return false;
        return id == subtask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
