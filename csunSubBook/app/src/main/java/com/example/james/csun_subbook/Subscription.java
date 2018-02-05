package com.example.james.csun_subbook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * created on Feb 2 2018
 * @author James Sun
 *
 * this class is contains data about a subscription and returns the details of a subscription
 * as well as the toString function which returns a string that contains the data to be presented
 *
 */

public abstract class Subscription implements Subscribber {
    private String name;
    private Date date;
    private Float amount;
    private String comment;

    Subscription(String name, Date date, Float amount, String comment) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.comment = comment;
    }

    // setters and getters, nothing special
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount){
        this.amount = amount;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        return "Name: " + name + "\nDate: " + dateFormat.format(date) + "\n" +
                String.format(Locale.CANADA, "$%.2f",amount);
    }
}