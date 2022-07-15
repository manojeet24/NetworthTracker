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

@Component
public class LivePrice {

    @Autowired
    YamlConfig yamlConfig;

    private String price ="";
    private Integer volume = 0;

    private Stock stock= new Stock();

    public Stock getPrice(String company,String ticker) {
        try {
            String url =  yamlConfig.getGet_price_url() + ticker;
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
            stock.setName(company);
            stock.setPrice(price);
            stock.setVolume(volume);
        }catch (Exception e){
            e.printStackTrace();
            stock.setName("Invalid Company");
            stock.setPrice("###");
            stock.setVolume(-1);
            System.out.println("No company Found");
        }
        return stock;
    }

}
