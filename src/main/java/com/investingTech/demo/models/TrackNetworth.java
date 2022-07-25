package com.investingTech.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "NetworthTracker")
public class TrackNetworth {

    @Id
    private String _id;

    @Field("date")
    private String date;

    @Field("networth")
    private String networth;

    public TrackNetworth(String date, String networth) {
        this.date = date;
        this.networth = networth;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNetworth() {
        return networth;
    }

    public void setNetworth(String networth) {
        this.networth = networth;
    }
}
