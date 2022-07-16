package com.investingTech.demo.controllers;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.DataPoint;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.service.LivePrice;
import com.investingTech.demo.service.Ticker;
import com.investingTech.demo.utilities.LoadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;

@CrossOrigin(origins ="http://localhost:3000/")
@RestController
public class AppController {

    @Autowired
    YamlConfig yamlConfig;

    @Autowired
    LivePrice price;

    @Autowired
    Ticker ticker;

    @Autowired
    LoadFile loadList;

    private Map<String,String> tickerList,portfolioList;

    private ArrayList<DataPoint> networthTrackingList;

    @Autowired
    PortfolioController portfolio;

    @PostConstruct
    public void runAfterObjectCreated() {
        tickerList = loadList.getMap(yamlConfig.getTicker_filePath());
        networthTrackingList = loadList.getArray(yamlConfig.getNetworthTracking_filePath());
        portfolioList = loadList.getMap(yamlConfig.getPortfolio_filePath());
//        System.out.println(tickerList);
    }

    @GetMapping("/")
    public String load(){
        return "Search a Company";
    }

    @GetMapping(value = "/{company}")
    public Stock stock(@PathVariable("company") String company) {
        System.out.println("company:" + company);
        return price.getPrice(company,ticker.getTicker(company,tickerList));
    }

    @GetMapping("/trackportfolio")
    public ArrayList<DataPoint> trackPortfolio(){
        return networthTrackingList = portfolio.trackPortfolio(tickerList,networthTrackingList,portfolioList);
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

}