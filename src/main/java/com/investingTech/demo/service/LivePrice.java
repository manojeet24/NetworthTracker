package com.investingTech.demo.service;

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

    public String getPrice(String ticker) {
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
        }catch (Exception e){
            e.printStackTrace();
            return "No company Found";
        }
        return "Price: " + price + " and Volume: " + volume;
    }

}
