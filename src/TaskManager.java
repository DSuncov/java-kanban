import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private int id = 1; //id для простых задач и эпиков
    private int subtaskId; //id для подзадач

    Map<Integer, Task> commonTasks = new HashMap<>(); // для хранения обычных задач
    Map<Integer, Epic> epics = new HashMap<>(); // для хранения крупных задач

    Map<Epic, HashMap<Integer, Subtask>> subtasks = new HashMap<>(); // для хранения подзадач

    int getId() {
        return id;
    }

    int getSubtaskId() {
        return subtaskId;
    }

    //Печать всех задач из коллекции

    void printAllTask() {
        if (commonTasks.isEmpty()) {
            System.out.println("Коллекция пуста!");
            return;
        }
        for (Task value : commonTasks.values()) {
            System.out.println(value);
        }
    }

    void printAllEpic() {
        if (epics.isEmpty()) {
            System.out.println("Коллекция пуста!");
            return;
        }
        for (Epic value : epics.values()) {
            System.out.println(value);
        }
    }

    void printAllSubtask() {
        if (subtasks.isEmpty()) {
            System.out.println("Коллекция пуста!");
            return;
        }
        for (Epic key : subtasks.keySet()) {
            for (Subtask value : subtasks.get(key).values()) {
                System.out.println(value);
            }
        }
    }

    void printSubtaskByEpic(Epic epic) {
        if (subtasks.isEmpty()) {
            System.out.println("Коллекция пуста!");
            return;
        }

        for (Subtask value : subtasks.get(epic).values()) {
            System.out.println(value);
        }
    }

    //Удаление всех задач из коллекции

    void removeAllTask() {
        commonTasks.clear();
        if (commonTasks.isEmpty()) {
            System.out.println("Коллекция успешно очищена!");
        }
    }

    void removeAllEpic() {
        epics.clear();
        if (epics.isEmpty()) {
            System.out.println("Коллекция успешно очищена!");
        }
    }

    void removeAllSubtask() {
        subtasks.clear();
        if (subtasks.isEmpty()) {
            System.out.println("Коллекция успешно очищена!");
        }
    }

    //Получение задачи по id

    Task getTask(int id) {
        if (!commonTasks.containsKey(id)) {
            System.out.print("Обычной задачи с таким id не существует! ");
            return null;
        }
        return commonTasks.get(id);
    }

    Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.print("Эпика с таким id не существует! ");
            return null;
        }
        return epics.get(id);
    }

    Subtask getSubtask(Epic epic, int id) {
        if (!subtasks.containsKey(epic)) {
            System.out.print("Подзадачи с таким id не существует! ");
            return null;
        }
        return subtasks.get(epic).get(id);
    }

    //Добавление задачи в коллекцию

    void createTask(Task task) {
        commonTasks.put(getId(), task);
        task.setId(id);
        id++;
    }

    void createEpic(Epic epic) {
        epics.put(getId(), epic);
        epic.setId(id);
        id++;
    }

    void createSubtask(Epic epic, Subtask subtask) {
        if (subtasks.containsKey(epic)) {
            subtaskId = subtasks.get(epic).size() + 1; // если в коллекции с данным ключом уже есть элемент, то id получаем как сумму размера коллекции + 1
            subtasks.get(epic).put(getSubtaskId(), subtask);
        } else {
            subtaskId = 1; //если элемента с данным ключом нет, то начинаем нумерацию подзадач с 1
            subtasks.computeIfAbsent(epic, k -> new HashMap<>()).put(getSubtaskId(), subtask);
        }
        subtask.setId(subtaskId);
    }

    //Удаление задачи по id из коллекции

    void removeTask(int id) {
        if (!commonTasks.containsKey(id)) {
            System.out.println("Обычной задачи с таким id не существует!");
            return;
        }
        commonTasks.remove(id);
    }

    void removeEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпика с таким id не существует!");
            return;
        }
        epics.remove(id);
    }

    void removeSubtask(Epic epic, int id) {
        if (!subtasks.containsKey(epic)) {
            System.out.println("Подзадачи с таким id не существует!");
            return;
        }
        subtasks.get(epic).remove(id);
    }

    //Получение статуса определенной задачи

    Status getTaskStatus(Task task) {
        return task.getStatus();
    }

    Status getEpicStatus(Epic epic) {
        return epic.getStatus();
    }

    Status getSubtaskStatus(Subtask subtask) {
        return subtask.getStatus();
    }

    //Обновление задачи
    void updateTask(Task task, Task newTask) {
        if (!task.getStatus().equals(newTask.getStatus())) {
            task.setStatus(newTask.getStatus());
        }
        commonTasks.put(task.getId(), newTask);
    }

    void updateEpic(Epic epic, Epic newEpic) {
        epics.put(epic.getId(), newEpic);

        boolean isAllSubtaskDone = false;

        for (Epic key : subtasks.keySet()) {
            for (Subtask value : subtasks.get(key).values()) {
                Subtask subtaskForCheckStatus = value;
                if (subtaskForCheckStatus.getStatus() == Status.DONE) {
                    isAllSubtaskDone = true;
                } else if (subtaskForCheckStatus.getStatus() == Status.IN_PROGRESS){
                    isAllSubtaskDone = false;
                    epic.setStatus(Status.IN_PROGRESS);
                    break;
                }
            }
        }

        if (isAllSubtaskDone) {
            epic.setStatus(Status.DONE);
        }
    }

    void updateSubtask(Epic epic, Subtask subtask, Subtask newSubtask) {
        subtasks.computeIfAbsent(epic, k -> new HashMap<>()).put(subtask.getId(), newSubtask);
        subtask.setStatus(newSubtask.getStatus());
    }
}
