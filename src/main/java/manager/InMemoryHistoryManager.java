package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> list = new LinkedList<>();
    private Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    public List<Task> getList() {
        return list;
    }

    private List<Task> getTasks() {
        return new ArrayList<>(list);
    }

    private void linkLast(Task task) { // Добавляет задачу в конец
        Node node = new Node(task, null, null);

        if (first == null) {
            first = last = node;
        } else {
            last.next = node;
            node.prev = last;
            last = node;
        }
        list.add(node.task);
    }

    private void removeNode(Node node, Task task) {
        if (nodeMap.containsValue(node) && node.task.equals(task)) {
            list.remove(node.task);
        } else {
            return;
        }

        if (last == first) {
            first = last = node;
        }

        if (node == first) {
            first = node.next;
            first.prev = null;
        } else if (node == last) {
            last = node.prev;
            last.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public void removeAll() {
        list.clear();
        nodeMap.clear();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int id = task.getId();
        removeNode(nodeMap.get(id), task);
        linkLast(task);
        nodeMap.put(id, last);
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        removeNode(node, node.task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
