package com.investingTech.demo.controller;

import com.investingTech.demo.service.LivePrice;
import com.investingTech.demo.service.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan
@RestController
public class Controller {

    @Autowired
    LivePrice price;

    @Autowired
    Ticker ticker;


    @GetMapping(value = "/{company}")
    public String stock(@PathVariable("company") String company) {
        System.out.println("company:" + company);
        return price.getPrice(ticker.getTicker(company));
    }

}