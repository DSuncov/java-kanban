package ru.yandex.sprints.fourth.tasks;

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
}
