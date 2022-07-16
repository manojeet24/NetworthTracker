package com.investingTech.demo.controllers;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.DataPoint;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.service.LivePrice;
import com.investingTech.demo.service.Ticker;
import com.investingTech.demo.utilities.keyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
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

    //Can be optimised by using Arraylist instead of Map
    public ArrayList<DataPoint> trackPortfolio(Map<String,String> tickerList, ArrayList<DataPoint> networthTrackingList, Map<String,String> portfolio){
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
        String current_date = date.toString();

        int last_day_index = networthTrackingList.size()-1;

        //If latest day value is already present
        if(networthTrackingList.get(last_day_index).getDate().equalsIgnoreCase(current_date)){
            //Update the array
            networthTrackingList.get(last_day_index).setDate(current_date);
            networthTrackingList.get(last_day_index).setNetWorth(portfolio_value_string);

            //Update the file
            writeFile.ArraytoFile(networthTrackingList,filePath);


        }
        //if not present add in array also in file
        else{
            DataPoint dataPoint = new DataPoint();
            dataPoint.setDate(current_date);
            dataPoint.setNetWorth(portfolio_value_string);
            networthTrackingList.add(dataPoint);
            writeFile.addNode(current_date,portfolio_value_string,filePath);
        }

        return networthTrackingList;
    }


}
