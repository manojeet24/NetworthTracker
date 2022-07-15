//This Class is responsible to store new Company_name:Ticker_name pair to Company_Ticker File

package com.investingTech.demo.utilities;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;

@Component
public class keyValue {
    public void addNode(String key, String value, String filePath){

        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(filePath, true));  //appends
            output.append(key).append(":").append(value).append("\n");
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void MaptoFile(Map<String,String> list, String filePath){

        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(filePath));  //overwrites
            for (String key : list.keySet()) {
                String value = list.get(key);
                output.append(key).append(":").append(value).append("\n");
            }
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
