package com.investingTech.demo.controller;

import com.investingTech.demo.models.Stock;
import com.investingTech.demo.service.LivePrice;
import com.investingTech.demo.utilities.LoadFile;
import com.investingTech.demo.service.Ticker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@ComponentScan
@RestController
public class Controller {

    LivePrice price = new LivePrice();

    Ticker ticker = new Ticker();

    LoadFile loadFile = new LoadFile("./src/main/resources/Company_Ticker.txt");

    private final Map<String, String> tickerList = loadFile.getMap();
    private final PortfolioController portfolio = new PortfolioController();

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
        return portfolio.getPortfolio(tickerList);
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

}