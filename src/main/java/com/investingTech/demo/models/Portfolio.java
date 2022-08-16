package com.investingTech.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Portfolio")
public class Portfolio {

    @Id
    private String _id;

    @Field("company_name")
    private String company_name;

    @Field("quantity")
    private String quantity;

    @Field("buy_price")
    private String buy_price;

    public Portfolio(String company_name, String quantity, String buy_price) {
        this.company_name = company_name;
        this.quantity = quantity;
        this.buy_price = buy_price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }
}
