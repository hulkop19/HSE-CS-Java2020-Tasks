package ru.hse.cs.java2020.task02;

import ru.hse.cs.java2020.task02.cache.CacheFactory;
import ru.hse.cs.java2020.task02.cache.EvictionPolicy;
import ru.hse.cs.java2020.task02.cache.lfucache.LfuCache;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        var cache = CacheFactory.createCache(100, 11000, Paths.get("C:\\Users\\hulko\\Desktop\\"), EvictionPolicy.LFU);
//
        String s1 = "Hellohello";
        String s2 = "bebebebebe";
        cache.put(1, s1);
//        cache.put(2, s2);
        cache.get(1);
        cache.get(2);
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));
        System.out.println("foo");


//        for (int i = 0; i < 1000; ++i) {
//            cache.put(i, "lel " + i + " lel");
//        }
//
//        for (int i = 0; i < 50; ++i) {
//            cache.get(i);
//        }
//
//        for (int i = 1000; i < 1001; ++i) {
//            cache.put(i, "lol " + i + " lol");
//        }
//
//        cache.get(3);
//        cache.get(900);
    }
}
