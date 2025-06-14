package ru.yandex.sprints.fourth.manager;

import ru.yandex.sprints.fourth.tasks.Epic;
import ru.yandex.sprints.fourth.tasks.Status;
import ru.yandex.sprints.fourth.tasks.Subtask;
import ru.yandex.sprints.fourth.tasks.Task;

import java.util.*;

public class TaskManager {
    private int idCounter; //id для простых задач и эпиков
    private int subtaskIdCounter; //id для подзадач

    private Map<Integer, Task> commonTasks = new HashMap<>(); // для хранения обычных задач
    private Map<Integer, Epic> epics = new HashMap<>(); // для хранения крупных задач
    private Map<Epic, HashMap<Integer, Subtask>> subtasks = new HashMap<>(); // для хранения подзадач

    private int getId() {
        return idCounter;
    }

    private int getSubtaskId() {
        return subtaskIdCounter;
    }

    //Печать задач из коллекции

    public List<Task> getAllTask() {
        if (commonTasks.isEmpty()) {
            throw new NullPointerException("Коллекция пуста. Невозможно получить список задач.");
        }
        return new ArrayList<>(commonTasks.values());
    }

    public List<Epic> getAllEpic() {
        if (epics.isEmpty()) {
            throw new NullPointerException("Коллекция пуста. Невозможно получить список эпиков.");
        }
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getSubtaskByEpic(Epic epic) {
        if (subtasks.isEmpty()) {
            throw new NullPointerException("Коллекция пуста. Невозможно получить список подзадач.");
        }
        return new ArrayList<>(subtasks.get(epic).values());
    }

    //Удаление всех задач из коллекции

    public void removeAllTask() {
        commonTasks.clear();
        System.out.println("Коллекция успешно очищена!");
    }

    public void removeAllEpic() {
        epics.clear();
        subtasks.clear();
        System.out.println("Коллекция успешно очищена!");
    }

    public void removeAllSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
        }
        System.out.println("Коллекция успешно очищена!");
    }

    //Получение задачи по id

    public Task getTask(int id) {
        if (!commonTasks.containsKey(id)) {
            throw new NullPointerException("Обычной задачи с таким id не существует.");
        }
        return commonTasks.get(id);
    }

    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            throw new NullPointerException("Эпика с таким id не существует.");
        }
        return epics.get(id);
    }

    public Subtask getSubtask(Epic epic, int id) {
        if (!subtasks.get(epic).containsKey(id)) {
            throw new NullPointerException("Подзадачи с таким id не существует.");
        }
        return subtasks.get(epic).get(id);
    }

    //Добавление задачи в коллекцию

    public void createTask(Task task) {
        idCounter++;
        commonTasks.put(getId(), task);
        task.setId(idCounter);
    }

    public void createEpic(Epic epic) {
        idCounter++;
        epics.put(getId(), epic);
        epic.setId(idCounter);
    }

    public void createSubtask(Epic epic, Subtask subtask) {
        if (subtasks.containsKey(epic)) {
            subtaskIdCounter = subtasks.get(epic).size() + 1; // если в коллекции с данным ключом уже есть элемент, то id получаем как сумму размера коллекции + 1
            subtasks.get(epic).put(getSubtaskId(), subtask);
        } else {
            subtaskIdCounter = 1; //если элемента с данным ключом нет, то начинаем нумерацию подзадач с 1
            subtasks.computeIfAbsent(epic, k -> new HashMap<>()).put(getSubtaskId(), subtask);
        }
        subtask.setId(subtaskIdCounter); // устанавливаем id для подзадачи
        subtask.setEpicid(epic.getId()); // получаем id эпика и устанавливаем его для подзадачи
        epic.setSubtasksid(subtaskIdCounter);
    }

    //Удаление задачи по id из коллекции

    public void removeTask(int id) {
        if (!commonTasks.containsKey(id)) {
            System.out.print("Обычной задачи с таким id не существует!");
            return;
        }
        commonTasks.remove(id);
        System.out.println("Задача успешно удалена!");
    }

    public void removeEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.print("Эпика с таким id не существует!");
            return;
        }
        subtasks.remove(getEpic(id));
        epics.remove(id);
        System.out.println("Задача успешно удалена!");
    }

    public void removeSubtask(Epic epic, int id) {
        if (!subtasks.containsKey(epic)) {
            System.out.print("Подзадачи с таким id не существует!");
            return;
        }
        subtasks.get(epic).remove(id);
        System.out.println("Задача успешно удалена!");
    }

    //Обновление задачи
    public void updateTask(int id, String title, String description, Status status) {
        if (!commonTasks.containsKey(id)) {
            System.out.println("Задача с таким id отсутствует");
            return;
        }
        Task task = commonTasks.get(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        System.out.println("Задача обновлена");
    }

    public void updateEpic(int id, String title, String description) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик с таким id отсутствует");
            return;
        }
        Epic epic = epics.get(id);
        epic.setTitle(title);
        epic.setDescription(description);
        System.out.println("Эпик обновлен");
    }

    public void updateSubtask(int epicid, int subtaskid, String title, String description, Status status) {
        Epic epic = epics.get(epicid);
        if (epic == null) {
            System.out.println("Эпик с таким id отсутствует");
            return;
        }

        if (!subtasks.containsKey(epic)) {
            System.out.println("Подзадачи у эпика отсутствуют");
            return;
        }

        Subtask subtask = subtasks.get(epic).get(subtaskid);
        if (subtask == null) {
            System.out.println("Подзадача с таким id отсутствует");
            return;
        }

        subtask.setTitle(title);
        subtask.setDescription(description);
        subtask.setStatus(status);
        System.out.println("Подзадача обновлена");

        updateEpicStatus(epicid);
    }

    private void updateEpicStatus(int epicid) {
        Epic epic = epics.get(epicid);
        if (epic == null) return;
        if (!subtasks.containsKey(epic) || subtasks.get(epic).isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int newCount = 0;
        int doneCount = 0;
        int total = subtasks.get(epic).size();

        for (Subtask subtask : subtasks.get(epic).values()) {
            if (subtask.getStatus() == Status.NEW) {
                newCount++;
            }
            if (subtask.getStatus() == Status.DONE) {
                doneCount++;
            }
        }

        if (newCount == total) {
            epic.setStatus(Status.NEW);
        } else if (doneCount == total) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
