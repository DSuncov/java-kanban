package ru.yandex.sprints.fourth.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtasksid = new ArrayList<>(); // поле для хранения id подзадач

    public Epic(String title, String description, Status status) {
        super(title, description, Status.NEW);
    }

    public List<Integer> getSubtasksid() {
        return subtasksid;
    }

    public void setSubtasksid(int subtasksid) {
        this.subtasksid.add(subtasksid);
    }

}