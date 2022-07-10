//This Class is responsible to store new Company_name:Ticker_name pair to Company_Ticker File

package com.investingTech.demo.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class StoreTicker {
    public void addTicker(String company, String ticker){

        String filePath = "./src/main/resources/Company_Ticker.txt";

        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(filePath, true));  //clears file every time
            output.append("\n").append(company).append(":").append(ticker);
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
