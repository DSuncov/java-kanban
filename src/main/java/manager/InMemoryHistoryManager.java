package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_LIST_SIZE = 10;
    private List<Task> historyList = new ArrayList<>(HISTORY_LIST_SIZE);

    @Override
    public void add(Task task) {
        if (historyList.size() == HISTORY_LIST_SIZE) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
