import java.util.Objects;

public class Task {

    private String title;
    private String description;
    private int id;
    private Status status;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (title != null) {
            result = title.hashCode();
        }
        if (description != null) {
            result += description.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                status == task.status;
    }

    @Override
    public String toString() {
        return "Задача (" +
                "Название: " + title +
                "; описание: " + description +
                "; id: " + id +
                "; статус:" + status +
                "; хэш-код объекта: " + hashCode() +
                ')';
    }
}
