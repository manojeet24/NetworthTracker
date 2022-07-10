package com.investingTech.demo.service;

import com.investingTech.demo.models.Stock;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class LivePrice {

    private String price ="";
    private Integer volume = 0;

    private Stock stock= new Stock();

    public Stock getPrice(String ticker) {
        try {
            String url = "https://api.tickertape.in/stocks/charts/intra/" + ticker;
            JSONObject json = new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
            System.out.println(url);
            JSONArray dataArray = (JSONArray) json.get("data");
            JSONArray pointsArray = (JSONArray) ((JSONObject) dataArray.get(0)).get("points");
            int last_index = pointsArray.length()-1;
            JSONObject obj = (JSONObject) pointsArray.get(last_index);
            volume = (Integer) obj.get("v");
            price = obj.get("lp").toString();
            System.out.println("Volume:" + volume);
            System.out.println("Price:" + price);
            stock.setPrice(price);
            stock.setVolume(volume);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("No company Found");
        }
        return stock;
    }

}
