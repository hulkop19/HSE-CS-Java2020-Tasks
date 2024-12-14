package ru.hse.cs.java2020.task02.cache;

import ru.hse.cs.java2020.task02.cache.lfucache.LfuCache;
import ru.hse.cs.java2020.task02.cache.lrucache.LruCache;

import java.io.IOException;
import java.nio.file.Path;

public final class CacheFactory {

    private CacheFactory() {

    }

    public static Cache createCache(long memorySize, long diskSize, Path path, EvictionPolicy policy) throws IOException {
        switch (policy) {
            case LRU:
                return new LruCache(memorySize, diskSize, path);
            case LFU:
                return new LfuCache(memorySize, diskSize, path);
            default:
                //TODO add exception
                return null;
        }
    }
}
