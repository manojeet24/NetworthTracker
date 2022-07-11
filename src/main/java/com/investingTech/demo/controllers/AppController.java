package com.investingTech.demo.controllers;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.service.LivePrice;
import com.investingTech.demo.service.Ticker;
import com.investingTech.demo.utilities.LoadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;


@RestController
public class AppController {

    @Autowired
    YamlConfig yamlConfig;

    @Autowired
    LivePrice price;

    @Autowired
    Ticker ticker;

    @Autowired
    LoadFile loadTickerList;

    private Map<String,String> tickerList;

    @Autowired
    PortfolioController portfolio;

    @PostConstruct
    public void runAfterObjectCreated() {
        tickerList = loadTickerList.getMap(yamlConfig.getTicker_filePath());
//        System.out.println(tickerList);
    }

    @GetMapping("/")
    public String load(){
        return "Search a Company";
    }

    @GetMapping(value = "/{company}")
    public Stock stock(@PathVariable("company") String company) {
        System.out.println("company:" + company);
        return price.getPrice(ticker.getTicker(company,tickerList));
    }

    @GetMapping("/portfolio")
    public float portfolioValue(){
        return portfolio.getPortfolioValue(tickerList);
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

}