package com.investingTech.demo.controllers;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.models.TrackNetworth;
import com.investingTech.demo.service.LivePrice;
import com.investingTech.demo.service.Ticker;
import com.investingTech.demo.utilities.LoadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

//ToDO: Connect with Database

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

    private Map<String,String> tickerList;

    @Autowired
    PortfolioController portfolio;


    @PostConstruct
    public void runAfterObjectCreated() {
        tickerList = loadList.getMap(yamlConfig.getTicker_filePath());
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

    @GetMapping(value = "/{operation}/{company}/{qty}")
    public String addStock(@PathVariable("operation") String operation, @PathVariable("company") String company, @PathVariable("qty") String qty) {
        return portfolio.modifyPortfolio(operation, company, qty);
    }

    @GetMapping("/trackportfolio")
    public List<TrackNetworth> trackPortfolio(){
        return portfolio.trackPortfolio(tickerList);
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

}