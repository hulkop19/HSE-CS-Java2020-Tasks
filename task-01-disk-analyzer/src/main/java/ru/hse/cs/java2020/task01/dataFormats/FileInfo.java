package ru.hse.cs.java2020.task01.dataFormats;

import java.nio.file.Path;

public class FileInfo implements Comparable<FileInfo> {

    private Path filePath;
    private long size;
    private long itemsCount;

    public FileInfo(Path filePath, long size, long itemsCount) {
        this.filePath = filePath;
        this.size = size;
        this.itemsCount = itemsCount;
    }

    public Path getFilePath() {
        return filePath;
    }

    public long getItemsCount() {
        return itemsCount;
    }

    public long getSize() {
        return size;
    }

    @Override
    public int compareTo(FileInfo fileInfo) {
        if (fileInfo.getSize() - this.size != 0) {
            return Long.compare(fileInfo.getSize(), this.size);
        } else {
            return fileInfo.getFilePath().toString().compareTo(this.filePath.toString());
        }
    }
}
