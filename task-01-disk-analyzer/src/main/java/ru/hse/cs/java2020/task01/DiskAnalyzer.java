package ru.hse.cs.java2020.task01.diskAnylyzer;

import ru.hse.cs.java2020.task01.dataFormats.AnalysisResult;
import ru.hse.cs.java2020.task01.dataFormats.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class DiskAnalyzer {

    static final int DEFAULT_BIGGEST_FILES_COUNT = 10;

    private DiskAnalyzer() {

    }

    public static AnalysisResult analyze(Path rootFilePath) throws IOException {
        return analyze(rootFilePath, DEFAULT_BIGGEST_FILES_COUNT);
    }

    public static AnalysisResult analyze(Path rootFilePath, int biggestFilesCount) throws IOException {
        long rootSize = 0;
        long rootItemsCount = 0;
        List<FileInfo> firstLevelFileInfos = new LinkedList<>();
        TreeSet<FileInfo> biggestFileInfos = new TreeSet<>();

        for (var file : listFolderContent(rootFilePath)) {
            var q  = new LinkedList<Path>();
            q.add(file);

            long size = 0;
            long itemsCount = 0;

            while (!q.isEmpty()) {
                var current = q.removeFirst();

                if (!Files.isDirectory(current)) {
                    size += Files.size(current);
                    itemsCount += 1;

                    biggestFileInfos.add(new FileInfo(current, Files.size(current), 0));
                    if (biggestFileInfos.size() > biggestFilesCount) {
                        biggestFileInfos.pollLast();
                    }
                } else {
                    q.addAll(listFolderContent(current));
                }
            }

            rootSize += size;
            rootItemsCount += itemsCount;
            firstLevelFileInfos.add(new FileInfo(file, size, itemsCount));
        }

        var rootFileInfo = new FileInfo(rootFilePath, rootSize, rootItemsCount);

        return new AnalysisResult(rootFileInfo, firstLevelFileInfos, biggestFileInfos);
    }

    private static List<Path> listFolderContent(Path path) throws IOException {
        var content = new ArrayList<Path>();

        Files.list(path).forEach((p) -> {if (pathCheck(p)) content.add(p);});

        return content;
    }

    private static boolean pathCheck(Path path) {
        try {
            if (Files.isDirectory(path)) {
                Files.size(path);
            } else if (Files.isSymbolicLink(path)) {
                return false;
            } else {
                Files.size(path);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    }

