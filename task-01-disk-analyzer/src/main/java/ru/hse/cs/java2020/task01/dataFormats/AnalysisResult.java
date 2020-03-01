package ru.hse.cs.java2020.task01.dataFormats;

import java.util.List;
import java.util.SortedSet;

public class AnalysisResult {

    private FileInfo rootInfo;
    private List<FileInfo> firstLevelFileInfos;
    private SortedSet<FileInfo> biggestFileInfos;

    public AnalysisResult(FileInfo rootInfo, List<FileInfo> firstLevelFileInfos, SortedSet<FileInfo> biggestFileInfos) {
        this.rootInfo = rootInfo;
        this.firstLevelFileInfos = firstLevelFileInfos;
        this.biggestFileInfos = biggestFileInfos;
    }

    public FileInfo getRootInfo() {
        return rootInfo;
    }

    public List<FileInfo> getFirstLevelFileInfos() {
        return firstLevelFileInfos;
    }

    public SortedSet<FileInfo> getBiggestFileInfos() {
        return biggestFileInfos;
    }
}
