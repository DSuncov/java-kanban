package tasks;

public enum TaskType {
    TASK,
    EPIC,
    SUBTASK;

    public static TaskType getType(Task task) {
        if (task instanceof Subtask) {
            return SUBTASK;
        } else if (task instanceof Epic) {
            return EPIC;
        } else {
            return TASK;
        }
    }
}
