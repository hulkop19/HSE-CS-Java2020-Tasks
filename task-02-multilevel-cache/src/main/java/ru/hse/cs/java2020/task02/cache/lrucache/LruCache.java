package ru.hse.cs.java2020.task02.cache.lrucache;

import ru.hse.cs.java2020.task02.cache.Disk;
import ru.hse.cs.java2020.task02.cache.Cache;
import ru.hse.cs.java2020.task02.cache.Node;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class LruCache implements Cache {

    private HashMap<Long, Node> dataInMemory;
    private Node leastRecentlyUsed;
    private Node mostRecentlyUsed;

    private long maxMemorySize;
    private long currentMemorySize = 0;

    private Disk disk;

    public LruCache(long maxMemorySize, long maxDiskSize, Path path) throws IOException {
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
        if (value != null) {
            putToMemory(key, value);
            disk.deleteFromDisk(key);
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

        if (currentNode == mostRecentlyUsed) {
            return;
        }

        if (currentNode == leastRecentlyUsed) {
            nextNode.setPrevious(null);
            leastRecentlyUsed = nextNode;
        } else {
            previousNode.setNext(nextNode);
            nextNode.setPrevious(previousNode);
        }

        currentNode.setPrevious(mostRecentlyUsed);
        currentNode.setNext(null);
        mostRecentlyUsed = currentNode;
    }

    @Override
    public String put(long key, String value) throws Exception {
        if (value.equals("")) {
            throw new Exception("incorrect input value");
        }

        String oldValue = putToMemory(key, value);
        disk.deleteFromDisk(key);

        while (currentMemorySize > maxMemorySize) {
            Node deletedNode = deleteFromMemory();
            disk.putToDisk(deletedNode.getAssociatedKey(), deletedNode.getValue());
        }

        return oldValue;
    }

    private String putToMemory(long key, String value) throws IOException {
        Node currentNode = dataInMemory.get(key);

        if (currentNode != null) {
            String oldValue = currentNode.getValue();
            currentNode.setValue(value);

            return oldValue;
        }

        Node newNode = new Node(value, mostRecentlyUsed, null, key);

        if (mostRecentlyUsed != null) {
            mostRecentlyUsed.setNext(newNode);
        }

        if (leastRecentlyUsed == null) {
            leastRecentlyUsed = newNode;
        }

        mostRecentlyUsed = newNode;
        dataInMemory.put(key, newNode);

        currentMemorySize += newNode.getValue().length();

        return null;
    }

    private Node deleteFromMemory() {
        Node deletedNode = leastRecentlyUsed;
        if (deletedNode.getNext() != null) {
            deletedNode.getNext().setPrevious(null);
        }

        leastRecentlyUsed = deletedNode.getNext();
        currentMemorySize -= deletedNode.getValue().length();
        dataInMemory.remove(deletedNode.getAssociatedKey());

        return deletedNode;
    }
}
