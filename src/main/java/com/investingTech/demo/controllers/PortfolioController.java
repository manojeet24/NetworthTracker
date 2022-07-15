package com.investingTech.demo.controllers;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.service.LivePrice;
import com.investingTech.demo.service.Ticker;
import com.investingTech.demo.utilities.LoadFile;
import com.investingTech.demo.utilities.keyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class PortfolioController {

    @Autowired
    private YamlConfig yamlConfig;

    @Autowired
    LivePrice price;

    @Autowired
    Ticker ticker;

    @Autowired
    keyValue writeFile;

    Stock stock = new Stock();

    public Map<String,String> trackPortfolio(Map<String,String> tickerList,Map<String,String> networthTrackingList){
        //Loading my Portfolio from Portfolio.txt
        LoadFile loadMapfromFile = new LoadFile();
        Map<String,String> portfolio = loadMapfromFile.getMap(yamlConfig.getPortfolio_filePath());
        float price_float = 0;
        float quantity_float = 0;
        float stock_value = 0;
        float portfolio_value =0;
        //Iterating over my Portfolio
        for (String company : portfolio.keySet()){
            String quantity = portfolio.get(company);

            System.out.println("Company: " + company);
            System.out.println("Qty: " + quantity);
            quantity_float = Float.parseFloat(quantity);

            //finding CMP of the Stock
            stock = price.getPrice(company,ticker.getTicker(company,tickerList));

            price_float = Float.parseFloat(stock.getPrice());

            stock_value = price_float * quantity_float;

            portfolio_value += stock_value;

        }

        //adding portfolio value in Date:Portfolio format in NetworthTracking.txt
        String filePath = yamlConfig.getNetworthTracking_filePath();
        String portfolio_value_string = Float.toString(portfolio_value);
        LocalDate date = LocalDate.now();
        if(networthTrackingList.containsKey(date.toString())){
            networthTrackingList.remove(date.toString());
            networthTrackingList.put(date.toString(),portfolio_value_string);
            writeFile.MaptoFile(networthTrackingList,filePath);
        }
        else {
            networthTrackingList.put(date.toString(),portfolio_value_string);
            writeFile.addNode(date.toString(), portfolio_value_string, filePath);
        }

        return networthTrackingList;
    }

}
