package ru.hse.cs.java2020.task02.cache;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class Disk {

    private final Path dataFile = Paths.get("dataFile.dat");

    private long currentDiskSize = 0;
    private long maxDiskSize;

    private HashMap<Long, DataInDiskInfo> dataInDiskInfos;

    private Path path;

    public Disk(long maxDiskSize, Path path) throws IOException {
        this.maxDiskSize = maxDiskSize;
        this.path = path;
        this.dataInDiskInfos = new HashMap<>();

        if (Files.exists(path.resolve(dataFile))) {
            Files.delete(path.resolve(dataFile));
        }
        Files.createFile(path.resolve(dataFile));
    }

    public String getFromDisk(long key) throws IOException {
        var dataInfo = dataInDiskInfos.get(key);
        if (dataInfo == null) {
            return null;
        }

        char[] data = new char[dataInfo.getLength()];
        try (var reader = new FileReader(path.resolve(dataFile).toString())) {
            reader.skip(dataInfo.getOffset());
            reader.read(data, 0, dataInfo.getLength());
        }

        return new String(data);
    }

    public String putToDisk(long key, String value) throws Exception {
        String oldValue =  put(key, value, dataFile);

        if (currentDiskSize > maxDiskSize) {
            compacting();
        }

        return oldValue;
    }

    private String put(long key, String value, Path targetFile) throws Exception {
        byte[] data = value.getBytes();

        try (RandomAccessFile writer = new RandomAccessFile(new File(path.resolve(targetFile).toString()), "rw")) {
            writer.seek(currentDiskSize);
            writer.write(data);
        }

        dataInDiskInfos.put(key, new DataInDiskInfo((int) currentDiskSize, data.length));
        currentDiskSize += data.length;

        return null;
    }



    public void deleteFromDisk(long key) throws IOException {
        var dataInfo = dataInDiskInfos.get(key);
        if (dataInfo == null) {
            return;
        }

        try (RandomAccessFile writer = new RandomAccessFile(new File(path.resolve(dataFile).toString()), "rw")) {
            writer.seek(dataInfo.getOffset());
            var nullBytes = new byte[dataInfo.getLength()];
            Arrays.fill(nullBytes, (byte) 0);
            writer.write(nullBytes);
        }

        dataInDiskInfos.remove(key);
    }


    private void compacting() throws Exception {
        Path tmpFile = Paths.get("compactingFile.dat");
        try {
            if (!Files.exists(path.resolve(tmpFile))) {
                Files.createFile(path.resolve(tmpFile));
            }

            currentDiskSize = 0;

            for (var key : dataInDiskInfos.keySet()) {
                String value = getFromDisk(key);
                put(key, value, tmpFile);
                dataInDiskInfos.put(key, new DataInDiskInfo((int) currentDiskSize, value.length()));
            }
        } finally {
            if (Files.exists(path.resolve(dataFile))) {
                Files.delete(path.resolve(dataFile));
            }
            Files.move(path.resolve(tmpFile), path.resolve(dataFile));
        }

        if (currentDiskSize > maxDiskSize) {
            throw new Exception("Disk overflow");
        }
    }
}
