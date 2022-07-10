package com.investingTech.demo.service;

import com.investingTech.demo.utilities.keyValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Ticker {

    private final keyValue storeTicker = new keyValue();
    public String getTicker(String Company, Map<String,String> tickerList){

        Company = Company.trim();
        Company += " Ltd";

        //Look in Map first
        if(tickerList!=null && tickerList.containsKey(Company)){
            System.out.println("Found in Map: " + tickerList.get(Company));
            return tickerList.get(Company);
        }


        String url = "https://www.tickertape.in/stocks?filter=" + Company.charAt(0);

        String ticker = "";

        try {
            Document doc = Jsoup.connect(url).get();
            Elements stocks = doc.select("a[href]");
            for (Element stock : stocks) {
                // get the value from the href attribute
                if(Company.equalsIgnoreCase(stock.text())){
//                    System.out.println("Stocks Text: " + stock);
                    String tick = stock.attr("href");
                    ticker = extractTicker(tick);
                    String filePath = "./src/main/resources/Company_Ticker.txt";
                    storeTicker.addNode(Company,ticker,filePath);
                    System.out.println("Ticker:" + ticker);
                    return ticker;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Bad Ticker:" + Company);
        return ticker;
    }

    String extractTicker(String link){
        StringBuilder ticker = new StringBuilder();
        for(int index = link.length()-1;index>=0;index--){
            if(link.charAt(index) == '-')
                break;
            ticker.insert(0, link.charAt(index));
        }
        return ticker.toString();
    }


}
