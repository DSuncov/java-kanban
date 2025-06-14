package ru.yandex.sprints.fourth.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    public List<Integer> subtasksid = new ArrayList<>(); // поле для хранения id подзадач

    public Epic(String title, String description, Status status) {
        super(title, description, Status.NEW);
    }

    public List<Integer> getSubtasksid() {
        return subtasksid;
    }

    public void setSubtasksid(int subtasksid) {
        this.subtasksid.add(subtasksid);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic epic)) return false;
        return id == epic.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}