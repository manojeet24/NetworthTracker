//This Class is responsible to load a file into a HashMap

package com.investingTech.demo.utilities;

import com.investingTech.demo.models.TrackNetworth;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class LoadFile {

    public Map<String, String> getMap(String filePath) {
        Map<String, String> map = new LinkedHashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> line.contains(":"))
                    .forEach(line -> {
                        String[] keyValuePair = line.split(":", 2);
                        String key = keyValuePair[0];
                        String value = keyValuePair[1];
//                        System.out.println("key: " + key);
//                        System.out.println("value: " + value);
                        map.putIfAbsent(key, value);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public ArrayList<TrackNetworth> getArray(String filePath) {
        ArrayList<TrackNetworth> trackNetworths = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> line.contains(":"))
                    .forEach(line -> {
                        TrackNetworth trackNetworth = new TrackNetworth();
                        String[] keyValuePair = line.split(":", 2);
                        trackNetworth.setDate(keyValuePair[0]);
                        trackNetworth.setNetworth(keyValuePair[1]);
                        trackNetworths.add(trackNetworth);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trackNetworths;
    }
}
