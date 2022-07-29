package com.investingTech.demo.controllers;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.Portfolio;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.models.TrackNetworth;
import com.investingTech.demo.service.InvestedValue;
import com.investingTech.demo.service.LivePriceTickertape;
import com.investingTech.demo.utilities.LoadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

//Next Release
//ToDo: Add Invested amount column in TrackNetworth => Add Buying Price column in Portfolio Table

//@CrossOrigin(origins ="http://localhost:3000/")
@CrossOrigin(origins ="*")
@RestController
public class AppController {

    @Autowired
    YamlConfig yamlConfig;

    @Autowired
    LivePriceTickertape price;

    @Autowired
    LoadFile loadList;

    private Map<String,String> tickerList;
    private List<Portfolio> portfolioTrackingList;
    private List<TrackNetworth> trackNetworthList;

    @Autowired
    private InvestedValue investedValue;

    @Autowired
    private PortfolioController portfolio;

    @Autowired
    private MongoTemplate mongoTemplate;


    @PostConstruct
    public void runAfterObjectCreated() {
        tickerList = loadList.getMap(yamlConfig.getTicker_filePath());
        System.out.println("MongoDB Collections:" + mongoTemplate.getCollectionNames());
    }

    @GetMapping("/")
    public String load(){
        return "Search a Company";
    }

    @GetMapping("/loadDatabase")
    public String loadDatabase(){
//        Map<String, String> portfolioTrackingList = loadList.getMap(yamlConfig.getPortfolio_filePath());
//        List<Portfolio> portfolioList = mongoTemplate.findAll(Portfolio.class);
//        int index=0;
//        for (String key : portfolioTrackingList.keySet()) {
//            String qty_val = portfolioTrackingList.get(key);
//            String[] keyValuePair = qty_val.split(":", 2);
//            System.out.println("Qty:" + keyValuePair[0]);
//            System.out.println("Buy Price:" + keyValuePair[1]);
//            Portfolio p = portfolioList.get(index++);
//            p.setBuy_price(keyValuePair[1]);
//            mongoTemplate.save(p);
//        }

//        Map<String, String> NetworthTrackingList = loadList.getMap(yamlConfig.getNetworthTracking_filePath());
//        List<TrackNetworth> networthList = mongoTemplate.findAll(TrackNetworth.class);
//        int index = 0;
//        for (String key : NetworthTrackingList.keySet()) {
//            String val = NetworthTrackingList.get(key);
//            String[] keyValuePair = val.split(":", 2);
//            System.out.println("Current Value:" + keyValuePair[0]);
//            System.out.println("Invested Value:" + keyValuePair[1]);
//            TrackNetworth t = networthList.get(index++);
//            t.setInvested(keyValuePair[1]);
//            mongoTemplate.save(t);
//        }
        return "Database Loaded from text files";
    }

    @GetMapping("/tickerlist")
    public void getTickerList(HttpServletRequest request, HttpServletResponse response) throws IOException {

        File file = new File(yamlConfig.getTicker_filePath());
        if (file.exists()) {
            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                //unknown mimetype so set the mimetype to application/octet-stream
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }

    @GetMapping("/portfoliolist")
    public List<Portfolio> getPortfolioList(){
        portfolioTrackingList = mongoTemplate.findAll(Portfolio.class);
        return portfolioTrackingList;
    }

    @GetMapping("/networthlist")
    public List<TrackNetworth> getNetworthList(){
        trackNetworthList = mongoTemplate.findAll(TrackNetworth.class);
        return trackNetworthList;
    }

    @GetMapping("/loadticker")
    public String loadTickerList(){
        tickerList = loadList.getMap(yamlConfig.getTicker_filePath());
        return "tickerList loaded from Company_ticker.txt";
    }
    @GetMapping(value = "/{company}")
    public Stock stock(@PathVariable("company") String company) {
        System.out.println("company:" + company);
        return price.getPrice(company,tickerList);
    }

    @GetMapping(value = "/{operation}/{company}/{qty}/{curr_buy_price}")
    public String addStock(@PathVariable("operation") String operation, @PathVariable("company") String company, @PathVariable("qty") String qty, @PathVariable("qty") String buyprice) {
        return portfolio.modifyPortfolio(operation, company, qty, buyprice);
    }

    @Scheduled(fixedDelay = 1800000)    //run after every 30mins to prevent sleeping in Heroku
    @GetMapping("/trackportfolio")
    public List<TrackNetworth> trackPortfolio(){
        return portfolio.updateNetworthTrackingList(tickerList);
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

}