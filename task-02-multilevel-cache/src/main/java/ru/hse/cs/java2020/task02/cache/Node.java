package ru.hse.cs.java2020.task02.cache;

public class Node {

    private String value;
    private Node previous;
    private Node next;
    private long associatedKey;
    private long frequency;

    public Node(String value, Node previous, Node next, long associatedKey) {
        this.value = value;

        this.previous = previous;
        this.next = next;

        this.associatedKey = associatedKey;
    }

    public Node(String value, Node previous, Node next, long associatedKey, long frequency) {
        this.value = value;

        this.previous = previous;
        this.next = next;

        this.associatedKey = associatedKey;
        this.frequency = frequency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public long getAssociatedKey() {
        return associatedKey;
    }

    public long getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        this.frequency += 1;
    }
}
