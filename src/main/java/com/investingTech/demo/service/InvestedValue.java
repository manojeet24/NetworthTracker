package com.investingTech.demo.service;

import com.investingTech.demo.models.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvestedValue {

    private float investedValue = 0;

    @Autowired
    private MongoTemplate mongoTemplate;

    public String getInvestedValue(){
        List<Portfolio> portfolioTrackingList = mongoTemplate.findAll(Portfolio.class);
        for(int i=0;i<portfolioTrackingList.size();i++){
            Integer qty = Integer.parseInt((portfolioTrackingList.get(i).getQuantity()));
            Float buy_price = Float.parseFloat((portfolioTrackingList.get(i).getBuy_price()));

            investedValue  = investedValue + qty * buy_price;
        }

        return Float.toString(investedValue);
    }
}
