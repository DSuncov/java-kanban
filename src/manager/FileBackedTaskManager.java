package manager;


import exception.ManagerLoadException;
import exception.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    private String header = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Epic epic, Subtask subtask) {
        super.createSubtask(epic, subtask);
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(Epic epic, int id) {
        super.removeSubtask(epic, id);
        save();
    }

    @Override
    public void updateTask(int id, String title, String description, Status status) {
        super.updateTask(id, title, description, status);
        save();
    }

    @Override
    public void updateEpic(int id, String title, String description) {
        super.updateEpic(id, title, description);
        save();
    }

    @Override
    public void updateSubtask(int epicId, int subtaskId, String title, String description, Status status) {
        super.updateSubtask(epicId, subtaskId, title, description, status);
        save();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(header);
            writer.newLine();

            for (Map.Entry<Integer, Task> entry : getTasksList().entrySet()) {
                Task task = entry.getValue();
                writer.write(toString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : getEpicsList().entrySet()) {
                Epic epic = entry.getValue();
                writer.write(toString(epic));
                writer.newLine();
            }
            for (HashMap<Integer, Subtask> entry : getSubtasksList().values()) {
                for (Subtask subtask : entry.values()) {
                    writer.write(toString(subtask));
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении.");
        }
    }

    static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        try {
            String result = Files.readString(file.toPath(), StandardCharsets.UTF_8).replace("\r", "");
            if (result.isBlank()) {
                throw new ManagerLoadException("Файл пустой или содержит только пробельные символы.");
            }

            String[] str = result.split("\n");
            int startElement;
            if (str[0].equals(taskManager.header)) {
                startElement = 1;
            } else {
                startElement = 0;
            }

            taskManager.setHeader(str[0]);
            for (int i = startElement; i < str.length; i++) {
                Task task = taskManager.fromString(str[i]);
                if (task instanceof Subtask subtask) {
                    subtask.setId(task.getId());
                    taskManager.createSubtask(taskManager.getEpic(subtask.getEpicId()), subtask);
                } else if (task instanceof Epic epic) {
                    taskManager.createEpic(epic);;
                } else {
                    taskManager.createTask(task);
                }
            }
            System.out.println(result);
        } catch (IOException e) {
            throw new ManagerLoadException("Произошла ошибка при загрузке из файла.");
        }
        return taskManager;
    }

    public Task fromString(String value) {
        String[] elements = value.split(",");
        Task task = null;
        Epic epic = null;
        Subtask subtask = null;

        int id = Integer.parseInt(elements[0]);
        String title = elements[2];
        Status status = Status.valueOf(elements[3]);
        String description = elements[4];
        int epicId;

        if (elements[1].equals("TASK")) {
            task = new Task(title, description, status);
            task.setId(id);
        }

        if (elements[1].equals("EPIC")) {
            epic = new Epic(title, description, status);
            epic.setId(id);
        }

        if (elements[1].equals("SUBTASK")) {
            epicId = Integer.parseInt(elements[5]);
            subtask = new Subtask(title, description, status, epicId);
            subtask.setId(id);
        }

        return task != null ? task : epic != null ? epic : subtask;
    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",")
                .append(TaskType.getType(task)).append(",")
                .append(task.getTitle()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription());

        if (task instanceof Subtask) {
            sb.append(",").append(((Subtask) task).getEpicId());
        }
        return sb.toString();
    }
}
