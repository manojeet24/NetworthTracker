package com.investingTech.demo.service;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.utilities.LoadFile;
import com.investingTech.demo.utilities.keyValue;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class LivePriceAutomatic {

    @Autowired
    YamlConfig yamlConfig;

    private final keyValue storeTicker = new keyValue();

    public Stock getPrice(String company) {
        Stock stock = new Stock();
        String url = yamlConfig.getAutoCompleteStock_url() + removeSpaces(company);
        try {
            System.out.println(url);
            JSONObject json = new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
            JSONObject data = (JSONObject) json.get("data");
            JSONArray results = (JSONArray) data.get("results");
            JSONObject best_match_company = (JSONObject) results.get(0);
            JSONObject stock_info = (JSONObject) best_match_company.get("quote");
            int volume = (int) stock_info.get("volume");
            stock.setName(best_match_company.get("name").toString());
            stock.setPrice(stock_info.get("price").toString());
            stock.setVolume(volume);


            LoadFile loadList = new LoadFile();
            Map<String,String> tickerList = loadList.getMap(yamlConfig.getTicker_filePath());

            //Add Ticker in Company_Ticker for faster process
            if(!tickerList.containsKey(stock.getName().toLowerCase()))
                storeTicker.addNode(stock.getName().toLowerCase(),stock_info.get("sid").toString(),yamlConfig.getTicker_filePath());
        }catch (Exception e){
            e.printStackTrace();
        }
        return stock;
    }

    private String removeSpaces(String company){
        String[] arrOfStr = company.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<arrOfStr.length-1;i++){
            sb.append(arrOfStr[i]).append("%20");
        }
        sb.append(arrOfStr[arrOfStr.length-1]);
        return sb.toString();
    }
}
