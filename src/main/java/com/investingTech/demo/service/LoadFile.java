//This Class is responsible to load a file into a HashMap

package com.investingTech.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LoadFile {
    public  Map<String, String> getTickerList() {
        Map<String, String> map = new HashMap<>();

        String filePath = "./src/main/resources/Company_Ticker.txt";

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> line.contains(":"))
                    .forEach(line -> {
                        String[] keyValuePair = line.split(":", 2);
                        String key = keyValuePair[0];
                        String value = keyValuePair[1];
//                        System.out.println("key: " + key);
//                        System.out.println("value: " + value);
                        map.putIfAbsent(key,value);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
