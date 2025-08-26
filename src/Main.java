import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {

        var taskManager = Managers.getDefault();

        // Создание и добавление простых задач
        var task1 = new Task("Задача № 1", "Описание задачи № 1", Status.NEW);
        var task2 = new Task("Задача № 2", "Описание задачи № 2", Status.NEW);
        var task3 = new Task("Задача № 3", "Описание задачи № 3", Status.NEW);
        var task4 = new Task("Задача № 4", "Описание задачи № 4", Status.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);

        //Создание и добавление эпиков
        var epic1 = new Epic("Эпик № 1", "Описание эпика № 1", Status.NEW);
        var epic2 = new Epic("Эпик № 2", "Описание эпика № 2", Status.NEW);
        var epic3 = new Epic("Эпик № 3", "Описание эпика № 3", Status.NEW);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        //Создание и добавление подзадач для эпиков
        var subtask1 = new Subtask("Подзадача № 1 эпика № 1", "Описание подзадачи № 1 эпика № 1", Status.NEW);
        var subtask2 = new Subtask("Подзадача № 2 эпика № 1", "Описание подзадачи № 2 эпика № 1", Status.NEW);
        var subtask3 = new Subtask("Подзадача № 3 эпика № 1", "Описание подзадачи № 3 эпика № 1", Status.NEW);
        var subtask4 = new Subtask("Подзадача № 1 эпика № 2", "Описание подзадачи № 1 эпика № 2", Status.NEW);
        var subtask5 = new Subtask("Подзадача № 2 эпика № 2", "Описание подзадачи № 2 эпика № 2", Status.NEW);
        var subtask6 = new Subtask("Подзадача № 3 эпика № 2", "Описание подзадачи № 3 эпика № 2", Status.NEW);

        taskManager.createSubtask(epic1, subtask1);
        taskManager.createSubtask(epic1, subtask2);
        taskManager.createSubtask(epic1, subtask3);
        taskManager.createSubtask(epic2, subtask4);
        taskManager.createSubtask(epic2, subtask5);
        taskManager.createSubtask(epic2, subtask6);

        printAllTasks(taskManager);

        System.out.println(epic1.getSubtasksId()); // Выводим id подзадач из эпика 1 (должно быть 8, 9, 10)
        System.out.println(subtask6.getEpicid()); // Выводим id эпика, в которой хранится подзадача (должно быть 6)

        System.out.println(taskManager
                .getAllTask()
                .toString()
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "") + "-".repeat(100));
        System.out.println(taskManager
                .getAllEpic()
                .toString()
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "") + "-".repeat(100));
        System.out.println(taskManager
                .getSubtaskByEpic(epic2)
                .toString()
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "") + "-".repeat(100));

        //Получение задач по id
        System.out.print(taskManager
                .getTask(1)
                .toString()
                .replace("[", "").replace("]", ""));
        System.out.print(taskManager
                .getEpic(5)
                .toString()
                .replace("[", "").replace("]", ""));
        System.out.print(taskManager
                .getSubtask(epic2, 11)
                .toString()
                .replace("[", "").replace("]", ""));
        System.out.println("-".repeat(100));

        //Удаление задачи по id
        taskManager.removeTask(2);
        taskManager.removeEpic(5);
        taskManager.removeSubtask(epic2, 11);
        System.out.println("-".repeat(100));

        //Обновляем задачи
        taskManager.updateTask(1, "Задача № 1 обновлена", "Описание обновленной задачи № 1", Status.DONE);
        taskManager.updateEpic(7, "Эпик № 2 обновлен", "Описание обновленного эпика № 2");
        taskManager.updateSubtask(6, 11, "Подзадача № 2 эпика № 2 обновлена", "Описание обновленной подзадачи № 2 эпика № 2", Status.IN_PROGRESS);
        taskManager.updateSubtask(6, 12, "Подзадача № 1 эпика № 2 обновлена", "Описание обновленной подзадачи № 1 эпика № 2", Status.NEW);
        taskManager.updateSubtask(6, 13, "Подзадача № 3 эпика № 2 обновлена", "Описание обновленной подзадачи № 3 эпика № 2", Status.DONE);
        System.out.println("-".repeat(100));

        //Удаление всех задач
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
        taskManager.removeAllSubtask();
        System.out.println("-".repeat(100));
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTask()) {
            manager.getHistoryManager().add(task);
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpic()) {
            manager.getHistoryManager().add(epic);
            System.out.println(epic);

            if (manager.getSubtasksList().get(epic) == null) {
                System.out.println("Подзадач у данного эпика нет");
                continue;
            }

            System.out.println("Подзадачи:");

            for (Task task : manager.getSubtaskByEpic(epic)) {
                manager.getHistoryManager().add(task);
                System.out.println("--> " + task);
            }
        }

        System.out.println("История:");
        for (Task task : manager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }
    }
}
