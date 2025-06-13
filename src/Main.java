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
        var epic2 = new Epic("Изучить ООП", "В подзадачах рассмотреть принципы ООП", Status.NEW);

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

        taskManager.printAllTask();
        System.out.println("-".repeat(100));
        taskManager.printAllEpic();
        System.out.println("-".repeat(100));
        taskManager.printAllSubtask();
        System.out.println("-".repeat(100));
        taskManager.printSubtaskByEpic(epic2); // должна вывести все подзадачи с второго эпика
        System.out.println("-".repeat(100));

        //Получение задач по id
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(2)); //должно вывести null
        System.out.println(taskManager.getSubtask(epic2, 1));
        System.out.println("-".repeat(100));

        //Удаление задачи по id
        System.out.println(taskManager.getTask(2)); // hashCode ...
        taskManager.removeTask(2);
        System.out.println(taskManager.getTask(2)); // Должно вывести null

        System.out.println(taskManager.getEpic(5)); // Java Collection Framework ...
        taskManager.removeEpic(4);
        System.out.println(taskManager.getEpic(5)); // Должно вывести null

        System.out.println(taskManager.getSubtask(epic2, 1)); // Set ..
        taskManager.removeSubtask(epic2, 1);
        System.out.println(taskManager.getSubtask(epic2, 1)); // Должно вывести Map ...
        System.out.println("-".repeat(100));

        //Обновляем задачи
        System.out.println(taskManager.getTaskStatus(task1)); // Должно вывести "NEW"
        taskManager.updateTask(task1, new Task("Тема изучена", "метод проверяет равенство объектов", Status.DONE));
        System.out.println(taskManager.getTaskStatus(task1) + "\n"); // Должно вывести "DONE"

        System.out.println(taskManager.getEpicStatus(epic2)); // Должно вывести "NEW"
        taskManager.updateEpic(epic2, new Epic("Тема изучена", "Принципы рассмотрены", Status.DONE));
        System.out.println(taskManager.getEpicStatus(epic2) + "\n"); // Должно вывести "NEW", так как статус зависит от подзадач

        System.out.println(taskManager.getSubtaskStatus(subtask5)); // Должно вывести "NEW"
        taskManager.updateSubtask(epic2, subtask5, new Subtask("Полиморфизм", "Как работает полиморфизм в Java", Status.IN_PROGRESS));
        System.out.println(taskManager.getSubtaskStatus(subtask5) + "\n"); // Должно вывести "IN_PROGRESS"

        System.out.println(taskManager.getSubtaskStatus(subtask4)); // Должно вывести "NEW"
        taskManager.updateSubtask(epic2, subtask4, new Subtask("Наследование", "Как работает наследование в Java", Status.NEW));
        System.out.println(taskManager.getSubtaskStatus(subtask4) + "\n"); // Должно вывести "NEW"

        System.out.println(taskManager.getSubtaskStatus(subtask6)); // Должно вывести "NEW"
        taskManager.updateSubtask(epic2, subtask6, new Subtask("Инкапсуляция", "Как работает инкапсуляция в Java", Status.NEW));
        System.out.println(taskManager.getSubtaskStatus(subtask6) + "\n"); // Должно вывести "NEW"

        System.out.println(taskManager.getEpicStatus(epic2)); // Должно вывести "NEW"
        taskManager.updateEpic(epic2, new Epic("Тема изучена", "Принципы рассмотрены", Status.NEW));
        System.out.println(taskManager.getEpicStatus(epic2)); // Должно вывести "IN_PROGRESS"
        System.out.println("-".repeat(100));

        //Удаление всех задач
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
        taskManager.removeAllSubtask();
        System.out.println("-".repeat(100));
    }
}
