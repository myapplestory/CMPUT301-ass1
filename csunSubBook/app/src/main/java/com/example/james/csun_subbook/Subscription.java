package com.example.james.csun_subbook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by James on 2/3/2018.
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

    Subscription(String name, Date date, Float amount){
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.comment = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws NameTooLongException {
        if (name.length() < 20){
            this.name = name;
        }
        else{
            throw new NameTooLongException();
        }
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

    public void setAmount(Float amount) throws AmountNegativeException{
        if (amount >= 0.0) {
            this.amount = amount;
        }
        else {
            throw new AmountNegativeException();
        }
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws CommentTooLongException{
        if (comment.length() < 30){
            this.comment = comment;
        }
        else{
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }

    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        return "Name: " + name + "\nDate: " + dateFormat.format(date) + "\n" +
                String.format(Locale.CANADA, "$%.2f",amount);
    }
}