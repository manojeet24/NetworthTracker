package com.investingTech.demo.controllers;

import com.investingTech.demo.models.Portfolio;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.models.TrackNetworth;
import com.investingTech.demo.service.LivePriceTickertape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class PortfolioController {

    @Autowired
    LivePriceTickertape price;

    Stock stock = new Stock();

    @Autowired
    private MongoTemplate mongoTemplate;


    List<Portfolio> portfolioTrackingList;
    List<TrackNetworth> networthTrackingList;

    public List<TrackNetworth> updateNetworthTrackingList(Map<String,String> tickerList){
        float price_float = 0;
        float quantity_float = 0;
        float stock_value = 0;
        float portfolio_value =0;

        portfolioTrackingList = mongoTemplate.findAll(Portfolio.class);

        //Iterating over my Portfolio and calculating current Portfolio Value
        for (Portfolio value : portfolioTrackingList) {

            String company = value.getCompany_name();
            String quantity = value.getQuantity();

            System.out.println("Company: " + company);
            System.out.println("Qty: " + quantity);

            quantity_float = Float.parseFloat(quantity);

            //finding CMP of the Stock using TickerTape API
            stock = price.getPrice(company, tickerList);

            price_float = Float.parseFloat(stock.getPrice());

            stock_value = price_float * quantity_float;

            portfolio_value += stock_value;

        }

        //adding portfolio value
        String portfolio_value_string = Float.toString(portfolio_value);
        LocalDate date = LocalDate.now();
        String current_date = date.toString();

        Query query = new Query();
        query.addCriteria(Criteria.where("date").is(current_date));


        TrackNetworth latestDay = new TrackNetworth(current_date,portfolio_value_string);

        TrackNetworth findLatestDay = mongoTemplate.findOne(query,TrackNetworth.class);

        //Already Exists then use same ObjectId
        if(findLatestDay!=null){
            latestDay.set_id(findLatestDay.get_id());
        }

        mongoTemplate.save(latestDay);

        networthTrackingList = mongoTemplate.findAll(TrackNetworth.class);

        return networthTrackingList;
    }

    public String modifyPortfolio(String operation, String company, String qty){

//        System.out.println(operation + " " + company + " " + qty);

        //Add/Remove qty -> update
        //Add new Company -> Insert

        if(!operation.equalsIgnoreCase("buy") && !operation.equalsIgnoreCase("sell")) return "Invalid Operation";

        Query query = new Query();
        query.addCriteria(Criteria.where("company_name").is(company));

        portfolioTrackingList = mongoTemplate.find(query,Portfolio.class);

        //The Company Does not exists in Portfolio
        if(portfolioTrackingList.isEmpty()){
            if(operation.equalsIgnoreCase("buy")) {
                Portfolio portfolio = new Portfolio(company,qty);
                mongoTemplate.save(portfolio);
            }
            else{
                //Remove not allowed on Company which doesn't exist
                return "Invalid Operation";
            }
        }
        //Company Exists
        else {

            String prev_qty_string = portfolioTrackingList.get(0).getQuantity();

            Integer prev_qty = Integer.parseInt(prev_qty_string);

            if (operation.equalsIgnoreCase("buy")) {

                //adding qty
                Integer new_qty = prev_qty + Integer.parseInt(qty);

                Portfolio portfolio = new Portfolio(company,String.valueOf(new_qty));
                portfolio.set_id(portfolioTrackingList.get(0).get_id());
                mongoTemplate.save(portfolio);
            }
            else{
                //Removing qty
                Integer new_qty = prev_qty - Integer.parseInt(qty);

                if(new_qty<0)   return "Invalid Operation";

                if(new_qty == 0){
                    mongoTemplate.remove(query,Portfolio.class);
                }
                else{
                    Portfolio portfolio = new Portfolio(company,String.valueOf(new_qty));
                    portfolio.set_id(portfolioTrackingList.get(0).get_id());
                    mongoTemplate.save(portfolio);
                }
            }
        }
        return operation + " " + qty + " " + company + " Executed Successfully";
    }


}
