package com.investingTech.demo.controllers;

import com.investingTech.demo.config.YamlConfig;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.models.TrackNetworth;
import com.investingTech.demo.service.LivePriceTickertape;
import com.investingTech.demo.utilities.LoadFile;
import org.springframework.beans.factory.annotation.Autowired;
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

@CrossOrigin(origins ="http://localhost:3000/")
@RestController
public class AppController {

    @Autowired
    YamlConfig yamlConfig;

    @Autowired
    LivePriceTickertape price;

    @Autowired
    LoadFile loadList;

    private Map<String,String> tickerList;

    @Autowired
    PortfolioController portfolio;


    @PostConstruct
    public void runAfterObjectCreated() {
        tickerList = loadList.getMap(yamlConfig.getTicker_filePath());
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
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            //Here we have mentioned it to show as attachment
            //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }

    @GetMapping("/")
    public String load(){
        return "Search a Company";
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