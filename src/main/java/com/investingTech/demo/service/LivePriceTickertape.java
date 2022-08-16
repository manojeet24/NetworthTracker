package com.investingTech.demo.service;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.Stock;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class LivePriceTickertape {

    @Autowired
    YamlConfig yamlConfig;

    @Autowired
    LivePriceAutomatic livePriceAutomatic;

    private String price ="";
    private Integer volume = 0;

    private Stock stock= new Stock();

    public Stock getPrice(String company, Map<String,String> tickerList) {
        try {
            String ticker = "";
            String New_company = company + " Ltd";
            New_company = New_company.toLowerCase();
            //if already in TickerList
            if(tickerList.containsKey(New_company)) {
                System.out.println("Found in TickerList");
                ticker = tickerList.get(New_company);
                System.out.println("Ticker: " + ticker);
                JSONArray pointsArray = livePrice(ticker);
                int last_index = pointsArray.length() - 1;
                JSONObject obj = (JSONObject) pointsArray.get(last_index);
                volume = (Integer) obj.get("v");
                price = obj.get("lp").toString();
                System.out.println("Volume:" + volume);
                System.out.println("Price:" + price);
                stock.setName(format(New_company));
                stock.setPrice(price);
                stock.setVolume(volume);
            }
            //Hit different API
            else{
                stock = livePriceAutomatic.getPrice(company);
            }
        }catch (Exception e){
            e.printStackTrace();
            stock.setName("Invalid Company");
            stock.setPrice("###");
            stock.setVolume(-1);
            System.out.println("No company Found");
        }
        return stock;
    }

    public JSONArray livePrice(String ticker){
        JSONArray pointsArray = new JSONArray();
        String url = yamlConfig.getGet_price_url() + ticker;
        try {
            JSONObject json = new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
            System.out.println(url);
            JSONArray dataArray = (JSONArray) json.get("data");
            pointsArray = (JSONArray) ((JSONObject) dataArray.get(0)).get("points");
        }catch (Exception e){
            e.printStackTrace();
        }
        return pointsArray;
    }

    //Capitalise the first Character of each word
    private String format(String company){
        String[] arrOfStr = company.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<arrOfStr.length;i++){
            String temp = arrOfStr[i];
            char first = Character.toUpperCase(temp.charAt(0));
            if(i == arrOfStr.length-1)
                sb.append(first).append(temp.substring(1));
            else
                sb.append(first).append(temp.substring(1)).append(" ");
        }
        return sb.toString();
    }

}
