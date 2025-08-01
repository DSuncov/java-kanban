package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    //Получение списков
    Map<Integer, Task> getTasksList();
    Map<Integer, Epic> getEpicsList();
    Map<Epic, HashMap<Integer, Subtask>> getSubtasksList();

    //Печать задач из коллекции
    List<Task> getAllTask();
    List<Epic> getAllEpic();
    List<Subtask> getSubtaskByEpic(Epic epic);

    //Удаление всех задач из коллекции
    void removeAllTask();
    void removeAllEpic();
    void removeAllSubtask();

    //Получение задачи по id
    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(Epic epic, int id);

    //Добавление задачи в коллекцию
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubtask(Epic epic, Subtask subtask);

    //Удаление задачи по id из коллекции
    void removeTask(int id);
    void removeEpic(int id);
    void removeSubtask(Epic epic, int id);

    //Обновление задачи
    void updateTask(int id, String title, String description, Status status);
    void updateEpic(int id, String title, String description);
    void updateSubtask(int epicid, int subtaskid, String title, String description, Status status);

    HistoryManager getHistoryManager();
}
