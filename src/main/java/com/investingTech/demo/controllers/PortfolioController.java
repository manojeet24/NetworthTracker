package com.investingTech.demo.controllers;

import com.investingTech.demo.models.Portfolio;
import com.investingTech.demo.models.Stock;
import com.investingTech.demo.models.TrackNetworth;
import com.investingTech.demo.service.LivePriceTickertape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
    JdbcTemplate jdbcTemplate;


    List<Portfolio> portfolioTrackingList;
    List<TrackNetworth> networthTrackingList;

    //Can be optimised by using Arraylist instead of Map
    public List<TrackNetworth> trackPortfolio(Map<String,String> tickerList){
        float price_float = 0;
        float quantity_float = 0;
        float stock_value = 0;
        float portfolio_value =0;

        portfolioTrackingList = jdbcTemplate.query("SELECT * FROM Portfolio", new BeanPropertyRowMapper<>(Portfolio.class));

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

        //if Record already exists
        int alreadyExists = jdbcTemplate.update("UPDATE Networth_Track set Networth = ? where Date = ?", portfolio_value_string, current_date);

        //else
        if(alreadyExists == 0){
            jdbcTemplate.update("INSERT INTO Networth_Track (Date, Networth) VALUES (?, ?)", current_date, portfolio_value_string);
        }

        networthTrackingList = jdbcTemplate.query("SELECT * FROM Networth_Track", new BeanPropertyRowMapper<>(TrackNetworth.class));

        return networthTrackingList;
    }

    public String modifyPortfolio(String operation, String company, String qty){

//        System.out.println(operation + " " + company + " " + qty);

        //Add/Remove qty -> update
        //Add new Company -> Insert

        String company_sql = "'" + company + "'";

        if(!operation.equalsIgnoreCase("buy") && !operation.equalsIgnoreCase("sell")) return "Invalid Operation";

        portfolioTrackingList = jdbcTemplate.query("SELECT * FROM Portfolio where company_name = " + company_sql, new BeanPropertyRowMapper<>(Portfolio.class));

        if(portfolioTrackingList.isEmpty()){
            if(operation.equalsIgnoreCase("buy")) {
                jdbcTemplate.update("INSERT INTO Portfolio (company_name, quantity) VALUES (?, ?)", company, qty);
            }
            else{
                //Remove not allowed on Company which doesn't exist
                return "Invalid Operation";
            }
        }
        else {

            String prev_qty_string = portfolioTrackingList.get(0).getQuantity();

            Integer prev_qty = Integer.parseInt(prev_qty_string);

            if (operation.equalsIgnoreCase("buy")) {

                //adding qty
                Integer new_qty = prev_qty + Integer.parseInt(qty);

                jdbcTemplate.update("UPDATE Portfolio set quantity = ? where company_name = ?", String.valueOf(new_qty), company);
            }
            else{
                //Removing qty
                Integer new_qty = prev_qty - Integer.parseInt(qty);

                if(new_qty<0)   return "Invalid Operation";

                if(new_qty == 0){
                    jdbcTemplate.update("DELETE from Portfolio where company_name = ?", company);
                }
                else{
                    jdbcTemplate.update("UPDATE Portfolio set quantity = ? where company_name = ?", String.valueOf(new_qty), company);
                }
            }
        }
        return operation + " " + qty + " " + company + " Executed Successfully";
    }


}
