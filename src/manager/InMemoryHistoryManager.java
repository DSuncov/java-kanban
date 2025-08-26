package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    final private Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private List<Task> getTasks() {
        List<Task> resultList = new ArrayList<>();
        Node currentNode = first;
        while (currentNode != null) {
            resultList.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return resultList;
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
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            first = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            last = node.prev;
        }
        nodeMap.remove(node.task.getId());
    }

    @Override
    public void removeAll() {
        nodeMap.clear();
        first = null;
        last = null;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int id = task.getId();
        Node oldNode = nodeMap.get(id);
        if (oldNode != null) {
            removeNode(oldNode);
        }
        linkLast(task);
        nodeMap.put(id, last);
    }

    @Override
    public void remove(int id) {
        removeNode(nodeMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
