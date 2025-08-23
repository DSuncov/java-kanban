package manager;

import tasks.Task;

public class Node {
    Node next;
    Task task;
    Node prev;

    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

//    @Override
//    public String toString() {
//        return new StringBuilder("Node: ")
//                .append("data=").append(data)
//                .append(", next=").append(next)
//                .append(", prev=").append(prev).append("\n").toString();
//    }
}
