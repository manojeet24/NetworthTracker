package com.investingTech.demo.controller;

import com.investingTech.demo.models.Stock;
import com.investingTech.demo.service.LivePrice;
import com.investingTech.demo.utilities.LoadFile;
import com.investingTech.demo.service.Ticker;
import com.investingTech.demo.utilities.keyValue;

import java.time.LocalDate;
import java.util.Map;

public class PortfolioController {


    LivePrice price = new LivePrice();

    Ticker ticker = new Ticker();

    LoadFile loadFile = new LoadFile();
    private final String filePath = "./src/main/resources/Portfolio.txt";

    keyValue writeFile = new keyValue();

    Stock stock = new Stock();

    private  float portfolio_value = 0;

    public float getPortfolio(Map<String,String> tickerList){
        //Loading my Portfolio from Portfolio.txt
        Map<String,String> portfolio = loadFile.getMapfromFile(filePath);

        float price_float = 0;
        float quantity_float = 0;
        float stock_value = 0;

        //Iterating over my Portfolio
        for (String company : portfolio.keySet()){
            String quantity = portfolio.get(company);

            System.out.println("Company: " + company);
            System.out.println("Qty: " + quantity);
            quantity_float = Float.parseFloat(quantity);

            stock = price.getPrice(ticker.getTicker(company,tickerList));

            price_float = Float.parseFloat(stock.getPrice());

            stock_value = price_float * quantity_float;

            portfolio_value += stock_value;

        }
        System.out.println(portfolio);

        //adding portfolio value in Date:Portfolio format in NetworthTracking.txt
        String filePath = "./src/main/resources/NetworthTracking.txt";
        String portfolio_value_string = Float.toString(portfolio_value);
        LocalDate date = LocalDate.now();
        System.out.println(date);
        writeFile.addNode(date.toString(),portfolio_value_string,filePath);

        return portfolio_value;
    }

}
