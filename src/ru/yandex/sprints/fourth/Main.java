package ru.yandex.sprints.fourth;

import ru.yandex.sprints.fourth.manager.TaskManager;
import ru.yandex.sprints.fourth.tasks.Epic;
import ru.yandex.sprints.fourth.tasks.Status;
import ru.yandex.sprints.fourth.tasks.Subtask;
import ru.yandex.sprints.fourth.tasks.Task;

public class Main {
    public static void main(String[] args) {

        var taskManager = new TaskManager();

        // Создание и добавление простых задач
        var task1 = new Task("Изучить тему equals", "Определить для чего используется. Как его переопределить.", Status.NEW);
        var task2 = new Task("Изучить тему hashCode", "Определить для чего используется. Как его переопределить.", Status.NEW);
        var task3 = new Task("Изучить тему Comparable", "Определить для чего используется. Как его переопределить.", Status.NEW);
        var task4 = new Task("Изучить тему Comparator", "Определить для чего используется. Как его переопределить.", Status.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);

        //Создание и добавление эпиков
        var epic1 = new Epic("Изучить тему Java Collection Framework", "В подзадачах рассмотреть отдельные коллекции", Status.NEW);
        var epic2 = new Epic("Изучить ООП", "Рассмотрены часть тем", Status.NEW);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        //Создание и добавление подзадач для эпиков
        var subtask1 = new Subtask("Изучить тему List", "Рассмотреть реализации коллекции List", Status.NEW);
        var subtask2 = new Subtask("Изучить тему Set", "Рассмотреть реализации коллекции Set", Status.NEW);
        var subtask3 = new Subtask("Изучить тему Map", "Рассмотреть реализации коллекции Map", Status.NEW);
        var subtask4 = new Subtask("Изучить тему Наследование", "Определение, ключевые слова extends и super", Status.NEW);
        var subtask5 = new Subtask("Изучить тему Полиморфизм", "Определение, интерфейсы и абстрактные классы", Status.NEW);
        var subtask6 = new Subtask("Изучить тему Инкапсуляция", "Определение, геттеры и сеттеры", Status.NEW);

        taskManager.createSubtask(epic1, subtask1);
        taskManager.createSubtask(epic1, subtask2);
        taskManager.createSubtask(epic1, subtask3);
        taskManager.createSubtask(epic2, subtask4);
        taskManager.createSubtask(epic2, subtask5);
        taskManager.createSubtask(epic2, subtask6);

        System.out.println(epic1.getSubtasksid()); // Выводим id подзадач из эпика 1 (должно быть 1, 2, 3)
        System.out.println(subtask6.getEpicid()); // Выводим id эпика, в которой хранится подзадача (должно быть 6)

        //Чтобы убрать скобки и запятые при выводе через sout пришлось делать такой костыль
        System.out.println(taskManager.getAllTask().toString()
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "") + "-".repeat(100));
        System.out.println(taskManager.getAllEpic().toString()
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "") + "-".repeat(100));
        System.out.println(taskManager.getSubtaskByEpic(epic2).toString()
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "") + "-".repeat(100));

        //Получение задач по id
        System.out.print(taskManager.getTask(1).toString().replace("[", "").replace("]", ""));
        System.out.print(taskManager.getEpic(5).toString().replace("[", "").replace("]", ""));
        System.out.print(taskManager.getSubtask(epic2, 1).toString().replace("[", "").replace("]", ""));
        System.out.println("-".repeat(100));

        //Удаление задачи по id
        taskManager.removeTask(2);
        taskManager.removeEpic(5);
        taskManager.removeSubtask(epic2, 1);
        System.out.println("-".repeat(100));

        //Обновляем задачи
        taskManager.updateTask(1, "Тема изучена", "метод проверяет равенство объектов", Status.DONE);
        taskManager.updateEpic(6, "Тема изучена", "Коллекции рассмотрены");
        //Спасибо за развернутый ответ
        taskManager.updateSubtask(6, 2, "Полиморфизм", "Как работает полиморфизм в Java", Status.IN_PROGRESS);
        taskManager.updateSubtask(6, 1, "Наследование", "Как работает наследование в Java", Status.NEW);
        taskManager.updateSubtask(6, 3, "Инкапсуляция", "Как работает инкапсуляция в Java", Status.DONE);
        System.out.println("-".repeat(100));

        //Удаление всех задач
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
        taskManager.removeAllSubtask();
        System.out.println("-".repeat(100));
    }
}
