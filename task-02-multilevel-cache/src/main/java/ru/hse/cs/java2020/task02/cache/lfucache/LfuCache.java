package ru.hse.cs.java2020.task02.cache.lfucache;

import ru.hse.cs.java2020.task02.cache.Cache;
import ru.hse.cs.java2020.task02.cache.Disk;
import ru.hse.cs.java2020.task02.cache.Node;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class LfuCache implements Cache {

    private HashMap<Long, Node> dataInMemory;
    private Node leastFrequencyNode;
    private Node mostFrequencyNode;

    private long maxMemorySize;
    private long currentMemorySize = 0;

    private Disk disk;

    public LfuCache(long maxMemorySize, long maxDiskSize, Path path) throws IOException {
        this.maxMemorySize = maxMemorySize;
        this.disk = new Disk(maxDiskSize, path);
        this.dataInMemory = new HashMap<>();
    }

    @Override
    public String get(long key) throws IOException {
        String value = getFromMemory(key);
        if (value != null) {
            return value;
        }

        value = disk.getFromDisk(key);
        disk.deleteFromDisk(key);
        if (value != null) {
            putToMemory(key, value);
        }

        return value;
    }

    private String getFromMemory(long key) {
        Node currentNode = dataInMemory.get(key);
        if (currentNode == null) {
            return null;
        }

        updateUsage(currentNode);

        return currentNode.getValue();
    }

    private void updateUsage(Node currentNode) {
        Node previousNode = currentNode.getPrevious();
        Node nextNode = currentNode.getNext();

        currentNode.incrementFrequency();

        if (currentNode == mostFrequencyNode) {
            return;
        }

        if (currentNode.getFrequency() > nextNode.getFrequency()) {
            nextNode.setPrevious(currentNode.getPrevious());
            currentNode.setNext(nextNode.getNext());
            currentNode.setPrevious(nextNode);
            nextNode.setNext(currentNode);

            if (nextNode.getNext() != null) {
                nextNode.getNext().setPrevious(currentNode);
            }
            if (previousNode != null) {
                previousNode.setNext(nextNode);
            }
            if (nextNode == mostFrequencyNode) {
                mostFrequencyNode = currentNode;
            }
        }
    }

    @Override
    public String put(long key, String value) throws Exception {
        if (value.equals("")) {
            throw new Exception("incorrect input value");
        }

        while (currentMemorySize + value.length() > maxMemorySize) {
            Node deletedNode = deleteFromMemory();
            disk.putToDisk(deletedNode.getAssociatedKey(), deletedNode.getValue());
        }

        String oldValue = putToMemory(key, value);
        disk.deleteFromDisk(key);

        return oldValue;
    }

    private String putToMemory(long key, String value) throws IOException {
        Node currentNode = dataInMemory.get(key);

        if (currentNode != null) {
            String oldValue = currentNode.getValue();
            currentNode.setValue(value);

            return oldValue;
        }

        Node newNode = new Node(value, null, leastFrequencyNode, key, 1);

        if (leastFrequencyNode != null) {
            leastFrequencyNode.setPrevious(newNode);
        }

        if (mostFrequencyNode == null) {
            mostFrequencyNode = newNode;
        }

        leastFrequencyNode = newNode;
        dataInMemory.put(key, newNode);

        currentMemorySize += newNode.getValue().length();

        return null;
    }

    private Node deleteFromMemory() {
        Node deletedNode = leastFrequencyNode;
        if (deletedNode.getNext() != null) {
            deletedNode.getNext().setPrevious(null);
        }

        leastFrequencyNode = deletedNode.getNext();
        currentMemorySize -= deletedNode.getValue().length();
        dataInMemory.remove(deletedNode.getAssociatedKey());

        return deletedNode;
    }
}
