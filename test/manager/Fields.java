package manager;

import tasks.Status;

public class Fields {
    static Status DEFAULT_STATUS = Status.NEW;
    static String[] TASK_TITLE = {"Задача № 1", "Задача № 2", "Задача № 3", "Задача № 4", "Задача № 5", "Задача № 6"};
    static String[] SUBTASK_TITLE = {"Подзадача № 1.1", "Подзадача № 1.2", "Подзадача № 1.3", "Подзадача № 2.1", "Подзадача № 2.2", "Подзадача № 2.3"};
    static String[] TASK_DESCRIPTION = {"Описание № 1", "Описание № 2", "Описание № 3", "Описание № 4", "Описание № 5", "Описание № 6"};
    static String[] SUBTASK_DESCRIPTION = {"Описание № 1.1", "Описание № 1.2", "Описание № 1.3", "Описание № 2.1", "Описание № 2.2", "Описание № 2.3"};
    static String[] TASKDATETIME = {"2025-09-22 09:00", "2025-09-22 10:00", "2025-09-22 11:00", "2025-09-22 12:00"};
    static Long[] TASKDURATION = {60L, 45L, 50L, 30L};
    static String[] SUBTASKDATETIME = {"2025-09-22 14:00", "2025-09-22 14:20", "2025-09-22 14:40", "2025-09-22 15:00", "2025-09-22 15:30", "2025-09-22 16:00"};
    static Long[] SUBTASKDURATION = {20L, 10L, 15L, 5L, 25L, 30L};
}
