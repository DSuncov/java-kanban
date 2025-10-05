package manager;

import exception.NotFoundException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    private int taskIdCounter; //id для простых задач и эпиков
    private int epicIdCounter; //id для простых задач и эпиков
    private int subtaskIdCounter; //id для подзадач

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Map<Integer, Task> commonTasks = new HashMap<>(); // для хранения обычных задач
    private final Map<Integer, Epic> epics = new HashMap<>(); // для хранения крупных задач
    private final Map<Epic, HashMap<Integer, Subtask>> subtasks = new HashMap<>(); // для хранения подзадач
    private final Set<Task> tasksSortedByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime).thenComparing(Task::getDuration));
    private Task lastIntersection;

    private int getTaskId() {
        return taskIdCounter;
    }

    private int getEpicId() {
        return epicIdCounter;
    }

    private int getSubtaskId() {
        return subtaskIdCounter;
    }

    @Override
    public Map<Integer, Task> getTasksList() {
        return commonTasks;
    }

    @Override
    public Map<Integer, Epic> getEpicsList() {
        return epics;
    }

    @Override
    public Map<Epic, HashMap<Integer, Subtask>> getSubtasksList() {
        return subtasks;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    //Печать задач из коллекции
    @Override
    public List<Task> getAllTask() {
        if (commonTasks.isEmpty()) {
            throw new NotFoundException("Коллекция пуста. Невозможно получить список задач.");
        }
        return new ArrayList<>(commonTasks.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        if (epics.isEmpty()) {
            throw new NotFoundException("Коллекция пуста. Невозможно получить список эпиков.");
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
        if (subtasks.isEmpty()) {
            throw new NotFoundException("Коллекция пуста. Невозможно получить список подзадач.");
        }

        List<Subtask> allSubtasks = new ArrayList<>();
        for (Map<Integer, Subtask> map: subtasks.values()) {
            allSubtasks.addAll(map.values());
        }

        return allSubtasks;
    }

    @Override
    public List<Subtask> getSubtaskByEpic(Epic epic) {
        if (subtasks.isEmpty()) {
            throw new NotFoundException("Коллекция пуста. Невозможно получить список подзадач.");
        }
        return new ArrayList<>(subtasks.get(epic).values());
    }

    //Удаление всех задач из коллекции
    @Override
    public void removeAllTask() {
        commonTasks.clear();
        taskIdCounter = 0;
        System.out.println("Коллекция успешно очищена!");
    }

    @Override
    public void removeAllEpic() {
        epics.clear();
        subtasks.clear();
        epicIdCounter = 0;
        subtaskIdCounter = 0;
        System.out.println("Коллекция успешно очищена!");
    }

    @Override
    public void removeAllSubtask() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
        }
        epicIdCounter = 0;
        System.out.println("Коллекция успешно очищена!");
    }

    //Получение задачи по id
    @Override
    public Task getTask(int id) {
        if (!commonTasks.containsKey(id)) {
            throw new NotFoundException("Задачи с таким id не существует.");
        }
        Task task = commonTasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            throw new NotFoundException("Эпика с таким id не существует.");
        }
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(Epic epic, int id) {
        if (!subtasks.get(epic).containsKey(id)) {
            throw new NotFoundException("Подзадачи с таким id не существует.");
        }
        Subtask subtask = subtasks.get(epic).get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask;

        Optional<Subtask> optionalSubtask = subtasks.values().stream()
                .filter(map -> map.containsKey(id))
                .map(map -> map.get(id))
                .findFirst();

        if (optionalSubtask.isPresent()) {
            subtask = optionalSubtask.get();
        } else {
            throw new NotFoundException("Подзадачи с таким id не существует.");
        }

        historyManager.add(subtask);
        return subtask;
    }

    //Добавление задачи в коллекцию
    @Override
    public void createTask(Task task) {
        int setterId = task.getId(); // Если создаем задачу и вручную устанавливаем id

        if (setterId == taskIdCounter + 1 || setterId == epicIdCounter + 1) {
            taskIdCounter = getTaskId() + 1;
        }

        if (taskIdCounter >= epicIdCounter) {
            taskIdCounter++;
            task.setId(taskIdCounter);
        } else {
            taskIdCounter = epicIdCounter + 1;
            int id = task.getId();
            task.setId(Math.max(id, taskIdCounter));
        }

        if (setterId != 0) {
            task.setId(setterId);
        }

        tasksSortedByStartTime.add(task);

        if (isIntersection(task)) {
            System.out.println("Подзадачи пересекаются. Время старта добавляемой задачи: <" + task.getTitle() + "> " + task.getStartTime() + ", время окончания: " + task.getEndTime() +
                    ". Имеется пересечение с задачей: <" + lastIntersection.getTitle() + ">. Время старта: " + lastIntersection.getStartTime() + ", время окончания: " + lastIntersection.getEndTime());
            tasksSortedByStartTime.remove(task);
            return;
        }
        commonTasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        int setterId = epic.getId(); // Если создаем задачу и вручную устанавливаем id

        if (setterId == epicIdCounter + 1 || setterId == taskIdCounter + 1) {
            epicIdCounter = getEpicId() + 1;
        }

        if (epicIdCounter <= taskIdCounter) {
            epicIdCounter = taskIdCounter + 1;
            epic.setId(epicIdCounter);
        } else {
            epicIdCounter++;
            int id = epic.getId();
            epic.setId(Math.max(id, epicIdCounter));
        }

        if (setterId != 0) {
            epic.setId(setterId);
        }

        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Epic epic, Subtask subtask) {
        int setterId = subtask.getId(); // Если создаем задачу и вручную устанавливаем id

        if (setterId == subtaskIdCounter + 1 || setterId == epicIdCounter + 1 || setterId == taskIdCounter + 1) {
            subtaskIdCounter = getSubtaskId() + 1;
        }

        if (subtaskIdCounter <= epicIdCounter && subtaskIdCounter <= taskIdCounter) {
            subtaskIdCounter = Math.max(epicIdCounter, taskIdCounter) + 1;
            subtask.setId(subtaskIdCounter);
        } else {
            subtaskIdCounter++;
            int id = subtask.getId();
            subtask.setId(Math.max(id, subtaskIdCounter));
        }

        if (setterId != 0) {
            subtaskIdCounter = setterId;
            subtask.setId(setterId);
        }

        tasksSortedByStartTime.add(subtask);

        if (isIntersection(subtask)) {
            System.out.println("Подзадачи пересекаются. Время старта добавляемой задачи: <" + subtask.getTitle() + "> " + subtask.getStartTime() + ", время окончания: " + subtask.getEndTime() +
                    ". Имеется пересечение с задачей: <" + lastIntersection.getTitle() + ">. Время старта: " + lastIntersection.getStartTime() + ", время окончания: " + lastIntersection.getEndTime());
            tasksSortedByStartTime.remove(subtask);
            tasksSortedByStartTime.remove(epic);
            return;
        }

        subtasks.computeIfAbsent(epic, k -> new HashMap<>()).put(getSubtaskId(), subtask);

        subtask.setEpicId(epic.getId()); // получаем id эпика и устанавливаем его для подзадачи
        epic.setSubtasksId(subtaskIdCounter);

        epic.setStartTime(subtask);
        epic.setDuration(subtask);

        updateEpicStatus(subtask.getEpicId());
        tasksSortedByStartTime.add(epic);
    }

    //Удаление задачи по id из коллекции
    @Override
    public void removeTask(int id) {
        if (!commonTasks.containsKey(id)) {
            System.out.print("Задачи с таким id не существует!");
            return;
        }
        Task task = commonTasks.get(id);
        historyManager.remove(id);
        commonTasks.remove(id);
        tasksSortedByStartTime.remove(task);
        System.out.println("Задача успешно удалена!");
    }

    @Override
    public void removeEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.print("Эпика с таким id не существует!");
            return;
        }
        Epic epic = epics.get(id);

        if (subtasks.containsKey(epic)) {
            for (Subtask subtask : subtasks.get(epic).values()) {
                historyManager.remove(subtask.getId());
                subtasks.remove(epic);
                tasksSortedByStartTime.remove(subtask);
            }
            subtasks.remove(epic);
        }
        historyManager.remove(id);
        epics.remove(id);
        tasksSortedByStartTime.remove(epic);
        System.out.println("Задача успешно удалена!");
    }

    @Override
    public void removeSubtask(Epic epic, int id) {
        if (!subtasks.containsKey(epic)) {
            System.out.print("Подзадачи с таким id не существует!");
            return;
        }
        Subtask subtask = subtasks.get(epic).get(id);
        historyManager.remove(id);
        subtasks.get(epic).remove(id);
        tasksSortedByStartTime.remove(subtask);
        System.out.println("Задача успешно удалена!");
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask;

        for (Map<Integer, Subtask> map : subtasks.values()) {
            if (!map.containsKey(id)) {
                System.out.print("Подзадачи с таким id не существует!");
                return;
            } else {
                subtask = map.get(id);
                historyManager.remove(id);
                subtasks.get(epics.get(subtask.getEpicId())).remove(id);
                tasksSortedByStartTime.remove(subtask);
                System.out.println("Задача успешно удалена!");
            }
        }
    }

    //Обновление задачи
    @Override
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

    @Override
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

    @Override
    public void updateSubtask(int epicId, int subtaskId, String title, String description, Status status) {
        Epic epic = epics.get(epicId);
        if (!subtasks.containsKey(epic)) {
            System.out.println("Эпик с таким id отсутствует");
            return;
        }

        if (subtasks.get(epic).isEmpty()) {
            System.out.println("Подзадачи у эпика отсутствуют");
            return;
        }

        Subtask subtask = subtasks.get(epic).get(subtaskId);
        if (subtask == null) {
            System.out.println("Подзадача с таким id отсутствует");
            return;
        }

        subtask.setTitle(title);
        subtask.setDescription(description);
        subtask.setStatus(status);
        System.out.println("Подзадача обновлена");

        updateEpicStatus(epicId);
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

        newCount = (int) subtasks.get(epic).values().stream()
                .filter(subtask -> subtask.getStatus() == Status.NEW)
                .count();

        doneCount = (int) subtasks.get(epic).values().stream()
                .filter(subtask -> subtask.getStatus() == Status.DONE)
                .count();

        if (newCount == total) {
            epic.setStatus(Status.NEW);
        } else if (doneCount == total) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return tasksSortedByStartTime;
    }

    @Override
    public boolean isIntersection(Task task) {

        return getPrioritizedTasks().stream()
                .anyMatch(t -> {
                    if (task instanceof Subtask) {
                        if (((Subtask) task).getEpicId() == t.getId()) {
                            return false;
                        }
                    }

                    if (task instanceof Epic && t instanceof Subtask) {
                        if (task.getId() == ((Subtask) t).getEpicId()) {
                            return false;
                        }
                    }

                    if ((task.getStartTime().isBefore(t.getStartTime()) && task.getEndTime().isAfter(t.getEndTime()))
                            || (task.getStartTime().isBefore(t.getStartTime()) && task.getEndTime().isBefore(t.getEndTime()) && task.getEndTime().isAfter(t.getStartTime()))
                            || (task.getStartTime().isAfter(t.getStartTime()) && task.getEndTime().isBefore(t.getEndTime()))
                            || (task.getStartTime().isAfter(t.getStartTime()) && task.getEndTime().isAfter(t.getEndTime()) && task.getStartTime().isBefore(t.getEndTime())))  {
                        lastIntersection = t;
                        return true;
                    } else {
                        return false;
                    }
                });
    }
}
